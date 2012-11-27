package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.tools.transformer.EaeEvaluateurToAgentFlatTransformer;
import nc.noumea.mairie.sirh.tools.transformer.EaeFichePosteToEaeListTransformer;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;
import nc.noumea.mairie.sirh.tools.transformer.NullableIntegerTransformer;
import nc.noumea.mairie.sirh.tools.transformer.SimpleAgentTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ValueEnumTransformer;
import flexjson.JSONSerializer;

public class EaeListItemDto {

	private Integer idEae;
	private Agent agentEvalue;
	private List<EaeEvaluateur> eaeEvaluateurs;
	private Agent agentDelegataire;
	private EaeFichePoste eaeFichePoste;
	private EaeEtatEnum etat;
	private boolean cap;
	private String avisShd;
	private boolean docAttache;
	private Date dateCreation;
	private Date dateFinalisation;
	private Date dateControle;
	private boolean droitInitialiser;
	private boolean droitAcceder;
	private boolean droitDemarrer;
	private boolean droitAffecterDelegataire;
	private boolean droitImprimer;
	private String idDocumentGed;
	
	public EaeListItemDto() {
		
	}
	
	public EaeListItemDto(Eae eaeItem) {

		this.setIdEae(eaeItem.getIdEae());
		this.setAgentDelegataire(eaeItem.getAgentDelegataire());
		this.setEaeEvaluateurs(new ArrayList<EaeEvaluateur>(eaeItem.getEaeEvaluateurs()));
		this.setEaeFichePoste(eaeItem.getPrimaryFichePoste());
		this.setEtat(eaeItem.getEtat());
		this.setCap(eaeItem.isCap());
		this.setDocAttache(eaeItem.isDocAttache());
		this.setDateCreation(eaeItem.getDateCreation());
		this.setDateFinalisation(eaeItem.getDateFinalisation());
		this.setDateControle(eaeItem.getDateControle());
		
		if (eaeItem.getEaeEvalue() != null)
			this.setAgentEvalue(eaeItem.getEaeEvalue().getAgent());
		
		if (eaeItem.getEaeEvaluation() != null)
			this.setAvisShd(eaeItem.getEaeEvaluation().getAvisShd());
		
		if (eaeItem.getEtat() == EaeEtatEnum.F) {
			this.setDroitImprimer(true);
			this.setIdDocumentGed(eaeItem.getLatestFinalisation().getIdGedDocument());
		}
	}
	
	public void setAccessRightsForAgentId(Eae eae, int idAgent) {
	
		boolean isEvaluateurOrDelegataire = eae.isEvaluateurOrDelegataire(idAgent);
		boolean isEvaluateur = eae.isEvaluateur(idAgent);
		
		switch (getEtat()) {
			case ND:
				setDroitInitialiser(isEvaluateurOrDelegataire);
				setDroitAffecterDelegataire(isEvaluateur);
				break;
				
			case C:
			case EC:
				setDroitDemarrer(isEvaluateurOrDelegataire);
				setDroitAcceder(true);
				setDroitAffecterDelegataire(isEvaluateur);
				break;
				
			case F:
				setDroitAcceder(true);
				break;
				
			default:
				break;
		}
	}

	public static JSONSerializer getSerializerForEaeListItemDto() {

		JSONSerializer serializer = new JSONSerializer()
				.include("agentEvalue")
				.include("etat")
				.include("cap")
				.include("docAttache")
				.include("dateCreation")
				.include("dateFinalisation")
				.include("dateControle")
				.include("agentDelegataire")
				.include("avisShd")
				.include("idEae")
				.include("eaeEvaluateurs")
				.include("eaeFichePoste")
				.include("droitInitialiser")
				.include("droitAcceder")
				.include("droitDemarrer")
				.include("droitAffecterDelegataire")
				.include("droitImprimer")
				.include("idDocumentGed")
				.transform(new MSDateTransformer(), Date.class)
				.transform(new NullableIntegerTransformer(), Integer.class)
				.transform(new SimpleAgentTransformer(false), Agent.class)
				.transform(new EaeEvaluateurToAgentFlatTransformer(), EaeEvaluateur.class)
				.transform(new EaeFichePosteToEaeListTransformer(), EaeFichePoste.class)
				.transform(new ValueEnumTransformer(), Enum.class).exclude("*");

		return serializer;
	}

	public Integer getIdEae() {
		return idEae;
	}

	public void setIdEae(Integer idEae) {
		this.idEae = idEae;
	}

	public Agent getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(Agent agentEvalue) {
		this.agentEvalue = agentEvalue;
	}
	
	public List<EaeEvaluateur> getEaeEvaluateurs() {
		return eaeEvaluateurs;
	}

	public void setEaeEvaluateurs(List<EaeEvaluateur> eaeEvaluateurs) {
		this.eaeEvaluateurs = eaeEvaluateurs;
	}

	public Agent getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(Agent agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
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

	public String getAvisShd() {
		return avisShd;
	}

	public void setAvisShd(String avisShd) {
		this.avisShd = avisShd;
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

	public EaeFichePoste getEaeFichePoste() {
		return eaeFichePoste;
	}

	public void setEaeFichePoste(EaeFichePoste eaeFichePoste) {
		this.eaeFichePoste = eaeFichePoste;
	}
	

	public boolean isDroitInitialiser() {
		return droitInitialiser;
	}

	public void setDroitInitialiser(boolean droitInitialiser) {
		this.droitInitialiser = droitInitialiser;
	}

	public boolean isDroitAcceder() {
		return droitAcceder;
	}

	public void setDroitAcceder(boolean droitAcceder) {
		this.droitAcceder = droitAcceder;
	}

	public boolean isDroitDemarrer() {
		return droitDemarrer;
	}

	public void setDroitDemarrer(boolean droitDemarrer) {
		this.droitDemarrer = droitDemarrer;
	}

	public boolean isDroitAffecterDelegataire() {
		return droitAffecterDelegataire;
	}

	public void setDroitAffecterDelegataire(boolean droitAffecterDelegataire) {
		this.droitAffecterDelegataire = droitAffecterDelegataire;
	}

	public boolean isDroitImprimer() {
		return droitImprimer;
	}

	public void setDroitImprimer(boolean droitImprimer) {
		this.droitImprimer = droitImprimer;
	}

	public String getIdDocumentGed() {
		return idDocumentGed;
	}

	public void setIdDocumentGed(String idDocumentGed) {
		this.idDocumentGed = idDocumentGed;
	}
}
