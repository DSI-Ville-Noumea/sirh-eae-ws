package nc.noumea.mairie.sirh.eae.web.controller.xml;

import javax.jws.WebParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.agent.BirtDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDtoList;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

@Controller
@RequestMapping("/xml/evaluation")
public class EvaluationXmlController {

	@Autowired
	private IEvaluationService evaluationService;

	@Autowired
	private DtoSessionRemoverService dtoSessionRemoverService;

	@RequestMapping(value = "eaeIdentification", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeIdentification(@WebParam(name = "idEae") int idEae) {
		
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(idEae);

		// For Birt reporting purposes (in order to have the fields displayed)
		if (dto.getEvaluateurs().size() == 0)
			dto.getEvaluateurs().add(new BirtDto());

		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeFichePoste", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeFichePoste(@WebParam(name = "idEae") int idEae) {
		
		EaeFichePosteDtoList dto = new EaeFichePosteDtoList();
		dto.setEaeFichePostes(evaluationService.getEaeFichePoste(idEae));

		// For Birt reporting purposes (in order to have the fields displayed)
		if (dto.getEaeFichePostes().size() == 0)
			dto.getEaeFichePostes().add(new EaeFichePosteDto());

		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeResultats", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeResultat(@WebParam(name = "idEae") int idEae) {
		
		EaeResultatsDto dto = evaluationService.getEaeResultats(idEae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeAppreciations", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeAppreciations(@WebParam(name = "idEae") int idEae) {
		
		EaeAppreciationsDto dto = evaluationService.getEaeAppreciations(idEae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeEvaluation", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeEvaluation(@WebParam(name = "idEae") int idEae) {
		
		EaeEvaluationDto dto = evaluationService.getEaeEvaluation(idEae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeAutoEvaluation(@WebParam(name = "idEae") int idEae) {
		
		EaeAutoEvaluationDto dto = evaluationService.getEaeAutoEvaluation(idEae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaePlanAction", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaePlanAction(@WebParam(name = "idEae") int idEae) {
		
		EaePlanActionDto dto = evaluationService.getEaePlanAction(idEae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeEvolution", produces = "application/xml", method = RequestMethod.GET)
	public ModelAndView getEaeEvolution(@WebParam(name = "idEae") int idEae) {
		
		EaeEvolutionDto dto = evaluationService.getEaeEvolution(idEae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}
}
