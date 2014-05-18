package nc.noumea.mairie.sirh.eae.web.controller.xml;

import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.dto.EaeAppreciationsDto;
import nc.noumea.mairie.sirh.eae.dto.EaeAutoEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvaluationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionDto;
import nc.noumea.mairie.sirh.eae.dto.EaeResultatsDto;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDtoList;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/xml/evaluation")
public class EvaluationXmlController {

	@Autowired
	private IEvaluationService evaluationService;

	@Autowired
	private DtoSessionRemoverService dtoSessionRemoverService;

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;

	@RequestMapping(value = "eaeIdentification", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeIdentification(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(eae);

		// For Birt reporting purposes (in order to have the fields displayed)
		if (dto.getEvaluateurs().size() == 0)
			dto.getEvaluateurs().add(new EaeEvaluateur());

		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeFichePoste", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeFichePoste(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeFichePosteDtoList dto = new EaeFichePosteDtoList();
		dto.setEaeFichePostes(evaluationService.getEaeFichePoste(eae));

		// For Birt reporting purposes (in order to have the fields displayed)
		if (dto.getEaeFichePostes().size() == 0)
			dto.getEaeFichePostes().add(new EaeFichePosteDto());

		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeResultats", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeResultat(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeResultatsDto dto = evaluationService.getEaeResultats(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeAppreciations", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeAppreciations(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeAppreciationsDto dto = evaluationService.getEaeAppreciations(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeEvaluation", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeEvaluation(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeEvaluationDto dto = evaluationService.getEaeEvaluation(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeAutoEvaluation", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeAutoEvaluation(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeAutoEvaluationDto dto = evaluationService.getEaeAutoEvaluation(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaePlanAction", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaePlanAction(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaePlanActionDto dto = evaluationService.getEaePlanAction(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}

	@RequestMapping(value = "eaeEvolution", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeEvolution(@WebParam(name = "idEae") int idEae) {
		Eae eae = eaeEntityManager.find(Eae.class, idEae);
		EaeEvolutionDto dto = evaluationService.getEaeEvolution(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}
}
