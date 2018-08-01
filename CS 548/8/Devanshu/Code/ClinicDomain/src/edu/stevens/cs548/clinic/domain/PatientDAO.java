package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;


public class PatientDAO implements IPatientDAO {

	public EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	public PatientDAO(EntityManager em) {
		this.em = em;
		this.treatmentDAO = new TreatmentDAO(em);
	}

	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger(PatientDAO.class.getCanonicalName());

	@Override
	public long addPatient(Patient patient) throws PatientExn {
		long pid = patient.getPatientId();
		Query query = em.createNamedQuery("CountPatientByPatientID").setParameter("pid", pid);
		Long numExisting = (Long) query.getSingleResult();
		if (numExisting < 1) {
			// TODO add to database (and sync with database to generate primary key)
			// Don't forget to initialize the patient aggregate with a treatment DAO
			em.flush();
			em.persist(patient);
			patient.setTreatmentDAO(this.treatmentDAO);	
		} else {
			throw new PatientExn("Insertion: Patient with patient id (" + pid + ") already exists.");
		}
		return patient.getId();
	}

	@Override
	public Patient getPatient(long id) throws PatientExn {
		// TODO retrieve patient using primary key
		logger.info("PatientDAO getpatient method");
		Query query = em.createNamedQuery("CountPatientByID").setParameter("pid", id);
		Long numExisting = (Long) query.getSingleResult();
		logger.info("value of numexisting" + numExisting);
		if(numExisting < 1 || numExisting > 1){
			throw new PatientExn ("No patient record for patient id:"+ id);
		}
		else{
			Patient	patient	=	em.find(Patient.class,	id);
			if(patient	!=	null) {
				patient.setTreatmentDAO(this.treatmentDAO);
				logger.info("Patient treatments: " + patient.getTreatments());
			} else {
				throw new PatientExn("No patient Found with the provided Id");					
 			}
			logger.info("Patient value: " + patient);
			return	patient;
 		}
	}

	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		// TODO retrieve patient using query on patient id (secondary key)
		TypedQuery<Patient> query =  em.createNamedQuery("SearchPatientByPatientID",Patient.class).setParameter("pid", pid);
		List<Patient> patientL = query.getResultList();
			if (patientL.isEmpty() || patientL.size() > 1) {
			throw new PatientExn("Ambiguious results: "+patientL.size());
		} else {
			Patient patient = patientL.get(0);
			patient.setTreatmentDAO(this.treatmentDAO);
			return patient;
		}
	
	}
	
	@Override
	public void deletePatients() {
		em.createQuery("delete from Treatment t").executeUpdate();
		Query update = em.createNamedQuery("RemoveAllPatients");
		update.executeUpdate();
	}

}
