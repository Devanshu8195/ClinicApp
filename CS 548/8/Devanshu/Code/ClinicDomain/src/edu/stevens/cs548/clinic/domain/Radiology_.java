package edu.stevens.cs548.clinic.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-11-25T08:20:30.237-0500")
@StaticMetamodel(Radiology.class)
public class Radiology_ extends Treatment_ {
	public static volatile SingularAttribute<Radiology, Long> rid;
	public static volatile ListAttribute<Radiology, RadiologyDate> date;
}