package nc.noumea.mairie.sirh.eae.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7291138785335378491L;

}
