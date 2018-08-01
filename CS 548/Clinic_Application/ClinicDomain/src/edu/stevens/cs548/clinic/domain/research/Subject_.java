package edu.stevens.cs548.clinic.domain.research;

import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-12-02T17:24:58.533-0500")
@StaticMetamodel(Subject.class)
public class Subject_ {
	public static volatile CollectionAttribute<Subject, DrugTreatmentRecord> treatments;
	public static volatile SingularAttribute<Subject, Long> id;
	public static volatile SingularAttribute<Subject, Long> subjectId;
}
