package edu.stevens.cs548.clinic.domain;


public interface ITreatmentFactory {
	
	public Treatment createDrugTreatment (String diagnosis, String drug, float dosage);
	
	/*
	 * TODO add methods for Radiology, Surgery
	 */

}
