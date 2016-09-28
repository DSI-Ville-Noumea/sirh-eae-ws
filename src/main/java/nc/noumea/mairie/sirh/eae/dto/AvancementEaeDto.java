package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.dto.agent.GradeDto;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class AvancementEaeDto {

	public static final String	SGC	= "C";

	private Integer				idAvct;
	private String				etat;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date				dateAvctMoy;
	private GradeDto			grade;
	private Integer				idMotifAvct;

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Integer getIdAvct() {
		return idAvct;
	}

	public void setIdAvct(Integer idAvct) {
		this.idAvct = idAvct;
	}

	public Date getDateAvctMoy() {
		return dateAvctMoy;
	}

	public void setDateAvctMoy(Date dateAvctMoy) {
		this.dateAvctMoy = dateAvctMoy;
	}

	public GradeDto getGrade() {
		return grade;
	}

	public void setGrade(GradeDto grade) {
		this.grade = grade;
	}

	public Integer getIdMotifAvct() {
		return idMotifAvct;
	}

	public void setIdMotifAvct(Integer idMotifAvct) {
		this.idMotifAvct = idMotifAvct;
	}

}
