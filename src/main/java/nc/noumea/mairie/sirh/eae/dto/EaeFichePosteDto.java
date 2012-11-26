package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpCompetence;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import flexjson.JSONSerializer;

public class EaeFichePosteDto implements IJSONSerialize {

	private int idEae;
	private String intitule;
	private String grade;
	private String emploi;
	private String directionService;
	private String localisation;
	private String missions;
	private String responsableNom;
	private String responsablePrenom;
	private String responsableFonction;
	private List<String> activites;
	private List<String> competencesSavoir;
	private List<String> competencesSavoirFaire;
	private List<String> competencesComportementProfessionnel;

	public EaeFichePosteDto() {
		activites = new ArrayList<String>();
		competencesSavoir = new ArrayList<String>();
		competencesSavoirFaire = new ArrayList<String>();
		competencesComportementProfessionnel = new ArrayList<String>();
	}

	public EaeFichePosteDto(EaeFichePoste fdp) {
		this();
		idEae = fdp.getEae().getIdEae();
		intitule = fdp.getFonction();
		grade = fdp.getGradePoste();
		emploi = fdp.getEmploi();
		directionService = fdp.getDirectionService();
		localisation = fdp.getLocalisation();
		missions = fdp.getMissions();
		responsableFonction = fdp.getFonctionResponsable();
		
		if (fdp.getAgentShd() != null) {
			responsableNom = fdp.getAgentShd().getDisplayNom();
			responsablePrenom = fdp.getAgentShd().getDisplayPrenom();
		}
		
		for(EaeFdpCompetence comp : fdp.getEaeFdpCompetences()) {
			switch(comp.getType()) {
				case SA:
					competencesSavoir.add(comp.getLibelle());
					break;
				case SF:
					competencesSavoirFaire.add(comp.getLibelle());
					break;
				case CP:
					competencesComportementProfessionnel.add(comp.getLibelle());
					break;
			}
		}
		
		for (EaeFdpActivite act : fdp.getEaeFdpActivites()) {
			activites.add(act.getLibelle());
		}
	}

	public static JSONSerializer getSerializerForEaeFichePosteDto() {
		
		return new JSONSerializer()
			.include("idEae")
			.include("intitule")
			.include("grade")
			.include("emploi")
			.include("directionService")
			.include("localisation")
			.include("missions")
			.include("responsableNom")
			.include("responsablePrenom")
			.include("responsableFonction")
			.include("activites.*")
			.include("competencesSavoir.*")
			.include("competencesSavoirFaire.*")
			.include("competencesComportementProfessionnel.*")
			.exclude("*");
	}
	
	@Override
	public String serializeInJSON() {
		return getSerializerForEaeFichePosteDto().serialize(this);
	}
	
	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getEmploi() {
		return emploi;
	}

	public void setEmploi(String emploi) {
		this.emploi = emploi;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getLocalisation() {
		return localisation;
	}

	public void setLocalisation(String localisation) {
		this.localisation = localisation;
	}

	public String getMissions() {
		return missions;
	}

	public void setMissions(String missions) {
		this.missions = missions;
	}

	public String getResponsableNom() {
		return responsableNom;
	}

	public void setResponsableNom(String responsableNom) {
		this.responsableNom = responsableNom;
	}

	public String getResponsablePrenom() {
		return responsablePrenom;
	}

	public void setResponsablePrenom(String responsablePrenom) {
		this.responsablePrenom = responsablePrenom;
	}

	public String getResponsableFonction() {
		return responsableFonction;
	}

	public void setResponsableFonction(String responsableFonction) {
		this.responsableFonction = responsableFonction;
	}

	public List<String> getCompetencesSavoir() {
		return competencesSavoir;
	}

	public List<String> getActivites() {
		return activites;
	}

	public void setActivites(List<String> activites) {
		this.activites = activites;
	}

	public void setCompetencesSavoir(List<String> competencesSavoir) {
		this.competencesSavoir = competencesSavoir;
	}

	public List<String> getCompetencesSavoirFaire() {
		return competencesSavoirFaire;
	}

	public void setCompetencesSavoirFaire(List<String> competencesSavoirFaire) {
		this.competencesSavoirFaire = competencesSavoirFaire;
	}

	public List<String> getCompetencesComportementProfessionnel() {
		return competencesComportementProfessionnel;
	}

	public void setCompetencesComportementProfessionnel(
			List<String> competencesComportementProfessionnel) {
		this.competencesComportementProfessionnel = competencesComportementProfessionnel;
	}
}
