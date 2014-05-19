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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.domain.Agent;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "EAE_FICHE_POSTE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFichePoste {

	@Id
	@Column(name = "ID_EAE_FICHE_POSTE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeFichePoste;

	@Column(name = "PRIMAIRE", nullable = false)
	@Type(type = "boolean")
	private boolean primary;

	@Column(name = "DIRECTION_SERVICE")
	private String directionService;

	@Column(name = "SERVICE")
	private String service;

	@Column(name = "SECTION_SERVICE")
	private String sectionService;

	@Column(name = "EMPLOI")
	private String emploi;

	@Column(name = "FONCTION")
	private String fonction;

	@Column(name = "DATE_ENTREE_FONCTION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeFonction;

	@Column(name = "DATE_ENTREE_COLLECT_RESP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeCollectiviteResponsable;

	@Column(name = "DATE_ENTREE_FONCTION_RESP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeFonctionResponsable;

	@Column(name = "DATE_ENTREE_SERVICE_RESP")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeServiceResponsable;

	@Column(name = "GRADE_POSTE")
	private String gradePoste;

	@Column(name = "LOCALISATION")
	private String localisation;

	@Column(name = "FONCTION_RESP")
	private String fonctionResponsable;

	@Column(name = "ID_SHD")
	private Integer idAgentShd;

	@Column(name = "ID_SIRH_FICHE_POSTE")
	private Integer idSirhFichePoste;

	@Column(name = "MISSIONS")
	@Lob
	private String missions;

	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;

	@OneToMany(mappedBy = "eaeFichePoste", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeFdpActivite> eaeFdpActivites = new HashSet<EaeFdpActivite>();

	@OneToMany(mappedBy = "eaeFichePoste", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeFdpCompetence> eaeFdpCompetences = new HashSet<EaeFdpCompetence>();

	@Column(name = "CODE_SERVICE")
	private String codeService;

	@Transient
	private Agent agentShd;

	public Integer getIdEaeFichePoste() {
		return idEaeFichePoste;
	}

	public void setIdEaeFichePoste(Integer idEaeFichePoste) {
		this.idEaeFichePoste = idEaeFichePoste;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSectionService() {
		return sectionService;
	}

	public void setSectionService(String sectionService) {
		this.sectionService = sectionService;
	}

	public String getEmploi() {
		return emploi;
	}

	public void setEmploi(String emploi) {
		this.emploi = emploi;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public Date getDateEntreeCollectiviteResponsable() {
		return dateEntreeCollectiviteResponsable;
	}

	public void setDateEntreeCollectiviteResponsable(Date dateEntreeCollectiviteResponsable) {
		this.dateEntreeCollectiviteResponsable = dateEntreeCollectiviteResponsable;
	}

	public Date getDateEntreeFonctionResponsable() {
		return dateEntreeFonctionResponsable;
	}

	public void setDateEntreeFonctionResponsable(Date dateEntreeFonctionResponsable) {
		this.dateEntreeFonctionResponsable = dateEntreeFonctionResponsable;
	}

	public Date getDateEntreeServiceResponsable() {
		return dateEntreeServiceResponsable;
	}

	public void setDateEntreeServiceResponsable(Date dateEntreeServiceResponsable) {
		this.dateEntreeServiceResponsable = dateEntreeServiceResponsable;
	}

	public String getGradePoste() {
		return gradePoste;
	}

	public void setGradePoste(String gradePoste) {
		this.gradePoste = gradePoste;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public String getFonctionResponsable() {
		return fonctionResponsable;
	}

	public void setFonctionResponsable(String fonctionResponsable) {
		this.fonctionResponsable = fonctionResponsable;
	}

	public Integer getIdAgentShd() {
		return idAgentShd;
	}

	public void setIdAgentShd(Integer idAgentShd) {
		this.idAgentShd = idAgentShd;
	}

	public Integer getIdSirhFichePoste() {
		return idSirhFichePoste;
	}

	public void setIdSirhFichePoste(Integer idSirhFichePoste) {
		this.idSirhFichePoste = idSirhFichePoste;
	}

	public String getMissions() {
		return missions;
	}

	public void setMissions(String missions) {
		this.missions = missions;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}

	public Set<EaeFdpActivite> getEaeFdpActivites() {
		return eaeFdpActivites;
	}

	public void setEaeFdpActivites(Set<EaeFdpActivite> eaeFdpActivites) {
		this.eaeFdpActivites = eaeFdpActivites;
	}

	public Set<EaeFdpCompetence> getEaeFdpCompetences() {
		return eaeFdpCompetences;
	}

	public void setEaeFdpCompetences(Set<EaeFdpCompetence> eaeFdpCompetences) {
		this.eaeFdpCompetences = eaeFdpCompetences;
	}

	public String getCodeService() {
		return codeService;
	}

	public void setCodeService(String codeService) {
		this.codeService = codeService;
	}

	public Agent getAgentShd() {
		return agentShd;
	}

	public void setAgentShd(Agent agentShd) {
		this.agentShd = agentShd;
	}
}
