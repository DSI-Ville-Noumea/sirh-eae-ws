package nc.noumea.mairie.sirh.eae.web.controller;

import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeReportFormatEnum;
import nc.noumea.mairie.sirh.eae.dto.CanFinalizeEaeDto;
import nc.noumea.mairie.sirh.eae.dto.EaeDashboardItemDto;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.EaeListItemDto;
import nc.noumea.mairie.sirh.eae.dto.FinalizationInformationDto;
import nc.noumea.mairie.sirh.eae.security.IEaeSecurityProvider;
import nc.noumea.mairie.sirh.eae.service.EaeReportingServiceException;
import nc.noumea.mairie.sirh.eae.service.EaeServiceException;
import nc.noumea.mairie.sirh.eae.service.IAgentMatriculeConverterService;
import nc.noumea.mairie.sirh.eae.service.IEaeReportingService;
import nc.noumea.mairie.sirh.eae.service.IEaeService;
import nc.noumea.mairie.sirh.eae.service.SirhWSConsumerException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/eaes")
public class EaeController {
	
	private Logger logger = LoggerFactory.getLogger(EaeController.class);
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private IEaeService eaeService;
	
	@Autowired
	private IAgentMatriculeConverterService agentMatriculeConverterService;
	
	@Autowired
	private IEaeSecurityProvider eaeSecurityProvider;
	
	@Autowired
	private IEaeReportingService eaeReportingService;
		
	@ResponseBody
	@RequestMapping(value = "listEaesByAgent", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> listEaesByAgent(@RequestParam("idAgent") int idAgent) {
		
		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
    	
    	List<EaeListItemDto> result;
		try {
			result = eaeService.listEaesByAgentId(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = EaeListItemDto.getSerializerForEaeListItemDto().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "initialiserEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> initializeEae(@RequestParam("idAgent") int idAgent, @RequestParam("idEvalue") int idEvalue) {
		
		Integer convertedIdAgentEvalue = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idEvalue);
    	
		List<Eae> agentEaes = eaeService.findCurrentAndPreviousEaesByAgentId(convertedIdAgentEvalue);
		
		if (agentEaes.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
		Eae lastEae = agentEaes.get(0);
		Eae previousEae = null;
		if (agentEaes.size() > 1)
			previousEae = agentEaes.get(1);
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(lastEae.getIdEae(), idAgent);
		
		if (response != null)
			return response;
		
		try {
			eaeService.initializeEae(lastEae, previousEae);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_INITIALISE_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "affecterDelegataire", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> setDelegataire(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestParam("idDelegataire") int idDelegataire) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Integer convertedIdAgentDelegataire = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idDelegataire);
    	
		Eae eae = eaeService.getEae(idEae);
		
		try {
			eaeService.setDelegataire(eae, convertedIdAgentDelegataire);
		} catch (EaeServiceException e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_DELEGATAIRE_OK", null, null), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "tableauDeBord", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getEaesDashboard(@RequestParam("idAgent") int idAgent) {
		
		Integer convertedId = agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent);
    	
    	List<EaeDashboardItemDto> result;
		try {
			result = eaeService.getEaesDashboard(convertedId);
		} catch (SirhWSConsumerException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (result.isEmpty())
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT); 
		
		String jsonResult = EaeDashboardItemDto.getSerializerForEaeDashboardItemDto().serialize(result);
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "canFinalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> canFinalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = eaeService.getEae(idEae);
	
		CanFinalizeEaeDto dto = eaeService.canFinalizEae(eae);
		
		if (!dto.isCanFinalize())
			return new ResponseEntity<String>(dto.getMessage(), HttpStatus.CONFLICT);
		else
			return new ResponseEntity<String>(HttpStatus.OK);
	}

	@ResponseBody
	@RequestMapping(value = "getFinalizationInformation", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public ResponseEntity<String> getFinalizationInformation(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = eaeService.getEae(idEae);
		
		FinalizationInformationDto dto = eaeService.getFinalizationInformation(eae);		
		
		String jsonResult = dto.serializeInJSON();
		
		return new ResponseEntity<String>(jsonResult, HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(value = "finalizeEae", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@Transactional(value = "eaeTransactionManager")
	public ResponseEntity<String> finalizeEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestBody String eaeFinalizationDtoJson) {
		
		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndWriteRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		Eae eae = eaeService.getEae(idEae);
		
		try {
			EaeFinalizationDto dto = new EaeFinalizationDto().deserializeFromJSON(eaeFinalizationDtoJson);
			eaeService.finalizEae(eae, agentMatriculeConverterService.tryConvertFromADIdAgentToEAEIdAgent(idAgent), dto);
			eae.flush();
		} catch (EaeServiceException e) {
			eae.clear();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<String>(messageSource.getMessage("EAE_FINALISE_OK", null, null), HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "downloadEae", method = RequestMethod.GET)
	public ResponseEntity downloadEae(@RequestParam("idEae") int idEae, @RequestParam("idAgent") int idAgent, @RequestParam(value = "format", required = false) String format) {

		ResponseEntity<String> response = eaeSecurityProvider.checkEaeAndReadRight(idEae, idAgent);
		
		if (response != null)
			return response;
		
		byte[] responseData = null;
		EaeReportFormatEnum formatValue = null;
		
		try {
			formatValue = eaeReportingService.getFileFormatFromString(format);
			responseData = eaeReportingService.getEaeReportAsByteArray(idEae, formatValue);
		} catch (EaeReportingServiceException e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
		}
		
		return new ResponseEntity<byte []>(responseData, getHeadersForFileFormat(formatValue), HttpStatus.OK);
	}
	
	private HttpHeaders getHeadersForFileFormat(EaeReportFormatEnum format) {
		
		String contentType;
		
		switch(format) {
			default:
			case PDF:
				contentType = "application/pdf";
				break;
			case DOC:
				contentType = "application/msword";
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", contentType);
		
		return headers;
	}
}
	
	
