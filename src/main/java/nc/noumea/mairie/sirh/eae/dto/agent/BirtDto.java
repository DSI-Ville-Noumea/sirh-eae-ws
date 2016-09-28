package nc.noumea.mairie.sirh.eae.dto.agent;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class BirtDto {

	private AgentEaeDto	agent;
	private Integer		ancienneteEchelonJours;
	private Integer		avctDureeMax;
	private Integer		avctDureeMin;
	private Integer		avctDureeMoy;
	private String		cadre;
	private String		categorie;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEffetAvancement;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeAdministration;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeCollectivite;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeFonctionnaire;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeService;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date		dateEntreeFonction;
	private String		echelon;
	private boolean		estDetache;
	private boolean		estEncadrant;
	private String		grade;
	private Integer		idAgent;
	private Integer		idEaeEvalue;
	private String		position;
	private String		statut;
	private String		fonction;
	private Integer		idEaeEvaluateur;

	public BirtDto() {

	}

	public BirtDto(EaeEvaluateur eaeEval) {
		this.agent = new AgentEaeDto(eaeEval.getAgent());
		this.dateEntreeCollectivite = eaeEval.getDateEntreeCollectivite();
		this.dateEntreeFonction = eaeEval.getDateEntreeFonction();
		this.dateEntreeService = eaeEval.getDateEntreeService();
		this.fonction = eaeEval.getFonction();
		this.idAgent = eaeEval.getIdAgent();
		this.idEaeEvaluateur = eaeEval.getIdEaeEvaluateur();
	}

	public BirtDto(EaeEvalue eaeEvalue, EaeFichePoste fdp) {
		this.agent = new AgentEaeDto(eaeEvalue.getAgent());
		this.ancienneteEchelonJours = eaeEvalue.getAncienneteEchelonJours();
		this.avctDureeMax = eaeEvalue.getAvctDureeMax();
		this.avctDureeMin = eaeEvalue.getAvctDureeMin();
		this.avctDureeMoy = eaeEvalue.getAvctDureeMoy();
		this.cadre = eaeEvalue.getCadre();
		this.categorie = eaeEvalue.getCategorie();
		this.dateEffetAvancement = eaeEvalue.getDateEffetAvancement();
		this.dateEntreeAdministration = eaeEvalue.getDateEntreeAdministration();
		this.dateEntreeCollectivite = eaeEvalue.getDateEntreeCollectivite();
		this.dateEntreeFonctionnaire = eaeEvalue.getDateEntreeFonctionnaire();
		this.dateEntreeService = eaeEvalue.getDateEntreeService();
		this.dateEntreeFonction = fdp == null ? null : fdp.getDateEntreeFonction();
		this.echelon = eaeEvalue.getEchelon();
		this.estDetache = eaeEvalue.isEstDetache();
		this.estEncadrant = eaeEvalue.isEstEncadrant();
		this.grade = eaeEvalue.getGrade();
		this.idAgent = eaeEvalue.getIdAgent();
		this.idEaeEvalue = eaeEvalue.getIdEaeEvalue();
		this.position = eaeEvalue.getPosition() == null ? null : eaeEvalue.getPosition().name();
		this.statut = eaeEvalue.getStatut() == null ? null : eaeEvalue.getStatut().name();
	}

	public AgentEaeDto getAgent() {
		return agent;
	}

	public void setAgent(AgentEaeDto agent) {
		this.agent = agent;
	}

	public Integer getAncienneteEchelonJours() {
		return ancienneteEchelonJours;
	}

	public void setAncienneteEchelonJours(Integer ancienneteEchelonJours) {
		this.ancienneteEchelonJours = ancienneteEchelonJours;
	}

	public Integer getAvctDureeMax() {
		return avctDureeMax;
	}

	public void setAvctDureeMax(Integer avctDureeMax) {
		this.avctDureeMax = avctDureeMax;
	}

	public Integer getAvctDureeMin() {
		return avctDureeMin;
	}

	public void setAvctDureeMin(Integer avctDureeMin) {
		this.avctDureeMin = avctDureeMin;
	}

	public Integer getAvctDureeMoy() {
		return avctDureeMoy;
	}

	public void setAvctDureeMoy(Integer avctDureeMoy) {
		this.avctDureeMoy = avctDureeMoy;
	}

	public String getCadre() {
		return cadre;
	}

	public void setCadre(String cadre) {
		this.cadre = cadre;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public Date getDateEffetAvancement() {
		return dateEffetAvancement;
	}

	public void setDateEffetAvancement(Date dateEffetAvancement) {
		this.dateEffetAvancement = dateEffetAvancement;
	}

	public Date getDateEntreeAdministration() {
		return dateEntreeAdministration;
	}

	public void setDateEntreeAdministration(Date dateEntreeAdministration) {
		this.dateEntreeAdministration = dateEntreeAdministration;
	}

	public Date getDateEntreeCollectivite() {
		return dateEntreeCollectivite;
	}

	public void setDateEntreeCollectivite(Date dateEntreeCollectivite) {
		this.dateEntreeCollectivite = dateEntreeCollectivite;
	}

	public Date getDateEntreeFonctionnaire() {
		return dateEntreeFonctionnaire;
	}

	public void setDateEntreeFonctionnaire(Date dateEntreeFonctionnaire) {
		this.dateEntreeFonctionnaire = dateEntreeFonctionnaire;
	}

	public Date getDateEntreeService() {
		return dateEntreeService;
	}

	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
	}

	public String getEchelon() {
		return echelon;
	}

	public void setEchelon(String echelon) {
		this.echelon = echelon;
	}

	public boolean isEstDetache() {
		return estDetache;
	}

	public void setEstDetache(boolean estDetache) {
		this.estDetache = estDetache;
	}

	public boolean isEstEncadrant() {
		return estEncadrant;
	}

	public void setEstEncadrant(boolean estEncadrant) {
		this.estEncadrant = estEncadrant;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Integer getIdEaeEvalue() {
		return idEaeEvalue;
	}

	public void setIdEaeEvalue(Integer idEaeEvalue) {
		this.idEaeEvalue = idEaeEvalue;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public Integer getIdEaeEvaluateur() {
		return idEaeEvaluateur;
	}

	public void setIdEaeEvaluateur(Integer idEaeEvaluateur) {
		this.idEaeEvaluateur = idEaeEvaluateur;
	}
}
