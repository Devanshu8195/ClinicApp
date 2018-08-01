package edu.stevens.cs548.clinic.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.persistence.EntityManager;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;

import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.PatientDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;

import edu.stevens.cs548.clinic.billing.service.IBillingServiceLocal;

import edu.stevens.cs548.clinic.service.ejb.ClinicDomain;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;

/**
 * Session Bean implementation class TestBean
 */
@Singleton
@LocalBean
@Startup
public class InitBean {

	private static Logger logger = Logger.getLogger(InitBean.class.getCanonicalName());
	// TODO inject an EM
	@Inject @ClinicDomain
	EntityManager em;
	/**
	 * Default constructor.
	 */
	public InitBean() {
	}
    
	@Inject
	IPatientServiceLocal patientService;
	@Inject
	IProviderServiceLocal providerService;
	@Inject
	IBillingServiceLocal billingService;


	@PostConstruct
	private <T> void init() {
		/*
		 * Put your testing logic here. Use the logger to display testing output in the server logs.
		 */
		logger.info("Your name here: Devanshu Mehta");

		try {

			//Calendar calendar = Calendar.getInstance();
			//calendar.set(1984, 9, 4);
			
			IPatientDAO patientDAO = new PatientDAO(em);
			ITreatmentDAO treatmentDAO = new TreatmentDAO(em);
			IProviderDAO providerDAO = new ProviderDAO(em);		
			
			PatientFactory patientFactory = new PatientFactory();
			TreatmentFactory treatmentFactory = new TreatmentFactory();
			ProviderFactory providerFactory = new ProviderFactory();
			
			
			
			/*
			 * TODO Clear the database and populate with fresh data.
			 * 
			 * TODO Do testing with patients, providers and treatments.
                         * Write results of testing to the logs.
			 */
			
			patientDAO.deletePatients();
			providerDAO.deleteProviders();
			
			// TODO add more testing, including treatments and providers
			
			//Creating and Adding Patient Ishan Pandya
			Patient pandu = patientFactory.createPatient(12332112L, "Ishan Pandya", setDate("11/27/1992"), 25);
			patientDAO.addPatient(pandu);
			logger.info("Added " + pandu.getName() + " with id " + pandu.getId());
			
			//Creating and Adding Patient John Doe
			Patient john = patientFactory.createPatient(12345678L, "John Doe", setDate("11/26/1990"), 27);
			patientDAO.addPatient(john);
			logger.info("Added "+john.getName()+" with id "+john.getId());
			
			//Creating and adding Patient Mitul Shah
			Patient mitul = patientFactory.createPatient(87654321L, "Mitul Shah",setDate("10/15/1980"), 37);
			patientDAO.addPatient(mitul);			
			logger.info("Added "+mitul.getName()+" with id "+mitul.getId());
			
			//Creating and adding Patient Malav Patel
			Patient malav = patientFactory.createPatient(15935785L, "Malav Patel",setDate("03/07/1993"), 24);
			patientDAO.addPatient(malav);			
			logger.info("Added "+malav.getName()+" with id "+malav.getId());
			
			//Adding patient through patientdto method
			PatientDtoFactory patientdtoFactory = new PatientDtoFactory();
			PatientDto patientdto = patientdtoFactory.createPatientDto();
			patientdto.setName("Robin");
			patientdto.setPatientId(1245);
			patientdto.setDob(new SimpleDateFormat("yyy-MM-dd").parse("1990-05-15"));
			patientdto.setAge(27);
			logger.info("Age added: " + patientdto.getAge());
			
			long patientId = patientService.addPatient(patientdto);
			String patientdtoName = patientService.getPatient(patientId).getName();
			long patientdtoId = patientService.getPatient(patientId).getId();
			logger.info("Added patient " + patientdtoName + " with id " + patientdtoId);
			
			//Adding provider through providerdto method
			ProviderDtoFactory proFac = new ProviderDtoFactory();
			ProviderDto Ted = proFac.createProviderDto();
			Ted.setName("Ted Mosby");
			Ted.setNpi(1215);
			Ted.setSpecialization("drug-Treatment");
			logger.info("Specialization added: " + Ted.getSpecialization());

			long proId = providerService.addProvider(Ted);
			String TedName = providerService.getProvider(proId).getName();
			long TedId = providerService.getProvider(proId).getId();
			logger.info("Added provider " + TedName + " with id " + TedId);
			
			//Creating and adding Provider Rohan Mehta
			Provider rohan = providerFactory.createProvider(989898, "Rohan Mehta", "surgery");
			providerDAO.addProvider(rohan);			
			logger.info("Added Provider: "+rohan.getName()+" with npi "+rohan.getNpi());

			//Creating and adding Provider Dilip Mehta
			Provider dilip = providerFactory.createProvider(989999, "Dilip Mehta", "drug-Treatment");
			providerDAO.addProvider(dilip);			
			logger.info("Added Provider: "+dilip.getName()+" with npi "+dilip.getNpi());
			
			//Creating and setting drugtreatment by provider rohan
			Treatment drugTreatment = treatmentFactory.createDrugTreatment("ICU", "Vicks", 12);
			drugTreatment.setProvider(dilip);
			rohan.addTreatment(drugTreatment);
			logger.info("Added treatment " + drugTreatment.getTreatmentType() + "  with id " + drugTreatment.getTreatmentid());

			
			//Creating and setting surgery treatment by provider dilip
			Treatment surgeryTreatment = treatmentFactory.createSurgery("surgery", setDate("01/10/2014"));
			surgeryTreatment.setProvider(rohan);
			dilip.addTreatment(surgeryTreatment);
			logger.info("Added treatment " + surgeryTreatment.getTreatmentType() + "  with id " + surgeryTreatment.getTreatmentid());
			
			logger.info("1 Ted id: " + TedId);
			//Adding treatment through treatmentdto method
			TreatmentDtoFactory treatFac = new TreatmentDtoFactory();
			TreatmentDto treatDto = treatFac.createDrugTreatmentDto();
			DrugTreatmentType drugTreatmentT = new DrugTreatmentType();
			treatDto.setDiagnosis("Stone");
			treatDto.setPatient(patientdto.getId());
			treatDto.setProvider(TedId); 
			treatDto.setDrugTreatment(drugTreatmentT);
			treatDto.setId(655);
			drugTreatmentT.setName("pain");
			drugTreatmentT.setDosage(25);
			logger.info("2 Ted id: " + TedId);
			logger.info("Treatment ID: " + treatDto.getId());
		
			
			//Calling addtreatmentforpat function from providerservice.
			long trmtId = treatDto.getId();
			logger.info("treatment id before function call: " + trmtId);
			try {
				logger.info("patientId: " + patientId);
				logger.info("providerId: " + TedId);
				trmtId = providerService.addTreatmentForPat(treatDto, patientId, TedId);
			} catch (JMSException e) {
				logger.severe("Jms error");
				e.printStackTrace();
			}

			logger.info("patient's Id:" + patientId + " and provider's NPI:" + Ted.getNpi()
					+ " add drug treatment with id:" + trmtId);

			String diag = patientService.getTreatment(patientId, trmtId).getDiagnosis();
			logger.info("new treatment diagnosis is " + diag);
			
			//Getting John Doe's information through getPatient() method
			Patient p = patientDAO.getPatient(john.getId());			
			logger.info("Added Patient "+p.getName()+" with id "+p.getId() +" & Patient Id: "+p.getPatientId() +"Patient DOB: "+ p.getBirthDate()+" & patient age: "+ p.getAge());
			
			//Getting John Doe's information through getPatientByPatientId() method
			Patient p1 = patientDAO.getPatientByPatientId(mitul.getPatientId());			
			logger.info("Added Patientby ID: "+p1.getName()+" with id "+p1.getId());
	
			//Getting Rohan Mehta's information through getProviderById() method
			Provider pro2 = providerDAO.getProviderById(rohan.getId());
			logger.info("By ID : Added Provider: "+pro2.getName()+" with npi "+pro2.getNpi()+ " with id: "+ pro2.getId());
			
			//Getting Rohan Mehta's information through getProviderByNPI() method
			Provider pro3 = providerDAO.getProviderByNPI(rohan.getNpi());
			logger.info("BY NPI : Added Provider: "+pro3.getName()+" with npi "+pro3.getNpi()+ " with id: "+ pro3.getId());		
			
		} catch (PatientExn  e) {

			// logger.log(Level.SEVERE, "Failed to add patient record.", e);
			IllegalStateException ex = new IllegalStateException("Failed to add patient record." + e.getMessage());
			ex.initCause(e);
			throw ex;
			
		} 
		
		catch( ProviderExn pro){
			IllegalStateException ex = new IllegalStateException("provider Exception :"  + pro.getMessage());
			ex.initCause(pro);
			throw ex;
		}		
		
		catch (ProviderServiceExn e) {
				logger.severe(e.toString());
				logger.severe(e.getCause().toString());
				throw new IllegalStateException("Failed to add provider");
			} catch (PatientServiceExn e) {
				logger.severe(e.toString());
				e.printStackTrace();
				throw new IllegalStateException("Failed to add patient");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
	}
	
	private Date setDate(String stringDate) {
		Date date = null;
		try {
			String pattern = "MM/dd/yyyy";
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			date = format.parse(stringDate);
		} catch (Exception e) {
			IllegalStateException ex = new IllegalStateException("Error while setting state");
			ex.initCause(e);
			throw ex;
		}
		return date;
	}	
}
