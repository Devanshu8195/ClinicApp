package edu.stevens.cs548.clinic.service.dto.util;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.service.dto.patient.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.patient.PatientDto;


public class PatientDtoFactory {
	
	ObjectFactory factory;
	
	private Logger logger = Logger.getLogger(PatientDtoFactory.class.getCanonicalName());
	
	public PatientDtoFactory() {
		factory = new ObjectFactory();
	}

	public PatientDto createPatientDto () {
		return factory.createPatientDto();
	}
	
	public PatientDto createPatientDto (Patient p) {
		logger.info("Entered Patientdtofac createpatientdto method");
		PatientDto d = factory.createPatientDto();
		/*
		 * TODO: Initialize the fields of the DTO.
		 */
		logger.info("Patientdto factory object creation");
		d.setId(p.getId());
		d.setDob(p.getBirthDate());
		d.setName(p.getName());
		d.setPatientId(p.getPatientId());
		return d;
	}
}
