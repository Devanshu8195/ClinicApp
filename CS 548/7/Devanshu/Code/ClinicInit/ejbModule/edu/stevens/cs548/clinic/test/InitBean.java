package edu.stevens.cs548.clinic.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO;
import edu.stevens.cs548.clinic.domain.ITreatmentExporter;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.TreatmentDAO;
import edu.stevens.cs548.clinic.domain.TreatmentFactory;
import edu.stevens.cs548.clinic.service.ejb.ClinicDomain;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceLocal;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Radiology;
import edu.stevens.cs548.clinic.domain.Treatment;

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
			Patient pandu = patientFactory.createPatient(12332112L, "Ishan Pandya", setDate("11/27/1992"), 24);
			patientDAO.addPatient(pandu);
			logger.info("Added " + pandu.getName() + " with id " + pandu.getId());
			
			//Creating and Adding Patient John Doe
			Patient john = patientFactory.createPatient(12345678L, "John Doe", setDate("11/26/1990"), 26);
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
			
			//Creating and adding Provider Rohan Mehta
			Provider rohan = providerFactory.createProvider(989898, "Rohan Mehta", "radiology");
			providerDAO.addProvider(rohan);			
			logger.info("Added Provider: "+rohan.getName()+" with npi "+rohan.getNpi());

			//Creating and adding Provider Dilip Mehta
			Provider dilip = providerFactory.createProvider(989999, "Dilip Mehta", "Drug-Treatment");
			providerDAO.addProvider(dilip);			
			logger.info("Added Provider: "+dilip.getName()+" with npi "+dilip.getNpi());
			
			//Adding Drug Treatment
			long drugTreatmentid = dilip.addDrugTreatment("Asthama", "Epitomic", 7, malav);
			logger.info("Provider : " + rohan.getName() + " supervising treatment with id " + drugTreatmentid + " for patient " + malav.getName());

			//Adding Surgery Treatment
			long surgeryid = rohan.addSurgeryTreatment(setDate("05/05/2010"), "appendics", mitul);
			
			//Adding Radiology Treatment 
			List<Date> radiologyDates = new ArrayList<Date>();
			for(int i =1;i<4;i++){
				radiologyDates.add(setDate("10/"+i+"/2014"));
			}
			long rid = rohan.addRadiologyTreatment(radiologyDates, "Cancer", malav);
			logger.info("Provider : " + rohan.getName() + " supervising treatment with id " + rid + " for patient "	+ malav.getName());

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
