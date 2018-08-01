package edu.stevens.cs548.clinic.service.ejb;

import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.stevens.cs548.clinic.domain.PatientDAO;

/**
 * Session Bean implementation class PatientProducer
 */
@Stateless
@LocalBean
public class ClinicDomainProducer {

    /**
     * Default constructor. 
     */
    public ClinicDomainProducer() {
    }
    
    // TODO inject the persistence context (do NOT use @Inject)
    @PersistenceContext(unitName = "ClinicDomain")
    EntityManager em;
    
    private Logger logger = Logger.getLogger(ClinicDomainProducer.class.getCanonicalName());
    
    @Produces @ClinicDomain
    public EntityManager clinicDomainProducer() {
    	logger.info("Entered clinicdomain producer, producing entitymanager.");
    	logger.info("em value: " +em);
    	return em;
    }
      
    
    public void clinicDomainDispose(@Disposes @ClinicDomain EntityManager em) {
    }
}
