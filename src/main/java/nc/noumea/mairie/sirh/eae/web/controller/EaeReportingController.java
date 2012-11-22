package nc.noumea.mairie.sirh.eae.web.controller;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeReportFormatEnum;
import nc.noumea.mairie.sirh.eae.service.EaeReportingServiceException;
import nc.noumea.mairie.sirh.eae.service.IEaeReportingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/reporting")
public class EaeReportingController {

	private Logger logger = LoggerFactory.getLogger(EaeReportingController.class);
	
	@Autowired
	private IEaeReportingService eaeReportingService;
	
	@ResponseBody
	@RequestMapping(value = "eae", produces = "application/pdf", method = RequestMethod.GET)
	public ResponseEntity<byte []> getEaeIdentifitcation(@RequestParam("idEae") int idEae) {

		Eae eae = Eae.findEae(idEae);
		
		if (eae == null)
			return new ResponseEntity<byte []>(HttpStatus.NOT_FOUND);
		
		byte[] responseData = null;
		
		try {
			responseData = eaeReportingService.getEaeReportAsByteArray(idEae, EaeReportFormatEnum.PDF);
		} catch (EaeReportingServiceException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<byte []>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<byte []>(responseData, HttpStatus.OK);
	}
}
