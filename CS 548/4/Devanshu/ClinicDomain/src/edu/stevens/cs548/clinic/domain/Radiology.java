package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.*;

/**
 * Entity implementation class for Entity: Radiology
 *
 */
@Entity

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
	

	private List<Calendar> date;

	public List<Calendar> getDate() {
		return date;
	}

	public void setDate(List<Calendar> date) {
		this.date = date;
	}

	
	public Radiology() {
		super();
	}
   
}
