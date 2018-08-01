package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Surgery
 *
 */
@Entity
@Table(name = "Surgery")
@DiscriminatorValue("SU")
public class Surgery extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date date;
	
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Surgery() {
		super();
		this.setTreatmentType("SU");
	}


	@Override
	public <T> T export(ITreatmentExporter<T> visitor) {
		// TODO Auto-generated method stub
		return visitor.exportSurgery(this.getTreatmentid(), this.getDiagnosis(), this.getDate());
	}
   
}
