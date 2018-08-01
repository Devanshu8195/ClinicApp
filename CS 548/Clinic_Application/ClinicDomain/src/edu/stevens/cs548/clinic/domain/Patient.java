package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import static javax.persistence.CascadeType.REMOVE;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

/**
 * Entity implementation class for Entity: Patient
 *
 */
/*
 * TODO
 */

@NamedQueries({
	@NamedQuery(
		name="SearchPatientByPatientID",
		query="select p from Patient p where p.patientId = :pid"),
	@NamedQuery(
		name="CountPatientByPatientID",
		query="select count(p) from Patient p where p.patientId = :pid"),
	@NamedQuery(
			name="CountPatientByID",
			query="select count(p) from Patient p where p.id = :pid"),
	@NamedQuery(
		name = "RemoveAllPatients", 
		query = "delete from Patient p")
})

/*
 * TODO
 */
@Entity
@Table(name = "Patient")
public class Patient implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// TODO JPA annotations
	@Id @GeneratedValue private long id;
	
	private long patientId;
	
	private String name;
	
	// TODO JPA annotation
	@Temporal(TemporalType.DATE)
	private Date birthDate;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	private int age;
	
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
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}
	
	public long addTreatment (Treatment t) {
		// Persist treatment and set forward and backward links
		
			this.treatmentDAO.addTreatment(t);
			this.getTreatments().add(t);
			if (t.getPatient() != this) {
				t.setPatient(this);
			}
		
		return t.getTreatmentid();
	}
	
	public List<Long> getTreatmentIds() {
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : this.getTreatments()) {
			tids.add(t.getTreatmentid());
		}
		return tids;
	}

	public void visitTreatments(ITreatmentVisitor visitor) {
		for (Treatment t : this.getTreatments()) {
			t.visit(visitor);
		}
	}
	
	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
		// Export a treatment without violated Aggregate pattern
		// Check that the exported treatment is a treatment for this patient.
		Treatment t = treatmentDAO.getTreatment(tid);
		if (t.getPatient() != this) {
			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}

	public Patient() {
		super();
		/*
		 * TODO initialize lists
		 */
		treatments = new ArrayList<Treatment>();
	}

	public long addDrugTreatment(String diagnosis, String drug, float dosage, Provider provider) {
		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDosage(dosage);
		treatment.setDrug(drug);
		treatment.setProvider(provider);
		long tid = this.addTreatment(treatment);
		return tid;
	}

	public void addSurgery(String diagnosis, Date date, Provider provider) {
		Surgery treatment = new Surgery();
		treatment.setDiagnosis(diagnosis);
		treatment.setDate(date);
		treatment.setProvider(provider);
		this.addTreatment(treatment);
	}

	public void addRadiology(List<Date> dates, Provider provider, String diagnosis) {
		Radiology treatment = new Radiology();
		treatment.setDates(dates);
		treatment.setDiagnosis(diagnosis);
		treatment.setProvider(provider);
		this.addTreatment(treatment);

	}
   
}
