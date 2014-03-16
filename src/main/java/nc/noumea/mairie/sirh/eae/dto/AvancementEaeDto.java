package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;


public class AvancementEaeDto {

	public static final String SGC = "C";
	
	private Integer idAvct;
	private String etat;
	
	private Date dateAvctMoy;
	private GradeDto grade;

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
	
}
