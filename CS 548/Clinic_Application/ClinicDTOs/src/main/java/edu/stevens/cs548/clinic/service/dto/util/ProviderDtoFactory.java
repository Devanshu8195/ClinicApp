package edu.stevens.cs548.clinic.service.dto.util;

import java.util.ArrayList;
import java.util.List;

import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.util.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;

public class ProviderDtoFactory {

	ObjectFactory factory;
	
	public ProviderDtoFactory() {
		factory = new ObjectFactory();
	}
	
	public ProviderDto createProviderDto () {
		return factory.createProviderDto();
	}
	
	public ProviderDto createProviderDto (Provider p) {
		ProviderDto d = factory.createProviderDto();
		/*
		 * TODO: Initialize the fields of the DTO.
		 */
		d.setName(p.getName());
		d.setId(p.getId());
		d.setNpi(p.getNpi());
		d.setSpecialization(p.getSpecialization());
		
		return d;
	}

	
}
