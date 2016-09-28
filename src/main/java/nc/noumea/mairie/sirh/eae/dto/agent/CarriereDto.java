package nc.noumea.mairie.sirh.eae.dto.agent;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;


public class CarriereDto {

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateDebut;
	private Integer noMatr;
	private Integer codeCategorie;
	private String libelleCategorie;
	
	private GradeDto grade;

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Integer getNoMatr() {
		return noMatr;
	}

	public void setNoMatr(Integer noMatr) {
		this.noMatr = noMatr;
	}

	public Integer getCodeCategorie() {
		return codeCategorie;
	}

	public void setCodeCategorie(Integer codeCategorie) {
		this.codeCategorie = codeCategorie;
	}

	public String getLibelleCategorie() {
		return libelleCategorie;
	}

	public void setLibelleCategorie(String libelleCategorie) {
		this.libelleCategorie = libelleCategorie;
	}

	public GradeDto getGrade() {
		return grade;
	}

	public void setGrade(GradeDto grade) {
		this.grade = grade;
	}
	
}
