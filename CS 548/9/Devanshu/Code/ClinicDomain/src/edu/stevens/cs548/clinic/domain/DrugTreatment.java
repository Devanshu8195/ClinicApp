package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import javax.persistence.*;
/**
 * Entity implementation class for Entity: DrugTreatment
 * 
 */
// TODO define discriminator column value 
@Entity
@Table(name = "DrugTreatment")
//@MappedSuperclass
@DiscriminatorValue("DT")
public class DrugTreatment extends Treatment implements Serializable {

	private static final long serialVersionUID = 1L;
	
//	@Id
//	@GeneratedValue
	
/*	private long drugTreatmentId;
	
	public long getDrugTreatmentId() {
		return drugTreatmentId;
	}

	public void setDrugTreatmentId(long drugTreatmentId) {
		this.drugTreatmentId = drugTreatmentId;
	}*/

	private String drug;
	private float dosage;

	public String getDrug() {
		return drug;
	}

	public void setDrug(String drug) {
		this.drug = drug;
	}

	public float getDosage() {
		return dosage;
	}

	public void setDosage(float dosage) {
		this.dosage = dosage;
	}

	public void visit(ITreatmentVisitor visitor) {
		visitor.visitDrugTreatment(this.getTreatmentid(), this.getDiagnosis(), this.getDrug(), this.getDosage(),
				this.getProvider());
	}

	public <T> T export(ITreatmentExporter<T> visitor) {
		return visitor.exportDrugTreatment(this.getTreatmentid(), 
								   		   this.getDiagnosis(),
								   		   this.drug, 
								   		   this.dosage);
	}

	public DrugTreatment() {
		super();
		this.setTreatmentType("DT");
	}

}
