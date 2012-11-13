package nc.noumea.mairie.sirh.eae.web.controller;

import javax.jws.WebParam;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeIdentificationDto;
import nc.noumea.mairie.sirh.eae.service.IEvaluationService;
import nc.noumea.mairie.sirh.eae.web.soap.DtoSessionRemoverService;

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
	
	@RequestMapping(value = "eaeIdentification", produces = "application/xml", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ModelAndView getEaeIdentification(@WebParam(name = "idEae") int idEae) {
		Eae eae = Eae.findEae(idEae);
		EaeIdentificationDto dto = evaluationService.getEaeIdentification(eae);
		return new ModelAndView("xmlView", "object", dtoSessionRemoverService.removeSessionOf(dto));
	}
}
