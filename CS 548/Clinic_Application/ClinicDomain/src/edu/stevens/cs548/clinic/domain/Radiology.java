package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
/**
 * Entity implementation class for Entity: Radiology
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = "RemoveAllRadiology", query = "delete from Radiology r") })
@Table(name = "Radiology")
@DiscriminatorValue("RA")
public class Radiology extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;



	@ElementCollection
	@Temporal(TemporalType.DATE)
	@CollectionTable(name="Radiology_date", joinColumns = @JoinColumn(name = "RadiologyTreatmentid_fk")) 
	
	private List<Date> dates;
	
	public List<Date> getDates() {
		return dates;
	}

	public void setDates(List<Date> dates) {
		this.dates = dates;
	}

	public Radiology() {
		super();
		this.setTreatmentType("RA");
		dates = new ArrayList<Date>();
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		// TODO Auto-generated method stub
	
		return visitor.exportRadiology(this.getTreatmentid(), this.getDiagnosis(), this.getDates());
		   		  
	}

	@Override
	public void visit(ITreatmentVisitor visitor) {
		// TODO Auto-generated method stub
		visitor.visitRadiology(this.getTreatmentid(), this.getDiagnosis(), this.getDates(), this.getProvider());
	}
   
}
