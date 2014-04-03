package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

public class ReturnMessageDto {

	private List<String> errors;
	private List<String> infos;

	public ReturnMessageDto() {
		errors = new ArrayList<String>();
		infos = new ArrayList<String>();
	}
	
	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public List<String> getInfos() {
		return infos;
	}

	public void setInfos(List<String> infos) {
		this.infos = infos;
	}
}
