ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_provider_fk;
ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_patient_fk;
ALTER TABLE DrugTreatment DROP CONSTRAINT FK_DRUG_TREATMENT_TREATMENTID;
ALTER TABLE RADIOLOGY DROP CONSTRAINT FK_RADIOLOGY_TREATMENTID;
ALTER TABLE SURGERY DROP CONSTRAINT FK_SURGERY_TREATMENTID;
ALTER TABLE Radiology_DATE DROP CONSTRAINT FK_Radiology_date_RadiologyTreatmentid_fk;
DROP TABLE PATIENT CASCADE;
DROP TABLE TREATMENT CASCADE;
DROP TABLE DrugTreatment CASCADE;
DROP TABLE PROVIDER CASCADE;
DROP TABLE RADIOLOGY CASCADE;
DROP TABLE SURGERY CASCADE;
DROP TABLE Radiology_DATE CASCADE;
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN';
