package nc.noumea.mairie.sirh.eae.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2128783016541797346L;

	public ConflictException(String message) {
        super(message);
    }
}
