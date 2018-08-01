package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.*;

/**
 * Entity implementation class for Entity: Surgery
 *
 */
@Entity
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
	}
   
}
