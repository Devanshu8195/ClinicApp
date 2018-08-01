package edu.stevens.cs548.clinic.domain.research;

import java.io.Serializable;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Entity implementation class for Entity: Subject
 *
 */
@Entity

public class Subject implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@OneToMany(mappedBy = "subject")
	private Collection<DrugTreatmentRecord> treatments;
	@Id
	@GeneratedValue
	private long id;
	private long subjectId;

	public Collection<DrugTreatmentRecord> getTreatments() {
		return treatments;
	}

	public void setTreatments(Collection<DrugTreatmentRecord> treatments) {
		this.treatments = treatments;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public Subject() {
		super();
		treatments = new ArrayList<DrugTreatmentRecord>();
	}
   
}
