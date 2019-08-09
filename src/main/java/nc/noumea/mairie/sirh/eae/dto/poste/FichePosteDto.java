package nc.noumea.mairie.sirh.eae.dto.poste;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FichePosteDto {

	private Integer idFichePoste;
	private String numero;
	private String titre;
	private String direction;
	private String budget;
	private String budgete;
	private String reglementaire;
	private String cadreEmploi;
	private String niveauEtudes;
	private String codeService;
	private Integer idServiceADS;
	private String service;
	private String section;
	private String lieu;
	private String gradePoste;
	private String superieur;

	private String missions;

	private List<String> activites;
	private List<String> savoirs;
	private List<String> savoirsFaire;
	private List<String> comportementsProfessionnels;

	// informations complementaires pour l impression FDP SIRH
	private String dateDebutAffectation;
	private String agent;
	private String categorie;
	private String filiere;
	private String superieurHierarchiqueFP;
	private String superieurHierarchiqueAgent;
	private String remplaceFP;
	private String remplaceAgent;
	private String emploiPrimaire;
	private String emploiSecondaire;
	private String NFA;
	private String OPI;
	private String anneeEmploi;
	private String statutFDP;
	private String natureCredit;

	private List<String> avantages;
	private List<String> delegations;
	private List<String> regimesIndemnitaires;
	private List<String> primes;
	
	private TitrePosteDto titrePoste;

	private Integer idAgent;
	
	// V2
	private String informationsComplementaires;
	private List<ActiviteMetierSavoirFaire> activiteMetier = new ArrayList<>();
	private List<String> savoirFaireMetier = new ArrayList<>();
	private List<String> activiteGenerale = new ArrayList<>();
	private List<String> conditionExercice = new ArrayList<>();
	private List<String> competenceManagement = new ArrayList<>();
	
	public FichePosteDto() {
		activites = new ArrayList<String>();
		savoirs = new ArrayList<String>();
		savoirsFaire = new ArrayList<String>();
		comportementsProfessionnels = new ArrayList<String>();

		avantages = new ArrayList<String>();
		delegations = new ArrayList<String>();
		regimesIndemnitaires = new ArrayList<String>();
		primes = new ArrayList<String>();
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getBudgete() {
		return budgete;
	}

	public void setBudgete(String budgete) {
		this.budgete = budgete;
	}

	public String getReglementaire() {
		return reglementaire;
	}

	public void setReglementaire(String reglementaire) {
		this.reglementaire = reglementaire;
	}

	public String getCadreEmploi() {
		return cadreEmploi;
	}

	public void setCadreEmploi(String cadreEmploi) {
		this.cadreEmploi = cadreEmploi;
	}

	public String getNiveauEtudes() {
		return niveauEtudes;
	}

	public void setNiveauEtudes(String niveauEtudes) {
		this.niveauEtudes = niveauEtudes;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getLieu() {
		return lieu;
	}

	public void setLieu(String lieu) {
		this.lieu = lieu;
	}

	public String getGradePoste() {
		return gradePoste;
	}

	public void setGradePoste(String gradePoste) {
		this.gradePoste = gradePoste;
	}

	public String getSuperieur() {
		return superieur;
	}

	public void setSuperieur(String superieur) {
		this.superieur = superieur;
	}

	public String getMissions() {
		return missions;
	}

	public void setMissions(String missions) {
		this.missions = missions;
	}

	public List<String> getActivites() {
		return activites;
	}

	public void setActivites(List<String> activites) {
		this.activites = activites;
	}

	public List<String> getSavoirs() {
		return savoirs;
	}

	public void setSavoirs(List<String> savoirs) {
		this.savoirs = savoirs;
	}

	public List<String> getSavoirsFaire() {
		return savoirsFaire;
	}

	public void setSavoirsFaire(List<String> savoirsFaire) {
		this.savoirsFaire = savoirsFaire;
	}

	public List<String> getComportementsProfessionnels() {
		return comportementsProfessionnels;
	}

	public void setComportementsProfessionnels(List<String> comportementsProfessionnels) {
		this.comportementsProfessionnels = comportementsProfessionnels;
	}

	public String getDateDebutAffectation() {
		return dateDebutAffectation;
	}

	public void setDateDebutAffectation(String dateDebutAffectation) {
		this.dateDebutAffectation = dateDebutAffectation;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getCategorie() {
		return categorie;
	}

	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public String getFiliere() {
		return filiere;
	}

	public void setFiliere(String filiere) {
		this.filiere = filiere;
	}

	public String getSuperieurHierarchiqueFP() {
		return superieurHierarchiqueFP;
	}

	public void setSuperieurHierarchiqueFP(String superieurHierarchiqueFP) {
		this.superieurHierarchiqueFP = superieurHierarchiqueFP;
	}

	public String getSuperieurHierarchiqueAgent() {
		return superieurHierarchiqueAgent;
	}

	public void setSuperieurHierarchiqueAgent(String superieurHierarchiqueAgent) {
		this.superieurHierarchiqueAgent = superieurHierarchiqueAgent;
	}

	public String getRemplaceFP() {
		return remplaceFP;
	}

	public void setRemplaceFP(String remplaceFP) {
		this.remplaceFP = remplaceFP;
	}

	public String getRemplaceAgent() {
		return remplaceAgent;
	}

	public void setRemplaceAgent(String remplaceAgent) {
		this.remplaceAgent = remplaceAgent;
	}

	public String getEmploiPrimaire() {
		return emploiPrimaire;
	}

	public void setEmploiPrimaire(String emploiPrimaire) {
		this.emploiPrimaire = emploiPrimaire;
	}

	public String getEmploiSecondaire() {
		return emploiSecondaire;
	}

	public void setEmploiSecondaire(String emploiSecondaire) {
		this.emploiSecondaire = emploiSecondaire;
	}

	public String getNFA() {
		return NFA;
	}

	public void setNFA(String nFA) {
		NFA = nFA;
	}

	public String getOPI() {
		return OPI;
	}

	public void setOPI(String oPI) {
		OPI = oPI;
	}

	public String getAnneeEmploi() {
		return anneeEmploi;
	}

	public void setAnneeEmploi(String anneeEmploi) {
		this.anneeEmploi = anneeEmploi;
	}

	public List<String> getAvantages() {
		return avantages;
	}

	public void setAvantages(List<String> avantages) {
		this.avantages = avantages;
	}

	public List<String> getDelegations() {
		return delegations;
	}

	public void setDelegations(List<String> delegations) {
		this.delegations = delegations;
	}

	public List<String> getRegimesIndemnitaires() {
		return regimesIndemnitaires;
	}

	public void setRegimesIndemnitaires(List<String> regimesIndemnitaires) {
		this.regimesIndemnitaires = regimesIndemnitaires;
	}

	public List<String> getPrimes() {
		return primes;
	}

	public void setPrimes(List<String> primes) {
		this.primes = primes;
	}

	public String getStatutFDP() {
		return statutFDP;
	}

	public void setStatutFDP(String statutFDP) {
		this.statutFDP = statutFDP;
	}

	public String getNatureCredit() {
		return natureCredit;
	}

	public void setNatureCredit(String natureCredit) {
		this.natureCredit = natureCredit;
	}

	public TitrePosteDto getTitrePoste() {
		return titrePoste;
	}

	public void setTitrePoste(TitrePosteDto titrePoste) {
		this.titrePoste = titrePoste;
	}

	public String getCodeService() {
		return codeService;
	}

	public void setCodeService(String codeService) {
		this.codeService = codeService;
	}

	public Integer getIdFichePoste() {
		return idFichePoste;
	}

	public void setIdFichePoste(Integer idFichePoste) {
		this.idFichePoste = idFichePoste;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Integer getIdServiceADS() {
		return idServiceADS;
	}

	public void setIdServiceADS(Integer idServiceADS) {
		this.idServiceADS = idServiceADS;
	}

	public List<ActiviteMetierSavoirFaire> getActiviteMetier() {
		return activiteMetier;
	}

	public void setActiviteMetier(List<ActiviteMetierSavoirFaire> activiteMetier) {
		this.activiteMetier = activiteMetier;
	}

	public List<String> getSavoirFaireMetier() {
		return savoirFaireMetier;
	}

	public void setSavoirFaireMetier(List<String> savoirFaireMetier) {
		this.savoirFaireMetier = savoirFaireMetier;
	}

	public List<String> getActiviteGenerale() {
		return activiteGenerale;
	}

	public void setActiviteGenerale(List<String> activiteGenerale) {
		this.activiteGenerale = activiteGenerale;
	}

	public List<String> getConditionExercice() {
		return conditionExercice;
	}

	public void setConditionExercice(List<String> conditionExercice) {
		this.conditionExercice = conditionExercice;
	}

	public List<String> getCompetenceManagement() {
		return competenceManagement;
	}

	public void setCompetenceManagement(List<String> competenceManagement) {
		this.competenceManagement = competenceManagement;
	}

	public String getInformationsComplementaires() {
		return informationsComplementaires;
	}

	public void setInformationsComplementaires(String informationsComplementaires) {
		this.informationsComplementaires = informationsComplementaires;
	}

}
