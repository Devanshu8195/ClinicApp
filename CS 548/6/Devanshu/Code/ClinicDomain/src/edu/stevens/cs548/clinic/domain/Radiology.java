package edu.stevens.cs548.clinic.domain;

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
@Table(name = "Radiology")
@DiscriminatorValue("RA")
public class Radiology extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	private long rid;
	
	public long getRid() {
		return rid;
	}

	public void setRid(long rid) {
		this.rid = rid;
	}

	@ElementCollection
	@Temporal(TemporalType.DATE)
	@CollectionTable(name="Radiology_date", joinColumns = @JoinColumn(name = "RadiologyTreatmentid_fk")) 
	private List<RadiologyDate> date;

	public List<RadiologyDate> getDate() {
		return date;
	}

	public void setDate(List<RadiologyDate> date) {
		this.date = date;
	}

	public Radiology() {
		super();
		this.setTreatmentType("RA");
		date = new ArrayList<RadiologyDate>();
	}

	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		// TODO Auto-generated method stub
		List<Date> rdList = new ArrayList<Date>();
		for(RadiologyDate rd : this.date){
			rdList.add(rd.getDate());
		}
		return visitor.exportRadiology(this.getTreatmentid(), this.getDiagnosis(), rdList); 
		   		  
	}
   
}
