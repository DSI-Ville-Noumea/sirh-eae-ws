package nc.noumea.mairie.sirh.eae.web.controller;

import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7734659556601851331L;
	
	public NotFoundException() {
	}
	
	public NotFoundException(ReturnMessageDto pResult) {
		this.result = pResult;
	}
	
	private ReturnMessageDto result;

	public ReturnMessageDto getResult() {
		return result;
	}

	public void setResult(ReturnMessageDto result) {
		this.result = result;
	}
	
}
