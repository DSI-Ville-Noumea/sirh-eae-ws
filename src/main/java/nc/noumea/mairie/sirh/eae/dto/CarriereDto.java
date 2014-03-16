package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

public class CarriereDto {

	private Date dateDebut;
	private Integer noMatr;
	private String codeCategorie;
	private String libelleCategorie;
	private String codeGrade;
	
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

	public String getCodeCategorie() {
		return codeCategorie;
	}

	public void setCodeCategorie(String codeCategorie) {
		this.codeCategorie = codeCategorie;
	}

	public String getCodeGrade() {
		return codeGrade;
	}

	public void setCodeGrade(String codeGrade) {
		this.codeGrade = codeGrade;
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
