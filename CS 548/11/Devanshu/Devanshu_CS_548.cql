/* Devanshu Mehta */


/* Create Keyspace */
CREATE KEYSPACE CS_548
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

/* Use Statement*/
use CS_548;

/* Create Patient */
 create table Patient (
 	id UUID,
 	patientId int primary key,
 	name text,
 	dob date
);

/* Create Provider */ 
create table Provider (
 	id UUID,
 	NPI int primary key,
 	name text,
 	specialization text
);
 
/*Create Treatment type */
create type CS_548.drugTreatment(
 	drug_name text,
 	dosage float
 );

/* Create Surgery */
create type CS_548.Surgery(
 	surgery_date date
 );

/* Create Radiology */
create type CS_548.Radiology(
 	radiology_dates List<date>
);

/* Create Patient Treatment */
create table Patient_Treatment (
 	id UUID,
 	trmtDate date,
 	year bigint,
 	diagnosis text,
 	patientId int,
 	NPI int,
 	drugTretament  frozen<drugTreatment>,
 	Surgery frozen<Surgery>,
 	Radiology frozen<Radiology>,
 	name text,
 	dob date,
 	PRIMARY KEY ((id,patientId),trmtDate)
)WITH CLUSTERING ORDER BY (trmtDate DESC);
 	
/*Create Provider Treatment */ 	
create table Provider_Treatment (
 	id UUID,
 	trmtDate date,
 	year bigint,
 	diagnosis text,
 	patientId int,
 	NPI int,
 	drugTreatment frozen<drugTreatment>,
 	Surgery frozen<Surgery>,
 	Radiology  frozen<Radiology>,
 	name text,
 	specialization text,
 	PRIMARY KEY((id,NPI,year),trmtDate)
 )WITH CLUSTERING ORDER BY (trmtDate DESC);

/* Insert Model Data of Patient */
 insert into CS_548.Patient(id,patientId,name,dob) values (now(),1,'Malav','1992-05-25');
 insert into CS_548.Patient(id,patientId,name,dob) values (now(),2,'Mitul','1990-01-05');
 insert into CS_548.Patient(id,patientId,name,dob) values (now(),3,'Ronit','1994-07-17');

/* Insert Model Data of Provider */ 	
 insert into CS_548.Provider(id,npi,name,specialization) values (now(),1,'Dr.Rohan','Gastro-Oncology');
 insert into CS_548.Provider(id,npi,name,specialization) values (now(),2,'Dr.Dilip','Radiology');
 insert into CS_548.Provider(id,npi,name,specialization) values (now(),3,'Dr.Ted','Physician');
 	
 	
/*Schema Document for Clinic Database */
insert into CS_548.Patient_Treatment(id,trmtDate,year,diagnosis,patientId,npi,drugTretament,radiology,surgery,name,dob) 
values(now(),'2016-12-09',2016,'Alcoholic',1,1,{drug_name:'Acamprosate',dosage:2.0},null,null,'Malav','1992-05-25');
 	
insert into CS_548.Patient_Treatment(id,trmtDate,year,diagnosis,patientId,npi,drugTretament,radiology,surgery,name,dob) 
values(now(),'2016-12-09',2016,'Cancer',2,2,null,{radiology_dates: ['2016-08-07','2016-08-08','2016-08-09']},null,'Mitul','1990-01-05');
 	
insert into CS_548.Patient_Treatment(id,trmtDate,year,diagnosis,patientId,npi,drugTretament,radiology,surgery,name,dob) 
values(now(),'2016-12-09',2016,'Blood Pressure',3,3,null,null,{surgery_date:'2016-09-01'},'Ronit','1994-07-17');

insert into CS_548.Provider_Treatment(id,trmtDate,year,diagnosis,patientId,npi,drugTreatment,radiology,surgery,name,specialization)
values(now(),'2016-12-09',2016,'Alcoholic',1,1,{drug_name:'Acamprosate',dosage:2.0},null,null,'Dr.Rohan','Gastro-oncology');
	
insert into CS_548.Provider_Treatment(id,trmtDate,year,diagnosis,patientId,npi,drugTreatment,radiology,surgery,name,specialization)
values(now(),'2016-12-09',2016,'Cancer',2,2,null,{radiology_dates: ['2016-08-09','2016-08-08','2016-08-07']},null,'Dr.Dilip','Radiology');
	
insert into CS_548.Provider_Treatment(id,trmtDate,year,diagnosis,patientId,npi,drugTreatment,radiology,surgery,name,specialization)
values(now(),'2016-12-09',2016,'Blood Pressure',3,3,null,null,{surgery_date:'2016-09-02'},'Dr.Ted','Physician');

/* Select Statements */
select * from Patient;
select * from Provider;
select * from Patient_Treatment;
select * from Provider_Treatment;