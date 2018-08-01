package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.Topic;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.treatment.*;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientService.class.getCanonicalName());

	private IPatientFactory patientFactory;
	
	private PatientDtoFactory patientDtoFactory;

	private IPatientDAO patientDAO;

	@Inject @ClinicDomain
	private EntityManager em;
	
	/**
	 * Default constructor.
	 */
	public PatientService() {
		// TODO initialize factories
		patientFactory = new PatientFactory();
		patientDtoFactory = new PatientDtoFactory();
	}
	
	@PostConstruct
	private void initialize(){
		patientDAO = new PatientDAO(em);
	}
	
	// TODO use dependency injection and EJB lifecycle methods to initialize DAOs

	/**
	 * @see IPatientService#addPatient(String, Date, long)
	 */
	@Override
	public long addPatient(PatientDto dto) throws PatientServiceExn {
		// Use factory to create patient entity, and persist with DAO
		try {
			Patient patient = patientFactory.createPatient(dto.getPatientId(), dto.getName(), dto.getDob(), dto.getAge());
			logger.info("PatientDTO addpatient method: " + patient);
			patientDAO.addPatient(patient);
			logger.info("Patient Id in db: " + patient.getId());
			return patient.getId();
		} catch (PatientExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	/**
	 * @see IPatientService#getPatient(long)
	 */
	@Override
	public PatientDto getPatient(long id) throws PatientServiceExn {
		// TODO use DAO to get patient by database key
		PatientDto patientdto;
		try{
			logger.info("PatientDTO getpatient method");
			Patient patient = patientDAO.getPatient(id);
			logger.info("value of patient:" +patient);
			patientdto = patientDtoFactory.createPatientDto(patient); 
			logger.info("value of patientdto: " +patientdto);
		} catch(PatientExn e){
			throw new PatientServiceExn("Inside PatientService getPatient(lomg id) method: "+e.getMessage());
		}
		return patientdto;
	}

	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		// TODO use DAO to get patient by patient id
		PatientDto patientdto;
		try{
			Patient patient = patientDAO.getPatientByPatientId(pid);			
			patientdto = patientDtoFactory.createPatientDto(patient); 
		} catch(PatientExn e){
			throw new PatientServiceExn(e.getMessage());
		}
		return patientdto;
	}
	
		@Resource(mappedName = "jms/TreatmentPool")
	private ConnectionFactory treatmentConnFactory;

	@Resource(mappedName = "jms/Treatment")
	private Topic treatmentTopic;

	Logger logger1 = Logger.getLogger("edu.stevens.cs548.clinic.service.ejb");

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
			// TODO Auto-generated method stub	
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radioInfo = factory.createRadiologyType();
			//radioInfo.setDate(dates);
			dto.setRadiology(radioInfo);
			return dto;
		}

		@Override
		public TreatmentDto exportSurgery(long tid, String diagnosis, Date date) {
			// TODO Auto-generated method stub	
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgeryInfo = factory.createSurgeryType();
			surgeryInfo.setDate(date);
			dto.setSurgery(surgeryInfo);
			return dto;
		}
	}
	
	@Override
	public TreatmentDto getTreatment(long id, long tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			logger.info("Patient service treatmentdto get treatment method ");
			logger.info("Patient id: " + id);
			logger.info("Treatment id: " + tid);
			
			Patient patient = patientDAO.getPatient(id);
			logger.info("get Patient id: " + patient.getId());
			TreatmentExporter visitor = new TreatmentExporter();
			
			return patient.exportTreatment(tid, visitor);
			//return null;
			//return treatments;
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString());
		} catch (TreatmentExn e) {
			throw new PatientServiceExn(e.toString());
		}
	}

	// TODO inject resource value
	@Resource(name="SiteInfo")
	public String siteInformation;
	

	@Override
	public String siteInfo() {
		return siteInformation;
	}

	@Override
	public void addDrugTraeatment(long id, String diagnosis, String drug, float dosage) throws PatientServiceExn {
		
		Connection treatmentConn = null;
		try {
			Provider provider = new Provider();
			Patient patient = patientDAO.getPatient(id);
			long tid = patient.addDrugTreatment(diagnosis, drug, dosage, provider);
			logger1.info("drug treatment of patient service added, jms function incoming");
			treatmentConn = treatmentConnFactory.createConnection();
			Session session = treatmentConn.createSession(true, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(treatmentTopic);

			TreatmentDto treatment = new TreatmentDto();
			treatment.setId(tid);
			treatment.setPatient(id);
			treatment.setDiagnosis(diagnosis);
			treatment.setProvider(provider.getId());
			DrugTreatmentType dt = new DrugTreatmentType();
			dt.setName(drug);
			dt.setDosage(dosage);
			// dt.setPrescribingPhysician("Dr. Max");
			treatment.setDrugTreatment(dt);
			ObjectMessage message = session.createObjectMessage();
			message.setObject(treatment);
			message.setStringProperty("treatmentType", "Drug");
			producer.send(message);
		} catch (PatientExn e) {
			throw new PatientNotFoundExn(e.toString() + "jms problem in patient service");
		} catch (JMSException e) {
			logger1.severe("JMS Error: " + e);
		} finally {
			try {
				if (treatmentConn != null)
					treatmentConn.close();
			} catch (JMSException e) {
				logger1.severe("Error closing JMS connection " + e);
			}

		}

		
	}	

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// TODO Auto-generated method stub
	}

	@Override
	public void addRadiology(long id, List<Date> radiologyDates, String diagnosis) throws PatientServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider provider = new Provider();
			Patient patient = patientDAO.getPatient(id);
			patient.addRadiology(radiologyDates, provider, diagnosis);
		} catch(PatientExn n){
			throw new PatientNotFoundExn(n.toString());
		}
	}

	@Override
	public void addSurgery(long id, Date surgeryDate, String diagnosis) throws PatientServiceExn {
		// TODO Auto-generated method stub
		try{
			Provider provider = new Provider();
			Patient patient = patientDAO.getPatient(id);
			patient.addSurgery( diagnosis, surgeryDate, provider);
		} catch(PatientExn n){
			throw new PatientNotFoundExn(n.toString());
		}
	}
}
