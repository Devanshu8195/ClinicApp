CREATE TABLE PATIENT (ID BIGINT NOT NULL, AGE INTEGER, BIRTHDATE DATE, NAME VARCHAR(255), PATIENTID BIGINT, PRIMARY KEY (ID));
CREATE TABLE TREATMENT (TREATMENTID BIGINT NOT NULL, TREATMENT_TYPE VARCHAR(31), DIAGNOSIS VARCHAR(255), patient_fk BIGINT, provider_fk BIGINT, PRIMARY KEY (TREATMENTID));
CREATE TABLE DrugTreatment (TREATMENTID BIGINT NOT NULL, DOSAGE FLOAT, DRUG VARCHAR(255), PRIMARY KEY (TREATMENTID));
CREATE TABLE PROVIDER (ID BIGINT NOT NULL, NAME VARCHAR(255), NPI BIGINT, SPECIALIZATION VARCHAR(255), PRIMARY KEY (ID));
CREATE TABLE RADIOLOGY (TREATMENTID BIGINT NOT NULL, RID BIGINT, PRIMARY KEY (TREATMENTID));
CREATE TABLE SURGERY (TREATMENTID BIGINT NOT NULL, DATE DATE, PRIMARY KEY (TREATMENTID));
CREATE TABLE Radiology_DATE (RadiologyTreatmentid_fk BIGINT, DATE VARCHAR(255));
ALTER TABLE TREATMENT ADD CONSTRAINT FK_TREATMENT_provider_fk FOREIGN KEY (provider_fk) REFERENCES PROVIDER (ID);
ALTER TABLE TREATMENT ADD CONSTRAINT FK_TREATMENT_patient_fk FOREIGN KEY (patient_fk) REFERENCES PATIENT (ID);
ALTER TABLE DrugTreatment ADD CONSTRAINT FK_DRUG_TREATMENT_TREATMENTID FOREIGN KEY (TREATMENTID) REFERENCES TREATMENT (TREATMENTID);
ALTER TABLE RADIOLOGY ADD CONSTRAINT FK_RADIOLOGY_TREATMENTID FOREIGN KEY (TREATMENTID) REFERENCES TREATMENT (TREATMENTID);
ALTER TABLE SURGERY ADD CONSTRAINT FK_SURGERY_TREATMENTID FOREIGN KEY (TREATMENTID) REFERENCES TREATMENT (TREATMENTID);
ALTER TABLE Radiology_DATE ADD CONSTRAINT FK_Radiology_date_RadiologyTreatmentid_fk FOREIGN KEY (RadiologyTreatmentid_fk) REFERENCES RADIOLOGY (TREATMENTID);
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT DECIMAL(38), PRIMARY KEY (SEQ_NAME));
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0);
