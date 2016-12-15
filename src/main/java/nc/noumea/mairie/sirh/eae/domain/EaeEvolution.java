package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "EAE_EVOLUTION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeEvolution {

	@Id
	@Column(name = "ID_EAE_EVOLUTION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeEvolution;

	@Column(name = "MOBILIE_GEO", nullable = false)
	@Type(type = "boolean")
	private boolean mobiliteGeo;

	@Column(name = "MOBILIE_FONCT", nullable = false)
	@Type(type = "boolean")
	private boolean mobiliteFonctionnelle;

	@Column(name = "CHANGEMENT_METIER", nullable = false)
	@Type(type = "boolean")
	private boolean changementMetier;

	@ManyToOne
	@JoinColumn(name = "ID_DELAI_ENVISAGE")
	private EaeRefDelai delaiEnvisage;

	@Column(name = "MOBILITE_SERVICE", nullable = false)
	@Type(type = "boolean")
	private boolean mobiliteService;

	@Column(name = "MOBILITE_DIRECTION", nullable = false)
	@Type(type = "boolean")
	private boolean mobiliteDirection;

	@Column(name = "MOBILITE_COLLECTIVITE", nullable = false)
	@Type(type = "boolean")
	private boolean mobiliteCollectivite;

	@Column(name = "NOM_COLLECTIVITE", length = 100)
	private String nomCollectivite;

	@Column(name = "MOBILITE_AUTRE", nullable = false)
	@Type(type = "boolean")
	private boolean mobiliteAutre;

	@Column(name = "CONCOURS", nullable = false)
	@Type(type = "boolean")
	private boolean concours;

	@Column(name = "NOM_CONCOURS", length = 100)
	private String nomConcours;

	@Column(name = "VAE", nullable = false)
	@Type(type = "boolean")
	private boolean vae;

	@Column(name = "NOM_VAE", length = 100)
	private String nomVae;

	@Column(name = "TEMPS_PARTIEL", nullable = false)
	@Type(type = "boolean")
	private boolean tempsPartiel;

	@Column(name = "TEMPS_PARTIEL_ID_SPBHOR")
	private Integer tempsPartielIdSpbhor;

	@Column(name = "RETRAITE", nullable = false)
	@Type(type = "boolean")
	private boolean retraite;

	@Column(name = "DATE_RETRAITE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRetraite;

	@Column(name = "AUTRE_PERSPECTIVE", nullable = false)
	@Type(type = "boolean")
	private boolean autrePerspective;

	@Column(name = "LIB_AUTRE_PERSPECTIVE", length = 255)
	private String libelleAutrePerspective;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_EVOLUTION")
	private EaeCommentaire commentaireEvolution;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_EVALUATEUR")
	private EaeCommentaire commentaireEvaluateur;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_EVALUE")
	private EaeCommentaire commentaireEvalue;

	@OneToMany(mappedBy = "eaeEvolution", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaeEvolutionSouhait ASC")
	private Set<EaeEvolutionSouhait> eaeEvolutionSouhaits = new HashSet<EaeEvolutionSouhait>();

	@OneToMany(mappedBy = "eaeEvolution", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaeDeveloppement ASC")
	private Set<EaeDeveloppement> eaeDeveloppements = new HashSet<EaeDeveloppement>();

	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeEvolution() {
		return idEaeEvolution;
	}

	public void setIdEaeEvolution(Integer idEaeEvolution) {
		this.idEaeEvolution = idEaeEvolution;
	}

	public boolean isMobiliteGeo() {
		return mobiliteGeo;
	}

	public void setMobiliteGeo(boolean mobiliteGeo) {
		this.mobiliteGeo = mobiliteGeo;
	}

	public boolean isMobiliteFonctionnelle() {
		return mobiliteFonctionnelle;
	}

	public void setMobiliteFonctionnelle(boolean mobiliteFonctionnelle) {
		this.mobiliteFonctionnelle = mobiliteFonctionnelle;
	}

	public boolean isChangementMetier() {
		return changementMetier;
	}

	public void setChangementMetier(boolean changementMetier) {
		this.changementMetier = changementMetier;
	}

	public EaeRefDelai getDelaiEnvisage() {
		return delaiEnvisage;
	}

	public void setDelaiEnvisage(EaeRefDelai delaiEnvisage) {
		this.delaiEnvisage = delaiEnvisage;
	}

	public boolean isMobiliteService() {
		return mobiliteService;
	}

	public void setMobiliteService(boolean mobiliteService) {
		this.mobiliteService = mobiliteService;
	}

	public boolean isMobiliteDirection() {
		return mobiliteDirection;
	}

	public void setMobiliteDirection(boolean mobiliteDirection) {
		this.mobiliteDirection = mobiliteDirection;
	}

	public boolean isMobiliteCollectivite() {
		return mobiliteCollectivite;
	}

	public void setMobiliteCollectivite(boolean mobiliteCollectivite) {
		this.mobiliteCollectivite = mobiliteCollectivite;
	}

	public String getNomCollectivite() {
		return nomCollectivite;
	}

	public void setNomCollectivite(String nomCollectivite) {
		this.nomCollectivite = nomCollectivite;
	}

	public boolean isMobiliteAutre() {
		return mobiliteAutre;
	}

	public void setMobiliteAutre(boolean mobiliteAutre) {
		this.mobiliteAutre = mobiliteAutre;
	}

	public boolean isConcours() {
		return concours;
	}

	public void setConcours(boolean concours) {
		this.concours = concours;
	}

	public String getNomConcours() {
		return nomConcours;
	}

	public void setNomConcours(String nomConcours) {
		this.nomConcours = nomConcours;
	}

	public boolean isVae() {
		return vae;
	}

	public void setVae(boolean vae) {
		this.vae = vae;
	}

	public String getNomVae() {
		return nomVae;
	}

	public void setNomVae(String nomVae) {
		this.nomVae = nomVae;
	}

	public boolean isTempsPartiel() {
		return tempsPartiel;
	}

	public void setTempsPartiel(boolean tempsPartiel) {
		this.tempsPartiel = tempsPartiel;
	}

	public Integer getTempsPartielIdSpbhor() {
		return tempsPartielIdSpbhor;
	}

	public void setTempsPartielIdSpbhor(Integer tempsPartielIdSpbhor) {
		this.tempsPartielIdSpbhor = tempsPartielIdSpbhor;
	}

	public boolean isRetraite() {
		return retraite;
	}

	public void setRetraite(boolean retraite) {
		this.retraite = retraite;
	}

	public Date getDateRetraite() {
		return dateRetraite;
	}

	public void setDateRetraite(Date dateRetraite) {
		this.dateRetraite = dateRetraite;
	}

	public boolean isAutrePerspective() {
		return autrePerspective;
	}

	public void setAutrePerspective(boolean autrePerspective) {
		this.autrePerspective = autrePerspective;
	}

	public String getLibelleAutrePerspective() {
		return libelleAutrePerspective;
	}

	public void setLibelleAutrePerspective(String libelleAutrePerspective) {
		this.libelleAutrePerspective = libelleAutrePerspective;
	}

	public EaeCommentaire getCommentaireEvolution() {
		return commentaireEvolution;
	}

	public void setCommentaireEvolution(EaeCommentaire commentaireEvolution) {
		this.commentaireEvolution = commentaireEvolution;
	}

	public EaeCommentaire getCommentaireEvaluateur() {
		return commentaireEvaluateur;
	}

	public void setCommentaireEvaluateur(EaeCommentaire commentaireEvaluateur) {
		this.commentaireEvaluateur = commentaireEvaluateur;
	}

	public EaeCommentaire getCommentaireEvalue() {
		return commentaireEvalue;
	}

	public void setCommentaireEvalue(EaeCommentaire commentaireEvalue) {
		this.commentaireEvalue = commentaireEvalue;
	}

	public Set<EaeEvolutionSouhait> getEaeEvolutionSouhaits() {
		return eaeEvolutionSouhaits;
	}

	public void setEaeEvolutionSouhaits(Set<EaeEvolutionSouhait> eaeEvolutionSouhaits) {
		this.eaeEvolutionSouhaits = eaeEvolutionSouhaits;
	}

	public Set<EaeDeveloppement> getEaeDeveloppements() {
		return eaeDeveloppements;
	}

	public void setEaeDeveloppements(Set<EaeDeveloppement> eaeDeveloppements) {
		this.eaeDeveloppements = eaeDeveloppements;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
