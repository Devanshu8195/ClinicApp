ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_provider_fk
ALTER TABLE TREATMENT DROP CONSTRAINT FK_TREATMENT_patient_fk
ALTER TABLE DRUG_TREATMENT DROP CONSTRAINT FK_DRUG_TREATMENT_ID
ALTER TABLE RADIOLOGY DROP CONSTRAINT FK_RADIOLOGY_ID
ALTER TABLE SURGERY DROP CONSTRAINT FK_SURGERY_ID
ALTER TABLE Radiology_DATE DROP CONSTRAINT FK_Radiology_DATE_Radiology_RID
DROP TABLE PATIENT
DROP TABLE TREATMENT
DROP TABLE DRUG_TREATMENT
DROP TABLE PROVIDER
DROP TABLE RADIOLOGY
DROP TABLE SURGERY
DROP TABLE Radiology_DATE
DELETE FROM SEQUENCE WHERE SEQ_NAME = 'SEQ_GEN'
