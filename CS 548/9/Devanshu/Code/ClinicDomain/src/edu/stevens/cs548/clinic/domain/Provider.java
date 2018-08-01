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
 * Entity implementation class for Entity: Provider
 *
 */


@NamedQueries({
	@NamedQuery(
			name = "SearchProviderBynpi", 
			query = "select p from Provider p where p.npi = :pid"
			),
	@NamedQuery(
			name = "CountProviderBynpi", 
			query = "select count(p) from Provider p where p.npi = :pid"
			),
	@NamedQuery(
			name = "RemoveAllProvider", 
			query = "delete from Provider p"
			) 
})

@Entity
@Table(name = "Provider")
public class Provider implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	
	private long npi; 
	
	private String name;
	
	private String specialization;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	
	public long getNpi() {
		return npi;
	}

	public void setNpi(long npi) {
		this.npi = npi;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();		
	}

	@OneToMany(cascade = REMOVE, mappedBy = "providers", orphanRemoval = true)
	@OrderBy
	private List<Treatment> treatments;

	public List<Treatment> getTreatment() {
		return treatments;
	}

	public void setTreatment(List<Treatment> treatment) {
		this.treatments = treatment;
	}


	@Transient
	private ITreatmentDAO treatmentDAO;
	
	public void setTreatmentDAO (ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}
	
	public long addTreatment (Treatment t) {
		// Persist treatment and set forward and backward links
		
			this.treatmentDAO.addTreatment(t);
			this.getTreatment().add(t);
			if (t.getProvider() != this) {
				t.setProvider(this);
			}
			//patient.addTreatment(t);
			
		
		return t.getTreatmentid();
	}

	// 8a
	public List<Long> getTreatmentIds() {
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : this.getTreatment()) {
			tids.add(t.getTreatmentid());
		}
		return tids;
	}

	public void visitTreatments(ITreatmentVisitor visitor) {
		for (Treatment t : this.getTreatment()) {
			t.visit(visitor);
		}
	}

	// 8b
	public <T> T exportTreatment(long tid, ITreatmentExporter<T> visitor) throws TreatmentExn {
		// Export a treatment without violated Aggregate pattern
		// Check that the exported treatment is a treatment for this patient.
		Treatment t = treatmentDAO.getTreatment(tid);
		if (t.getProvider() != this) {
			throw new TreatmentExn("Inappropriate treatment access: patient = " + id + ", treatment = " + tid);
		}
		return t.export(visitor);
	}

}