package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IPatientFactory;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.PatientDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
public class PatientService implements IPatientServiceLocal, IPatientServiceRemote {
	
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
			patientDAO.addPatient(patient);
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
		try{
			Patient patient = patientDAO.getPatient(id);			
			return patientDtoFactory.createPatientDto(patient); 
		} catch(PatientExn e){
			throw new PatientServiceExn(e.getMessage());
		}
	}

	/**
	 * @see IPatientService#getPatientByPatId(long)
	 */
	@Override
	public PatientDto getPatientByPatId(long pid) throws PatientServiceExn {
		// TODO use DAO to get patient by patient id
		try{
			Patient patient = patientDAO.getPatientByPatientId(pid);			
			return patientDtoFactory.createPatientDto(patient); 
		} catch(PatientExn e){
			throw new PatientServiceExn(e.getMessage());
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
			// TODO Auto-generated method stub	
			TreatmentDto dto = factory.createTreatmentDto();
			dto.setDiagnosis(diagnosis);
			RadiologyType radioInfo = factory.createRadiologyType();
			radioInfo.setDate(dates);
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
	public TreatmentDto[] getTreatment(long id, long[] tid)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		// Export treatment DTO from patient aggregate
		try {
			Patient patient = patientDAO.getPatient(id);
			TreatmentDto[] treatments = new TreatmentDto[tid.length];
			
			for(int i=0; i<tid.length; i++){
				TreatmentExporter visitor = new TreatmentExporter();
				patient.exportTreatment(tid[i], visitor);
				treatments[i]=visitor.getDto();
			}		
			return treatments;
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
	public void addDrugTraeatment(long id, String diagnosis, String Drug, float dosage) throws PatientServiceExn {
		try{
			Provider provider = new Provider();
			Patient patient = patientDAO.getPatient(id);
			patient.addDrugTreatment(null, diagnosis, dosage, provider);
		} catch(PatientExn n){
			throw new PatientNotFoundExn(n.toString());
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
			patient.addRadiologyTreatment(radiologyDates, diagnosis, provider);
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
			patient.addSurgeryTreatment(surgeryDate, diagnosis, provider);
		} catch(PatientExn n){
			throw new PatientNotFoundExn(n.toString());
		}
	}
}
