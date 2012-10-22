package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpCompetence;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.tools.transformer.ObjectToPropertyTransformer;
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
	private List<EaeFdpActivite> activites;
	private List<EaeFdpCompetence> competencesRequises;

	public EaeFichePosteDto() {
		activites = new ArrayList<EaeFdpActivite>();
		competencesRequises = new ArrayList<EaeFdpCompetence>();
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
		responsableNom = fdp.getAgentShd().getDisplayNom();
		responsablePrenom = fdp.getAgentShd().getDisplayPrenom();
		responsableFonction = fdp.getFonctionResponsable();
		activites.addAll(fdp.getEaeFdpActivites());
		competencesRequises.addAll(fdp.getEaeFdpCompetences());
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
			.include("activites")
			.include("competencesRequises")
			.transform(new ObjectToPropertyTransformer("libelle", EaeFdpActivite.class), EaeFdpActivite.class)
			.transform(new ObjectToPropertyTransformer("fullLabel", EaeFdpCompetence.class), EaeFdpCompetence.class)
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

	public List<EaeFdpActivite> getActivites() {
		return activites;
	}

	public void setActivites(List<EaeFdpActivite> activites) {
		this.activites = activites;
	}

	public List<EaeFdpCompetence> getCompetencesRequises() {
		return competencesRequises;
	}

	public void setCompetencesRequises(
			List<EaeFdpCompetence> competencesRequises) {
		this.competencesRequises = competencesRequises;
	}
}
