package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAvctEnum;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "EAE_EVALUE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeEvalue {

	@Id
	@Column(name = "ID_EAE_EVALUE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeEvalue;

	@Column(name = "ID_AGENT")
	private int idAgent;

	@Column(name = "DATE_ENTREE_SERVICE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeService;

	@Column(name = "DATE_ENTREE_COLLECTIVITE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeCollectivite;

	@Column(name = "DATE_ENTREE_FONCTIONNAIRE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeFonctionnaire;

	@Column(name = "DATE_ENTREE_ADMINISTRATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeAdministration;

	@Column(name = "STATUT")
	@Enumerated(EnumType.STRING)
	private EaeAgentStatutEnum statut;

	@Column(name = "STATUT_PRECISION")
	private String statutPrecision;

	@Column(name = "ANCIENNETE_ECHELON_JOURS")
	private Integer ancienneteEchelonJours;

	@Column(name = "CADRE")
	private String cadre;

	@Column(name = "CATEGORIE")
	private String categorie;

	@Column(name = "CLASSIFICATION")
	private String classification;

	@Column(name = "GRADE")
	private String grade;

	@Column(name = "ECHELON")
	private String echelon;

	@Column(name = "DATE_EFFET_AVCT")
	private Date dateEffetAvancement;

	@Column(name = "NOUV_GRADE")
	private String nouvGrade;

	@Column(name = "NOUV_ECHELON")
	private String nouvEchelon;

	@Column(name = "EST_ENCADRANT", nullable = false)
	@Type(type = "boolean")
	private boolean estEncadrant;

	@Column(name = "AGENT_DETACHE", nullable = false)
	@Type(type = "boolean")
	private boolean estDetache;

	@Column(name = "TYPE_AVCT")
	@Enumerated(EnumType.STRING)
	private EaeTypeAvctEnum typeAvancement;

	@Column(name = "POSITION")
	@Enumerated(EnumType.STRING)
	private EaeAgentPositionAdministrativeEnum position;

	@Column(name = "AVCT_DUR_MIN")
	private Integer avctDureeMin;

	@Column(name = "AVCT_DUR_MOY")
	private Integer avctDureeMoy;

	@Column(name = "AVCT_DUR_MAX")
	private Integer avctDureeMax;

	@OneToOne
	@JoinColumn(name = "ID_EAE")
	private Eae eae;

	/*
	 * Transient properties (will be populated by AS400 entity manager)
	 */
	@Transient
	private Agent agent;

	@Transient
	public String getAvctDureeMinDisplay() {
		return String.format("%s %s", EaeAvancementEnum.MINI.toString(), getAvctDureeDisplay(getAvctDureeMin()));
	}

	@Transient
	public String getAvctDureeMoyDisplay() {
		return String.format("%s %s", EaeAvancementEnum.MOY.toString(), getAvctDureeDisplay(getAvctDureeMoy()));
	}

	@Transient
	public String getAvctDureeMaxDisplay() {
		return String.format("%s %s", EaeAvancementEnum.MAXI.toString(), getAvctDureeDisplay(getAvctDureeMax()));
	}

	protected String getAvctDureeDisplay(Integer duree) {

		if (statut != EaeAgentStatutEnum.F)
			return "";

		if (duree != null && duree != 0)
			return String.format("(%d mois)", duree);

		return "(NR)";
	}

	public Integer getIdEaeEvalue() {
		return idEaeEvalue;
	}

	public void setIdEaeEvalue(Integer idEaeEvalue) {
		this.idEaeEvalue = idEaeEvalue;
	}

	public int getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(int idAgent) {
		this.idAgent = idAgent;
	}

	public Date getDateEntreeService() {
		return dateEntreeService;
	}

	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
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

	public Date getDateEntreeAdministration() {
		return dateEntreeAdministration;
	}

	public void setDateEntreeAdministration(Date dateEntreeAdministration) {
		this.dateEntreeAdministration = dateEntreeAdministration;
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

	public Integer getAncienneteEchelonJours() {
		return ancienneteEchelonJours;
	}

	public void setAncienneteEchelonJours(Integer ancienneteEchelonJours) {
		this.ancienneteEchelonJours = ancienneteEchelonJours;
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

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
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

	public Date getDateEffetAvancement() {
		return dateEffetAvancement;
	}

	public void setDateEffetAvancement(Date dateEffetAvancement) {
		this.dateEffetAvancement = dateEffetAvancement;
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

	public boolean isEstEncadrant() {
		return estEncadrant;
	}

	public void setEstEncadrant(boolean estEncadrant) {
		this.estEncadrant = estEncadrant;
	}

	public boolean isEstDetache() {
		return estDetache;
	}

	public void setEstDetache(boolean estDetache) {
		this.estDetache = estDetache;
	}

	public EaeTypeAvctEnum getTypeAvancement() {
		return typeAvancement;
	}

	public void setTypeAvancement(EaeTypeAvctEnum typeAvancement) {
		this.typeAvancement = typeAvancement;
	}

	public EaeAgentPositionAdministrativeEnum getPosition() {
		return position;
	}

	public void setPosition(EaeAgentPositionAdministrativeEnum position) {
		this.position = position;
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

	public Integer getAvctDureeMax() {
		return avctDureeMax;
	}

	public void setAvctDureeMax(Integer avctDureeMax) {
		this.avctDureeMax = avctDureeMax;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
