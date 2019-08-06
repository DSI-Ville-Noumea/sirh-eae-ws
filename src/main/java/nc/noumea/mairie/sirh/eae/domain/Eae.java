package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

@Entity
@Table(name = "EAE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
@NamedQueries({ @NamedQuery(name = "findEaeByIdAgent", query = "select e from Eae e where e.idEae = :idEae " ),
	@NamedQuery(name = "findEaeForMigration", query = "select e from Eae e where e.etat in ('F','CO') and e.eaeCampagne.idCampagneEae in (26,27,28) order by idEae" )})
public class Eae {

	@Id
	@Column(name = "ID_EAE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEae;

	/*
	 * Mapped properties
	 */
	@NotNull
	@Column(name = "ETAT")
	@Enumerated(EnumType.STRING)
	private EaeEtatEnum etat;

	@NotNull
	@Column(name = "CAP")
	private boolean cap;

	@NotNull
	@Column(name = "DOC_ATTACHE")
	private boolean docAttache;

	@Column(name = "DATE_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;

	@Column(name = "DATE_FIN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFin;

	@Column(name = "DATE_ENTRETIEN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntretien;

	@Column(name = "DUREE_ENTRETIEN", nullable = true)
	private Integer dureeEntretienMinutes;

	@Column(name = "DATE_FINALISE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFinalisation;

	@Column(name = "DATE_CONTROLE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateControle;

	@Column(name = "HEURE_CONTROLE")
	private String heureControle;

	@Column(name = "USER_CONTROLE")
	private String userControle;

	@Column(name = "ID_DELEGATAIRE")
	private Integer idAgentDelegataire;

	@OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private EaeEvaluation eaeEvaluation;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COMMENTAIRE")
	private EaeCommentaire commentaire;

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeEvaluateur> eaeEvaluateurs = new HashSet<EaeEvaluateur>();

	@OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private EaeEvalue eaeEvalue;

	@OneToMany(mappedBy = "eae", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeFichePoste> eaeFichePostes = new HashSet<EaeFichePoste>();

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaeDiplome asc")
	private Set<EaeDiplome> eaeDiplomes = new HashSet<EaeDiplome>();

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeParcoursPro> eaeParcoursPros = new HashSet<EaeParcoursPro>();

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeFormation> eaeFormations = new HashSet<EaeFormation>();

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaeResultat asc")
	private Set<EaeResultat> eaeResultats = new HashSet<EaeResultat>();

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaePlanAction asc")
	private Set<EaePlanAction> eaePlanActions = new HashSet<EaePlanAction>();

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeAppreciation> eaeAppreciations = new HashSet<EaeAppreciation>();

	@OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private EaeAutoEvaluation eaeAutoEvaluation;

	@OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private EaeEvolution eaeEvolution;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_CAMPAGNE_EAE")
	private EaeCampagne eaeCampagne;

	@OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy(value = "idEaeFinalisation desc")
	private Set<EaeFinalisation> eaeFinalisations = new HashSet<EaeFinalisation>();

	/*
	 * Transient properties (will be populated by AS400 entity manager)
	 */
	@Transient
	private Agent agentDelegataire;

	public EaeFichePoste getPrimaryFichePoste() {

		for (EaeFichePoste fdp : this.getEaeFichePostes()) {
			if (fdp.isPrimary())
				return fdp;
		}

		return null;
	}

	public EaeFichePoste getSecondaryFichePoste() {

		for (EaeFichePoste fdp : this.getEaeFichePostes()) {
			if (!fdp.isPrimary())
				return fdp;
		}

		return null;
	}

	public boolean isEvaluateur(int idAgent) {

		for (EaeEvaluateur e : this.getEaeEvaluateurs()) {
			if (e.getIdAgent() == idAgent)
				return true;
		}

		return false;
	}

	public boolean isDelegataire(int idAgent) {
		return (getIdAgentDelegataire() != null && getIdAgentDelegataire() == idAgent);
	}

	public boolean isEvaluateurOrDelegataire(int idAgent) {
		return isEvaluateur(idAgent) || isDelegataire(idAgent);
	}

	public EaeFinalisation getLatestFinalisation() {

		EaeFinalisation latestFinalisation = null;

		for (EaeFinalisation finalisation : this.getEaeFinalisations()) {
			if (latestFinalisation == null
					|| finalisation.getDateFinalisation().after(latestFinalisation.getDateFinalisation()))
				latestFinalisation = finalisation;
		}

		return latestFinalisation;
	}

	public void addEaeFichePoste(EaeFichePoste eaeFP) {
		if (!this.getEaeFichePostes().contains(eaeFP)) {
			this.getEaeFichePostes().add(eaeFP);
		}
	}

	public Integer getIdEae() {
		return idEae;
	}

	public void setIdEae(Integer idEae) {
		this.idEae = idEae;
	}

	public EaeEtatEnum getEtat() {
		return etat;
	}

	public void setEtat(EaeEtatEnum etat) {
		this.etat = etat;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public boolean isDocAttache() {
		return docAttache;
	}

	public void setDocAttache(boolean docAttache) {
		this.docAttache = docAttache;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public Integer getDureeEntretienMinutes() {
		return dureeEntretienMinutes;
	}

	public void setDureeEntretienMinutes(Integer dureeEntretienMinutes) {
		this.dureeEntretienMinutes = dureeEntretienMinutes;
	}

	public Date getDateFinalisation() {
		return dateFinalisation;
	}

	public void setDateFinalisation(Date dateFinalisation) {
		this.dateFinalisation = dateFinalisation;
	}

	public Date getDateControle() {
		return dateControle;
	}

	public void setDateControle(Date dateControle) {
		this.dateControle = dateControle;
	}

	public String getHeureControle() {
		return heureControle;
	}

	public void setHeureControle(String heureControle) {
		this.heureControle = heureControle;
	}

	public String getUserControle() {
		return userControle;
	}

	public void setUserControle(String userControle) {
		this.userControle = userControle;
	}

	public Integer getIdAgentDelegataire() {
		return idAgentDelegataire;
	}

	public void setIdAgentDelegataire(Integer idAgentDelegataire) {
		this.idAgentDelegataire = idAgentDelegataire;
	}

	public EaeEvaluation getEaeEvaluation() {
		return eaeEvaluation;
	}

	public void setEaeEvaluation(EaeEvaluation eaeEvaluation) {
		this.eaeEvaluation = eaeEvaluation;
	}

	public EaeCommentaire getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(EaeCommentaire commentaire) {
		this.commentaire = commentaire;
	}

	public Set<EaeEvaluateur> getEaeEvaluateurs() {
		return eaeEvaluateurs;
	}

	public void setEaeEvaluateurs(Set<EaeEvaluateur> eaeEvaluateurs) {
		this.eaeEvaluateurs = eaeEvaluateurs;
	}

	public EaeEvalue getEaeEvalue() {
		return eaeEvalue;
	}

	public void setEaeEvalue(EaeEvalue eaeEvalue) {
		this.eaeEvalue = eaeEvalue;
	}

	public Set<EaeFichePoste> getEaeFichePostes() {
		return eaeFichePostes;
	}

	public void setEaeFichePostes(Set<EaeFichePoste> eaeFichePostes) {
		this.eaeFichePostes = eaeFichePostes;
	}

	public Set<EaeDiplome> getEaeDiplomes() {
		return eaeDiplomes;
	}

	public void setEaeDiplomes(Set<EaeDiplome> eaeDiplomes) {
		this.eaeDiplomes = eaeDiplomes;
	}

	public Set<EaeParcoursPro> getEaeParcoursPros() {
		return eaeParcoursPros;
	}

	public void setEaeParcoursPros(Set<EaeParcoursPro> eaeParcoursPros) {
		this.eaeParcoursPros = eaeParcoursPros;
	}

	public Set<EaeFormation> getEaeFormations() {
		return eaeFormations;
	}

	public void setEaeFormations(Set<EaeFormation> eaeFormations) {
		this.eaeFormations = eaeFormations;
	}

	public Set<EaeResultat> getEaeResultats() {
		return eaeResultats;
	}

	public void setEaeResultats(Set<EaeResultat> eaeResultats) {
		this.eaeResultats = eaeResultats;
	}

	public Set<EaePlanAction> getEaePlanActions() {
		return eaePlanActions;
	}

	public void setEaePlanActions(Set<EaePlanAction> eaePlanActions) {
		this.eaePlanActions = eaePlanActions;
	}

	public Set<EaeAppreciation> getEaeAppreciations() {
		return eaeAppreciations;
	}

	public void setEaeAppreciations(Set<EaeAppreciation> eaeAppreciations) {
		this.eaeAppreciations = eaeAppreciations;
	}

	public EaeAutoEvaluation getEaeAutoEvaluation() {
		return eaeAutoEvaluation;
	}

	public void setEaeAutoEvaluation(EaeAutoEvaluation eaeAutoEvaluation) {
		this.eaeAutoEvaluation = eaeAutoEvaluation;
	}

	public EaeEvolution getEaeEvolution() {
		return eaeEvolution;
	}

	public void setEaeEvolution(EaeEvolution eaeEvolution) {
		this.eaeEvolution = eaeEvolution;
	}

	public EaeCampagne getEaeCampagne() {
		return eaeCampagne;
	}

	public void setEaeCampagne(EaeCampagne eaeCampagne) {
		this.eaeCampagne = eaeCampagne;
	}

	public Set<EaeFinalisation> getEaeFinalisations() {
		return eaeFinalisations;
	}

	public void setEaeFinalisations(Set<EaeFinalisation> eaeFinalisations) {
		this.eaeFinalisations = eaeFinalisations;
	}

	public Agent getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(Agent agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}
}
