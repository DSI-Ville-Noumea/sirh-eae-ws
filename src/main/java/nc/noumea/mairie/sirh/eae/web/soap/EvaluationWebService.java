package nc.noumea.mairie.sirh.eae.web.soap;
import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@WebService(name = "EvaluationWebService")
@Service("EvaluationWebService")
@Path("/xml2/evaluation")
//@Consumes("application/xml")
@Produces("application/xml")
public class EvaluationWebService {

	@Autowired
	private IEvaluationService evaluationService;
	
	@Autowired
	private DtoSessionRemoverService dtoSessionRemoverService;
	
	@GET
	@Path("eaeIdentification")
	@Transactional(readOnly = true)
	public EaeIdentificationDto getEaeIdentification(@WebParam(name = "idEae") int idEae) {
		Eae eae = Eae.findEae(idEae);
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(eae);
		return dtoSessionRemoverService.removeSessionOf(dto);
	}
	
	@GET
	@Path("eaeAutoEvaluation")
	@Transactional(readOnly = true)
	public EaeAutoEvaluationDto getEaeAutoEvaluation(@WebParam(name = "idEae") int idEae) {
		Eae eae = Eae.findEae(1);
		EaeAutoEvaluationDto dto = evaluationService.getEaeAutoEvaluation(eae);
		return dtoSessionRemoverService.removeSessionOf(dto);
	}
	
	@GET
	@Path("doSomething")
	public int doSomething(@WebParam(name = "theParameter") int theParameter) {
		return 42;
	}
}
