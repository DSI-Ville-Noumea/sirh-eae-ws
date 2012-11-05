package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ObjectToPropertyTransformer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class EaeEvolutionDto implements IJSONDeserialize<EaeEvolutionDto>, IJSONSerialize {

	private int idEae;
	private boolean mobiliteGeo;
	private boolean mobiliteFonctionnelle;
	private boolean changementMetier;
	private ValueWithListDto delaiEnvisage;
	private boolean mobiliteService;
	private boolean mobiliteDirection;
	private boolean mobiliteCollectivite;
	private String nomCollectivite;
	private boolean mobiliteAutre;
	private boolean concours;
	private String nomConcours;
	private boolean vae;
	private String nomDiplome;
	private boolean tempsPartiel;
	private int pourcentageTempsPartiel;
	private boolean retraite;
	private Date dateRetraite;
	private boolean autrePerspective;
	private String libelleAutrePerspective;
	private EaeCommentaire commentaireEvolution;
	private EaeCommentaire commentaireEvaluateur;
	private EaeCommentaire commentaireEvalue;
	
	private List<EaeEvolutionSouhait> souhaitsSuggestions;
	private List<EaeDeveloppement> connaissances;
	private List<EaeDeveloppement> competences;
	private List<EaeDeveloppement> examensConcours;
	private List<EaeDeveloppement> personnel;
	private List<EaeDeveloppement> comportement;
	private List<EaeDeveloppement> formateur;
	
	public EaeEvolutionDto() {
		souhaitsSuggestions = new ArrayList<EaeEvolutionSouhait>();
		connaissances = new ArrayList<EaeDeveloppement>();
		competences = new ArrayList<EaeDeveloppement>();
		examensConcours = new ArrayList<EaeDeveloppement>();
		personnel = new ArrayList<EaeDeveloppement>();
		comportement = new ArrayList<EaeDeveloppement>();
		formateur = new ArrayList<EaeDeveloppement>();
		delaiEnvisage = new ValueWithListDto(null, EaeDelaiEnum.class);
	}

	public EaeEvolutionDto(Eae eae) {
		this();
		idEae = eae.getIdEae();
		
		EaeEvolution evolution = eae.getEaeEvolution();
		
		if (evolution == null)
			return;
		
		mobiliteGeo = evolution.isMobiliteGeo();
		mobiliteFonctionnelle = evolution.isMobiliteFonctionnelle();
		changementMetier = evolution.isChangementMetier();
		delaiEnvisage = new ValueWithListDto(evolution.getDelaiEnvisage(), EaeDelaiEnum.class);
		mobiliteService = evolution.isMobiliteService();
		mobiliteDirection = evolution.isMobiliteDirection();
		mobiliteCollectivite = evolution.isMobiliteCollectivite();
		nomCollectivite = evolution.getNomCollectivite();
		mobiliteAutre = evolution.isMobiliteAutre();
		concours = evolution.isConcours();
		nomConcours = evolution.getNomConcours();
		vae = evolution.isVae();
		nomDiplome = evolution.getNomVae();
		tempsPartiel = evolution.isTempsPartiel();
		pourcentageTempsPartiel = evolution.getPourcentageTempsParciel();
		retraite = evolution.isRetraite();
		dateRetraite = evolution.getDateRetraite();
		autrePerspective = evolution.isAutrePerspective();
		libelleAutrePerspective = evolution.getLibelleAutrePerspective();
		commentaireEvolution = evolution.getCommentaireEvolution();
		commentaireEvaluateur = evolution.getCommentaireEvaluateur();
		commentaireEvalue = evolution.getCommentaireEvalue();
		
		for (EaeEvolutionSouhait souhait : evolution.getEaeEvolutionSouhaits()) {
			souhaitsSuggestions.add(souhait);
		}
		
		fillInDeveloppements(evolution.getEaeDeveloppements());
	}
	
	private void fillInDeveloppements(Set<EaeDeveloppement> developpements) {
		
		for (EaeDeveloppement dev : developpements) {
			switch(dev.getTypeDeveloppement()) {
				case CONNAISSANCE:
					connaissances.add(dev);
					break;
				case COMPETENCE:
					competences.add(dev);
					break;
				case CONCOURS:
					examensConcours.add(dev);
					break;
				case PERSONNEL:
					personnel.add(dev);
					break;
				case COMPORTEMENT:
					comportement.add(dev);
					break;
				case FORMATEUR:
					formateur.add(dev);
					break;
			}
		}
	}

	public static JSONSerializer getSerializerForEaeEvolutionDto() {
		return new JSONSerializer()
			.exclude("*.class")
			.include("idEae")
			.include("mobiliteGeo")
			.include("mobiliteFonctionnelle")
			.include("changementMetier")
			.include("delaiEnvisage.*")
			.include("mobiliteService")
			.include("mobiliteDirection")
			.include("mobiliteCollectivite")
			.include("nomCollectivite")
			.include("mobiliteAutre")
			.include("concours")
			.include("nomConcours")
			.include("vae")
			.include("nomDiplome")
			.include("tempsPartiel")
			.include("pourcentageTempsPartiel")
			.include("retraite")
			.include("dateRetraite")
			.include("autrePerspective")
			.include("libelleAutrePerspective")
			.include("souhaitsSuggestions.idEaeEvolutionSouhait")
			.include("souhaitsSuggestions.souhait")
			.include("souhaitsSuggestions.suggestion")
			.include("connaissances.idEaeDeveloppement")
			.include("connaissances.libelle")
			.include("connaissances.echeance")
			.include("connaissances.priorisation")
			.include("competences.idEaeDeveloppement")
			.include("competences.libelle")
			.include("competences.echeance")
			.include("competences.priorisation")
			.include("examensConcours.idEaeDeveloppement")
			.include("examensConcours.libelle")
			.include("examensConcours.echeance")
			.include("examensConcours.priorisation")
			.include("personnel.idEaeDeveloppement")
			.include("personnel.libelle")
			.include("personnel.echeance")
			.include("personnel.priorisation")
			.include("comportement.idEaeDeveloppement")
			.include("comportement.libelle")
			.include("comportement.echeance")
			.include("comportement.priorisation")
			.include("formateur.idEaeDeveloppement")
			.include("formateur.libelle")
			.include("formateur.echeance")
			.include("formateur.priorisation")
			.include("commentaireEvolution")
			.include("commentaireEvaluateur")
			.include("commentaireEvalue")
			.exclude("*")
			.transform(new MSDateTransformer(), Date.class)
			.transform(new ObjectToPropertyTransformer("text", EaeCommentaire.class), EaeCommentaire.class);
	}
	
	@Override
	public String serializeInJSON() {
		return getSerializerForEaeEvolutionDto().serialize(this);
	}

	@Override
	public EaeEvolutionDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaeEvolutionDto>()
				.use(EaeCommentaire.class, new ObjectToPropertyTransformer("text", EaeCommentaire.class))
				.use(Date.class, new MSDateTransformer())
				.deserializeInto(json, this);
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<EaeEvolutionSouhait> getSouhaitsSuggestions() {
		return souhaitsSuggestions;
	}

	public void setSouhaitsSuggestions(
			List<EaeEvolutionSouhait> souhaitsSuggestions) {
		this.souhaitsSuggestions = souhaitsSuggestions;
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

	public ValueWithListDto getDelaiEnvisage() {
		return delaiEnvisage;
	}

	public void setDelaiEnvisage(ValueWithListDto delaiEnvisage) {
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

	public String getNomDiplome() {
		return nomDiplome;
	}

	public void setNomDiplome(String nomDiplome) {
		this.nomDiplome = nomDiplome;
	}

	public boolean isTempsPartiel() {
		return tempsPartiel;
	}

	public void setTempsPartiel(boolean tempsPartiel) {
		this.tempsPartiel = tempsPartiel;
	}

	public int getPourcentageTempsPartiel() {
		return pourcentageTempsPartiel;
	}

	public void setPourcentageTempsPartiel(int pourcentageTempsPartiel) {
		this.pourcentageTempsPartiel = pourcentageTempsPartiel;
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

	public List<EaeDeveloppement> getConnaissances() {
		return connaissances;
	}

	public void setConnaissances(List<EaeDeveloppement> connaissances) {
		this.connaissances = connaissances;
	}

	public List<EaeDeveloppement> getCompetences() {
		return competences;
	}

	public void setCompetences(List<EaeDeveloppement> competences) {
		this.competences = competences;
	}

	public List<EaeDeveloppement> getExamensConcours() {
		return examensConcours;
	}

	public void setExamensConcours(List<EaeDeveloppement> examensConcours) {
		this.examensConcours = examensConcours;
	}

	public List<EaeDeveloppement> getPersonnel() {
		return personnel;
	}

	public void setPersonnel(List<EaeDeveloppement> personnel) {
		this.personnel = personnel;
	}

	public List<EaeDeveloppement> getComportement() {
		return comportement;
	}

	public void setComportement(List<EaeDeveloppement> comportement) {
		this.comportement = comportement;
	}

	public List<EaeDeveloppement> getFormateur() {
		return formateur;
	}

	public void setFormateur(List<EaeDeveloppement> formateur) {
		this.formateur = formateur;
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

	public EaeCommentaire getCommentaireEvolution() {
		return commentaireEvolution;
	}

	public void setCommentaireEvolution(EaeCommentaire commentaireEvolution) {
		this.commentaireEvolution = commentaireEvolution;
	}
}
