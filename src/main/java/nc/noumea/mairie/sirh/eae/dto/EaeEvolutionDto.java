package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.mairie.domain.Spbhor;
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

@XmlRootElement
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
	private String nomVae;
	private boolean tempsPartiel;
	private ValueWithListDto pourcentageTempsPartiel;
	private boolean retraite;
	private Date dateRetraite;
	private boolean autrePerspective;
	private String libelleAutrePerspective;
	private EaeCommentaire commentaireEvolution;
	private EaeCommentaire commentaireEvaluateur;
	private EaeCommentaire commentaireEvalue;
	
	private List<EaeEvolutionSouhait> souhaitsSuggestions;
	private List<EaeDeveloppement> developpementConnaissances;
	private List<EaeDeveloppement> developpementCompetences;
	private List<EaeDeveloppement> developpementExamensConcours;
	private List<EaeDeveloppement> developpementPersonnel;
	private List<EaeDeveloppement> developpementComportement;
	private List<EaeDeveloppement> developpementFormateur;
	
	public EaeEvolutionDto() {
		souhaitsSuggestions = new ArrayList<EaeEvolutionSouhait>();
		developpementConnaissances = new ArrayList<EaeDeveloppement>();
		developpementCompetences = new ArrayList<EaeDeveloppement>();
		developpementExamensConcours = new ArrayList<EaeDeveloppement>();
		developpementPersonnel = new ArrayList<EaeDeveloppement>();
		developpementComportement = new ArrayList<EaeDeveloppement>();
		developpementFormateur = new ArrayList<EaeDeveloppement>();
		delaiEnvisage = new ValueWithListDto(null, EaeDelaiEnum.class);
	}

	public EaeEvolutionDto(Eae eae, List<Spbhor> tempsPartiels) {
		this();
		idEae = eae.getIdEae();
		
		EaeEvolution evolution = eae.getEaeEvolution();
		
		if (evolution == null) {
			pourcentageTempsPartiel = new ValueWithListDto(null, tempsPartiels);
			return;
		}
		
		pourcentageTempsPartiel = new ValueWithListDto(evolution.getTempsPartielIdSpbhor(), tempsPartiels);
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
		nomVae = evolution.getNomVae();
		tempsPartiel = evolution.isTempsPartiel();
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
					developpementConnaissances.add(dev);
					break;
				case COMPETENCE:
					developpementCompetences.add(dev);
					break;
				case CONCOURS:
					developpementExamensConcours.add(dev);
					break;
				case PERSONNEL:
					developpementPersonnel.add(dev);
					break;
				case COMPORTEMENT:
					developpementComportement.add(dev);
					break;
				case FORMATEUR:
					developpementFormateur.add(dev);
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
			.include("nomVae")
			.include("tempsPartiel")
			.include("pourcentageTempsPartiel.*")
			.include("retraite")
			.include("dateRetraite")
			.include("autrePerspective")
			.include("libelleAutrePerspective")
			.include("souhaitsSuggestions.idEaeEvolutionSouhait")
			.include("souhaitsSuggestions.souhait")
			.include("souhaitsSuggestions.suggestion")
			.include("developpementConnaissances.idEaeDeveloppement")
			.include("developpementConnaissances.libelle")
			.include("developpementConnaissances.echeance")
			.include("developpementConnaissances.priorisation")
			.include("developpementCompetences.idEaeDeveloppement")
			.include("developpementCompetences.libelle")
			.include("developpementCompetences.echeance")
			.include("developpementCompetences.priorisation")
			.include("developpementExamensConcours.idEaeDeveloppement")
			.include("developpementExamensConcours.libelle")
			.include("developpementExamensConcours.echeance")
			.include("developpementExamensConcours.priorisation")
			.include("developpementPersonnel.idEaeDeveloppement")
			.include("developpementPersonnel.libelle")
			.include("developpementPersonnel.echeance")
			.include("developpementPersonnel.priorisation")
			.include("developpementComportement.idEaeDeveloppement")
			.include("developpementComportement.libelle")
			.include("developpementComportement.echeance")
			.include("developpementComportement.priorisation")
			.include("developpementFormateur.idEaeDeveloppement")
			.include("developpementFormateur.libelle")
			.include("developpementFormateur.echeance")
			.include("developpementFormateur.priorisation")
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

	public ValueWithListDto getPourcentageTempsPartiel() {
		return pourcentageTempsPartiel;
	}

	public void setPourcentageTempsPartiel(
			ValueWithListDto tpsPartiel) {
		this.pourcentageTempsPartiel = tpsPartiel;
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

	public List<EaeDeveloppement> getDeveloppementConnaissances() {
		return developpementConnaissances;
	}

	public void setDeveloppementConnaissances(
			List<EaeDeveloppement> developpementConnaissances) {
		this.developpementConnaissances = developpementConnaissances;
	}

	public List<EaeDeveloppement> getDeveloppementCompetences() {
		return developpementCompetences;
	}

	public void setDeveloppementCompetences(
			List<EaeDeveloppement> developpementCompetences) {
		this.developpementCompetences = developpementCompetences;
	}

	public List<EaeDeveloppement> getDeveloppementExamensConcours() {
		return developpementExamensConcours;
	}

	public void setDeveloppementExamensConcours(
			List<EaeDeveloppement> developpementExamensConcours) {
		this.developpementExamensConcours = developpementExamensConcours;
	}

	public List<EaeDeveloppement> getDeveloppementPersonnel() {
		return developpementPersonnel;
	}

	public void setDeveloppementPersonnel(
			List<EaeDeveloppement> developpementPersonnel) {
		this.developpementPersonnel = developpementPersonnel;
	}

	public List<EaeDeveloppement> getDeveloppementComportement() {
		return developpementComportement;
	}

	public void setDeveloppementComportement(
			List<EaeDeveloppement> developpementComportement) {
		this.developpementComportement = developpementComportement;
	}

	public List<EaeDeveloppement> getDeveloppementFormateur() {
		return developpementFormateur;
	}

	public void setDeveloppementFormateur(
			List<EaeDeveloppement> developpementFormateur) {
		this.developpementFormateur = developpementFormateur;
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
