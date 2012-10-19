package nc.noumea.mairie.sirh.eae.dto.identification;

import java.util.Date;

import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;

public class EaeIdentificationStatutDto {
	
	private EaeAgentStatutEnum statut;	
	private String cadre;
	private String grade;
	private String echelon;
	private String categorie;
	private String classification;
	private Integer ancienneteEchelonJours;
	private String nouvGrade;
	private String nouvEchelon;
	private Date dateEffet;
	private String statutPrecision;
	
	public EaeIdentificationStatutDto() {
		
	}
	
	public EaeIdentificationStatutDto(EaeEvalue eaeEvalue) {
		this.cadre = eaeEvalue.getCadre();
		this.grade = eaeEvalue.getGrade();
		this.echelon = eaeEvalue.getEchelon();
		this.categorie = eaeEvalue.getCategorie();
		this.classification = eaeEvalue.getClassification();
		this.ancienneteEchelonJours = eaeEvalue.getAncienneteEchelonJours();
		this.nouvGrade = eaeEvalue.getNouvGrade();
		this.nouvEchelon = eaeEvalue.getNouvEchelon();
		this.dateEffet = eaeEvalue.getDateEffetAvancement();
		this.statutPrecision = eaeEvalue.getStatutPrecision();
		this.statut = eaeEvalue.getStatut();
	}	
	
	public EaeAgentStatutEnum getStatut() {
		return statut;
	}

	public void setStatut(EaeAgentStatutEnum statut) {
		this.statut = statut;
	}

	public String getStatutPrecision() {
		return statutPrecision;
	}

	public void setStatutPrecision(String statutPrecision) {
		this.statutPrecision = statutPrecision;
	}

	public String getCadre() {
		return cadre;
	}

	public void setCadre(String cadre) {
		this.cadre = cadre;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Integer getAncienneteEchelonJours() {
		return ancienneteEchelonJours;
	}

	public void setAncienneteEchelonJours(Integer ancienneteEchelonJours) {
		this.ancienneteEchelonJours = ancienneteEchelonJours;
	}

	public String getNouvGrade() {
		return nouvGrade;
	}

	public void setNouvGrade(String nouvGrade) {
		this.nouvGrade = nouvGrade;
	}

	public String getNouvEchelon() {
		return nouvEchelon;
	}

	public void setNouvEchelon(String nouvEchelon) {
		this.nouvEchelon = nouvEchelon;
	}

	public Date getDateEffet() {
		return dateEffet;
	}

	public void setDateEffet(Date dateEffet) {
		this.dateEffet = dateEffet;
	}
}
