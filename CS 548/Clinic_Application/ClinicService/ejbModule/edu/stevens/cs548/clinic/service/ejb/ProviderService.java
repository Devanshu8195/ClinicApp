package edu.stevens.cs548.clinic.service.ejb;

import java.nio.file.ProviderNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
//import edu.stevens.cs548.clinic.service.dto.treatment.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;

/**
 * Session Bean implementation class ProviderService
 */
@Stateless
@LocalBean
public class ProviderService implements IProviderServiceRemote, IProviderServiceLocal {

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(ProviderService.class.getCanonicalName());

	private ProviderFactory providerFactory;
	
	private ProviderDtoFactory providerDtoFactory;

	private IProviderDAO providerDAO;
	
	private IPatientDAO patientDAO;
	
    /**
     * Default constructor. 
     */
    public ProviderService() {
        // TODO Auto-generated constructor stub
		providerFactory = new ProviderFactory();
		providerDtoFactory = new ProviderDtoFactory();
    }

    @Inject @ClinicDomain
	private EntityManager em;
	
	@PostConstruct
	private void initialize(){
		providerDAO = new ProviderDAO(em);
		patientDAO = new PatientDAO(em);
		
	}
	@Override
	public long addProvider(ProviderDto dto) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try {
			Provider provider = providerFactory.createProvider(dto.getNpi(), dto.getName(), dto.getSpecialization());
			providerDAO.addProvider(provider);
			return provider.getId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

	@Override
	public ProviderDto getProvider(long id) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider provider = providerDAO.getProviderById(id);			
			ProviderDto providerdto = providerDtoFactory.createProviderDto(provider); 
			return providerdto;
		} catch(ProviderExn e){
			throw new ProviderServiceExn(e.getMessage());
		}
		
	}

	@Override
	public ProviderDto getProviderByNPI(long pid) throws ProviderServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider provider = providerDAO.getProviderByNPI(pid);			
			ProviderDto providerdto = providerDtoFactory.createProviderDto(provider); 
			return providerdto; 
		} catch(ProviderExn e){
			throw new ProviderServiceExn(e.getMessage());
		}
	}

	@Resource(name="SiteInfo")
	private String siteInformation;

	@Override
	public String siteInfo() {
		// TODO Auto-generated method stub
		return siteInformation;
	}
	
	@Resource(mappedName = "jms/TreatmentPool")
	private ConnectionFactory treatmentConnFactory;

	@Resource(mappedName = "jms/Treatment")
	private Topic treatmentTopic;
	
	@Override
	public long addTreatmentForPat(TreatmentDto treatment, long patientId, long providerId)
			throws TreatmentNotFoundExn, PatientNotFoundExn, ProviderServiceExn, JMSException {
		Connection treatmentConn = null;
		try {

			Provider prov = providerDAO.getProviderById(providerId);
			
			TreatmentFactory treatmentFactory = new TreatmentFactory();
			logger.info("Inside addtreatmentforpat method");
			logger.info("Provider id: " + providerId + " treatment id: " + treatment.getProvider());
			if (providerId != treatment.getProvider()) {
				throw new ProviderServiceExn("some thing wrong for provider with id = " + providerId);
			}
			logger.info("Drug treatment value: " + treatment.getDrugTreatment());
			if (treatment.getDrugTreatment() != null) {
				logger.info("Before treatment factory connection creation");
				treatmentConn = treatmentConnFactory.createConnection();
				Session session = treatmentConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(treatmentTopic);
				logger.info("After Creating Producer");
				Treatment t = treatmentFactory.createDrugTreatment(treatment.getDiagnosis(),
						treatment.getDrugTreatment().getName(), treatment.getDrugTreatment().getDosage());
				logger.info("Create Treatment Object");
				t.setProvider(prov);
				logger.info("Set provider and before message creation");
				ObjectMessage message = session.createObjectMessage();
				message.setObject(treatment);
				message.setStringProperty("treatmentType", "Drug");
				producer.send(message);
				logger.info("After sending message");
				return patientDAO.getPatient(patientId).addTreatment(t);
			} else if (treatment.getSurgery() != null) {
				treatmentConn = treatmentConnFactory.createConnection();
				Session session = treatmentConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(treatmentTopic);

				Treatment t = treatmentFactory.createSurgery(treatment.getDiagnosis(),
						treatment.getSurgery().getDate());
				t.setProvider(prov);
				ObjectMessage message = session.createObjectMessage();
				message.setObject(treatment);
				message.setStringProperty("treatmentType", "Surgery");
				producer.send(message);
				return patientDAO.getPatient(patientId).addTreatment(t);
			} else if (treatment.getRadiology() != null) {
				treatmentConn = treatmentConnFactory.createConnection();
				Session session = treatmentConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
				MessageProducer producer = session.createProducer(treatmentTopic);

				Treatment t = treatmentFactory.createRadiology(treatment.getDiagnosis(),
						treatment.getRadiology().getDate());
				t.setProvider(prov);
				ObjectMessage message = session.createObjectMessage();
				message.setObject(treatment);
				message.setStringProperty("treatmentType", "Radiology");
				producer.send(message);
				return patientDAO.getPatient(patientId).addTreatment(t);
			} else {
				throw new TreatmentNotFoundExn("treatment not found!");
			}
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}

	}

	
	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn, ProviderServiceExn {
		// TODO Auto-generated method stub
		try {
//			Provider provider =  providerDAO.getProviderById(id);
//			TreatmentDto[] treatments = new TreatmentDto[tid.length];
//			
//			for(int i=0; i<tid.length; i++){
//				TreatmentExporter visitor = new TreatmentExporter();
//				provider.exportTreatment(tid[i], visitor);
//				treatments[i]=visitor.getDto();
//			}
			Provider provider = providerDAO.getProviderById(id);
			TreatmentExporter visitor = new TreatmentExporter();
			return provider.exportTreatment(tid, visitor);
			//return null;
			//return treatments;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}
	

public class TreatmentExporter implements ITreatmentExporter<TreatmentDto> {
		
		private ObjectFactory factory = new ObjectFactory();
		
		private TreatmentDto dto;
		public TreatmentDto getDto(){
			return dto;
		}
		
		@Override
		public TreatmentDto exportDrugTreatment(long tid, String diagnosis, String drug, float dosage) {
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = factory.createDrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportRadiology(long tid, String diagnosis, List<Date> dates) {
			 
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radioInfo = factory.createRadiologyType();
			//radioInfo.setDate(dates);
			dto.setRadiology(radioInfo);
			return dto;
			
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date) {
			 
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeryInfo = factory.createSurgeryType();
			surgeryInfo.setDate(date);
			dto.setSurgery(surgeryInfo);
			return dto;
			
		}
		
	}

}
