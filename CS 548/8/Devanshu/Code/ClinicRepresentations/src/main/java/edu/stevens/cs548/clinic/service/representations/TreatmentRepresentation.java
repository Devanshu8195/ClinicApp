package edu.stevens.cs548.clinic.service.representations;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.web.rest.data.ObjectFactory;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.web.rest.data.TreatmentType;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
//import edu.stevens.cs548.clinic.service.web.rest.data.dap.LinkType;

@XmlRootElement
public class TreatmentRepresentation extends TreatmentType {
	
	private ObjectFactory repFactory = new ObjectFactory();
	
	public LinkType getLinkPatient() {
		return this.getPatient();
	}
	
	public LinkType getLinkProvider() {
		return this.getProvider();
	}
	
	public static LinkType getTreatmentLink(long tid, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		UriBuilder ubTreatment = ub.clone().path("{tid}");
		String treatmentURI = ubTreatment.build(Long.toString(tid)).toString();
	
		LinkType link = new LinkType();
		link.setUrl(treatmentURI);
		link.setRelation(Representation.RELATION_TREATMENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	private TreatmentDtoFactory treatmentDtoFactory;
	
	public TreatmentRepresentation() {
		super();
		treatmentDtoFactory = new TreatmentDtoFactory();
	}
	
	public TreatmentRepresentation (TreatmentDto dto, UriInfo uriInfo) {
		this();
		this.id = getTreatmentLink(dto.getId(), uriInfo);
		this.patient =  PatientRepresentation.getPatientLink(dto.getPatient(), uriInfo);
		/*
		 * TODO: Need to fill in provider information.
		 */
		this.provider = ProviderRepresentation.getProviderLink(dto.getProvider(), uriInfo);

		
		this.diagnosis = dto.getDiagnosis();
		
		if (dto.getDrugTreatment() != null) {
			/*
			 * TODO finish this
			 */
			edu.stevens.cs548.clinic.service.web.rest.data.DrugTreatmentType dtrep = repFactory.createDrugTreatmentType();
			dtrep.setDosage(dto.getDrugTreatment().getDosage());
			dtrep.setDrugname(dto.getDrugTreatment().getName());
			this.drugTreatment = (DrugTreatmentTypeRepresentation) dtrep;
		} else if (dto.getSurgery() != null) {
			/*
			 * TODO finish this
			 */
			edu.stevens.cs548.clinic.service.web.rest.data.SurgeryType surRep = repFactory.createSurgeryType();
			surRep.setDate(dto.getSurgery().getDate());
			this.surgery = (SurgeryTypeRepresentation) surRep;
		} else if (dto.getRadiology() != null) {
			/*
			 * TODO finish this
			 */
			edu.stevens.cs548.clinic.service.web.rest.data.RadiologyType radiologyRep = repFactory.createRadiologyType();
			radiologyRep.getDate().addAll(dto.getRadiology().getDate());
			this.radiology = (RadiologyTypeRepresentation) radiologyRep;
		}
	}

	public TreatmentDto getTreatment() {
		TreatmentDto m = null;
		if (this.getDrugTreatment() != null) {
			m = treatmentDtoFactory.createTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			/*
			 * TODO finish this
			 */
			DrugTreatmentType d = treatmentDtoFactory.createDrugTreatmentDto();
			d.setDosage(drugTreatment.getDosage());
			d.setName(drugTreatment.getDrugname());
			m.setDrugTreatment(d);
		} else if (this.getSurgery() != null) {
			/*
			 * TODO finish this
			 */
			m = treatmentDtoFactory.createTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
			
			SurgeryType s = treatmentDtoFactory.createsurgeryTreatmentDto();
			s.setDate(surgery.getDate());
			m.setSurgery(s);
		} else if (this.getRadiology() != null) {
			/*
			 * TODO finish this
			 */
			m = treatmentDtoFactory.createTreatmentDto();
			m.setId(Representation.getId(id));
			m.setPatient(Representation.getId(patient));
			m.setProvider(Representation.getId(provider));
			m.setDiagnosis(diagnosis);
		
			RadiologyType r = treatmentDtoFactory.createRadiologyTreatmentDto();
			r.getDate().addAll(radiology.getDate());
		}
		return m;
	}

	
}
