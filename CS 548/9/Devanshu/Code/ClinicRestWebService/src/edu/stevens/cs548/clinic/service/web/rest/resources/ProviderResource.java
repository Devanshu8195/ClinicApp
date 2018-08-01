package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.stevens.cs548.clinic.service.dto.treatment.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.provider.ProviderDto;
import edu.stevens.cs548.clinic.service.dto.util.ProviderDtoFactory;
import edu.stevens.cs548.clinic.service.dto.treatment.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.treatment.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.treatment.TreatmentDto;
import edu.stevens.cs548.clinic.service.dto.util.TreatmentDtoFactory;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IPatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceLocal;
import edu.stevens.cs548.clinic.service.representations.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.representations.Representation;
import edu.stevens.cs548.clinic.service.representations.TreatmentRepresentation;

@Path("/provider")
@RequestScoped
public class ProviderResource {
    @SuppressWarnings("unused")
    final static Logger logger = Logger.getLogger(ProviderResource.class.getCanonicalName());
	
	public class RecordNotFound extends WebApplicationException {
		private static final long serialVersionUID = 1L;
		public RecordNotFound(String message) {
	         super(Response.status(Response.Status.NOT_FOUND)
	             .entity(message).type(Representation.MEDIA_TYPE).build());
	     }
	}
	public class RecordNotCreated extends WebApplicationException{
		private static final long serialVersionUID = 1L;
		public RecordNotCreated(String message) {
	         super(Response.status(Response.Status.BAD_REQUEST)
	             .entity(message).type(Representation.MEDIA_TYPE).build());
		}
	}
	
    @Context
    private UriInfo uriInfo;

	private ProviderDtoFactory providerDtoFactory;
	private TreatmentDtoFactory treatmentDtoFactory;
	
    /**
     * Default constructor. 
     */
    public ProviderResource() {
        // TODO Auto-generated constructor stub
		providerDtoFactory = new ProviderDtoFactory();
		treatmentDtoFactory = new TreatmentDtoFactory();
    }
    
	@Inject
	private IProviderServiceLocal providerService;

	@GET
	@Path("site")
	@Produces("text/plain")
	public String getSiteInfo() {
		return providerService.siteInfo();
	}
	
	@POST
	@Consumes("application/xml")
	public Response addProvider(ProviderRepresentation providerRep) {
		try {
			ProviderDto dto = providerDtoFactory.createProviderDto();
			dto.setId(providerRep.getProviderId());
			dto.setName(providerRep.getName());
			dto.setSpecialization(providerRep.getSpecialization());
			long id = providerService.addProvider(dto);
			UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(id));
			return Response.status(201).build();
		} catch (ProviderServiceExn e) {
			throw new RecordNotCreated("Unable to add Provider");
		}
	}
	
	@POST
	@Path("/id/treatments")
	@Consumes("application/xml")
	public Response addTreatment(@HeaderParam("id") String patientId, TreatmentRepresentation treatmentRep)
			throws NumberFormatException, JMSException {
		try {
			TreatmentDto dto = null;
			if (treatmentRep.getDrugTreatment() != null) {
				dto = treatmentDtoFactory.createDrugTreatmentDto();

				dto.setPatient(Representation.getId(treatmentRep.getLinkPatient()));
				logger.info("patient id: =" + dto.getPatient());
				dto.setProvider(Representation.getId(treatmentRep.getLinkProvider()));
				logger.info("provider id: =" + dto.getProvider());
				dto.setDiagnosis(treatmentRep.getDiagnosis());
				dto.getDrugTreatment().setName(treatmentRep.getDrugTreatment().getDrugname());
				dto.getDrugTreatment().setDosage(treatmentRep.getDrugTreatment().getDosage());
				long id = providerService.addTreatmentForPat(dto, dto.getPatient(), dto.getProvider());
				logger.info("treatment id: =" + id);
				UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
				URI url = ub.build(Long.toString(id));
				return Response.created(url).build();
			} else if (treatmentRep.getSurgery() != null) {
				dto = treatmentDtoFactory.createSurgeryDto();
				dto.setPatient(Representation.getId(treatmentRep.getLinkPatient()));
				dto.setProvider(Representation.getId(treatmentRep.getLinkProvider()));
				dto.setDiagnosis(treatmentRep.getDiagnosis());
				dto.getSurgery().setDate(treatmentRep.getSurgery().getDate());
				long id = providerService.addTreatmentForPat(dto, dto.getPatient(), dto.getProvider());
				UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
				URI url = ub.build(Long.toString(id));
				return Response.created(url).build();
			} else if (treatmentRep.getRadiology() != null) {
				dto = treatmentDtoFactory.createRadiologyDto();
				dto.setPatient(Representation.getId(treatmentRep.getLinkPatient()));
				dto.setProvider(Representation.getId(treatmentRep.getLinkProvider()));
				dto.setDiagnosis(treatmentRep.getDiagnosis());
				dto.getRadiology().setDate(treatmentRep.getRadiology().getDate());
				long id = providerService.addTreatmentForPat(dto, dto.getPatient(), dto.getProvider());
				UriBuilder ub = uriInfo.getAbsolutePathBuilder().path("{id}");
				URI url = ub.build(Long.toString(id));
				return Response.created(url).build();
			}
		} catch (PatientNotFoundExn e) {
			throw new WebApplicationException(e.toString());
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException(e.toString());
		}
		return null;
	}
	
    /**
     * Retrieves representation of an instance of ProviderResource
     * @return an instance of ProviderRepresentation
     */
    @GET
	@Path("{id}")
	@Produces("application/xml")
	public ProviderRepresentation getProvider(@PathParam("id") String id) {
		try {
			long key = Long.parseLong(id);
			ProviderDto providerDTO = providerService.getProvider(key);
			ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO, uriInfo);
			return providerRep;
		} catch (ProviderServiceExn e) {
			throw new RecordNotFound("No Provider Found");
		}
	}

	@GET
	@Path("byNPI")
	@Produces("application/xml")
	public ProviderRepresentation getProviderByProviderId(@QueryParam("id") String providerId) {
		try {
			long pid = Long.parseLong(providerId);
			ProviderDto providerDTO = providerService.getProviderByNPI(pid);
			ProviderRepresentation providerRep = new ProviderRepresentation(providerDTO, uriInfo);
			return providerRep;
		} catch (ProviderServiceExn e) {
			throw new RecordNotFound("No Provider Found");
		}
	}

	@GET
	@Path("{id}/treatment/{tid}")
	@Produces("application/xml")
	public TreatmentRepresentation getProviderTreatment(@PathParam("id") String id, @PathParam("tid") String tid) {
		try {
			logger.info("Provider RESOURCES: getProvideTreatment: "+tid);
//			long[] tids = new long[tid.split(",").length];
//	    	
//    		for(int i = 0; i < tid.split(",").length; i++) {
//    			tids[i] = Long.parseLong(tid.split(",")[i]);
//    		}
//    		
//    		TreatmentDto[] treatment = providerService.getTreatment(Long.parseLong(id), tids);
//			
//			TreatmentRepresentation[] treatmentRep = new TreatmentRepresentation[treatment.length];
//    		for(int i=0; i < treatment.length; i++){
//    			treatmentRep[i] = new TreatmentRepresentation(treatment[i], uriInfo);
//    		}
			
			TreatmentDto treatment = providerService.getTreatment(Long.parseLong(id), Long.parseLong(tid));
			logger.info("Provider RESOURCES: Treatment object generated: "+tid);
			TreatmentRepresentation treatmentRep = new TreatmentRepresentation(treatment, uriInfo);
			logger.info("Provider RESOURCES: treatment REpresentation generated: "+tid);
			return treatmentRep;
		} catch (ProviderServiceExn e) {
			throw new RecordNotFound("No Treatment record Found");
		}
	}

}