package nc.noumea.mairie.sirh.eae.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccessForbiddenException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2117783016541797346L;

	public AccessForbiddenException() {
	}
}
