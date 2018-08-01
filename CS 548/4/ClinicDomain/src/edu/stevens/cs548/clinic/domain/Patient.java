package edu.stevens.cs548.clinic.domain;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.*;
import java.util.*;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Patient
 *
 */
/*
 * TODO
 */
/*@NamedQueries({
	@NamedQuery(
		name="SearchPatientByPatientID",
		query="select p from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name="CountPatientByPatientID",
		query="select count(p) from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p")
})*/

/* 
 * TODO
 */
@Entity
@Table(name = "PATIENT")
public class Patient implements Serializable {

	
	private static final long serialVersionUID = 1L;

	// TODO JPA annotations
		@Id
		@GeneratedValue
		private long id;
		
		private long patientId;
		
		private String name;
		
		// TODO JPA annotation
		@Temporal(TemporalType.DATE)
		private Date birthDate;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public long getPatientId() {
			return patientId;
		}

		public void setPatientId(long patientId) {
			this.patientId = patientId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Date getBirthDate() {
			return birthDate;
		}

		public void setBirthDate(Date birthDate) {
			this.birthDate = birthDate;
		}

		// TODO JPA annotations (propagate deletion of patient to treatments)

		@OneToMany(cascade = REMOVE, mappedBy = "patient")
		@OrderBy
		private List<Treatment> treatments;

		protected List<Treatment> getTreatments() {
			return treatments;
		}

		protected void setTreatments(List<Treatment> treatments) {
			this.treatments = treatments;
		}
		
		/*
		 * Addition and deletion of treatments should be done in the provider aggregate.
		 */

	
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}
   
}
