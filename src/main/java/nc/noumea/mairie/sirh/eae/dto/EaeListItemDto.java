package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.eae.dto.agent.AgentDto;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeListItemDto {

	private Integer idEae;
	private AgentDto agentEvalue;
	private List<EaeEvaluateurDto> eaeEvaluateurs;
	private AgentDto agentDelegataire;
	private String etat;
	private boolean cap;
	private String avisShd;
	private boolean docAttache;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateCreation;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateFinalisation;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateControle;
	private boolean droitInitialiser;
	private boolean droitAcceder;
	private boolean droitDemarrer;
	private boolean droitAffecterDelegataire;
	private boolean droitImprimerBirt;
	private boolean droitImprimerGed;
	private String idDocumentGed;
	private boolean estDetache;
	
	private String directionService;
	private String sectionService;
	private String service;
	
	private AgentDto agentShd;
	
	public EaeListItemDto() {
		this.eaeEvaluateurs = new ArrayList<EaeEvaluateurDto>();
	}
	
	public EaeListItemDto(Eae eaeItem) {
		this();
		this.setIdEae(eaeItem.getIdEae());
		this.setAgentDelegataire(null == eaeItem.getAgentDelegataire() ? null : new AgentDto(eaeItem.getAgentDelegataire()));
		if(null != eaeItem.getEaeEvaluateurs()) {
			for(EaeEvaluateur eaeEval : eaeItem.getEaeEvaluateurs()) {
				this.eaeEvaluateurs.add(new EaeEvaluateurDto(eaeEval));
			}
		}
		this.setEtat(eaeItem.getEtat().name());
		this.setCap(eaeItem.isCap());
		this.setDocAttache(eaeItem.isDocAttache());
		this.setDateCreation(eaeItem.getDateCreation());
		this.setDateFinalisation(eaeItem.getDateFinalisation());
		this.setDateControle(eaeItem.getDateControle());
		
		EaeFichePoste fp = eaeItem.getPrimaryFichePoste();
		if (fp != null) {
			this.directionService = fp.getDirectionService();
			this.sectionService = fp.getSectionService();
			this.service = fp.getService();
			this.agentShd = null == fp.getAgentShd() ? null : new AgentDto(fp.getAgentShd());
		}
		
		if (eaeItem.getEaeEvalue() != null) {
			this.setAgentEvalue(null == eaeItem.getEaeEvalue().getAgent() ? null : new AgentDto(eaeItem.getEaeEvalue().getAgent()));
			estDetache = eaeItem.getEaeEvalue().isEstDetache();
		}
		
		if (eaeItem.getEaeEvaluation() != null)
			this.setAvisShd(eaeItem.getEaeEvaluation().getAvisShd());
		
		if (eaeItem.getEtat() == EaeEtatEnum.F || eaeItem.getEtat() == EaeEtatEnum.CO ) {
			this.setDroitImprimerGed(true);
			this.setIdDocumentGed(eaeItem.getLatestFinalisation().getNodeRefAlfresco());
		}
		
		this.setDroitImprimerBirt(eaeItem.getEtat() == EaeEtatEnum.C || eaeItem.getEtat() == EaeEtatEnum.EC);
	}
	
	public void setAccessRightsForAgentId(Eae eae, int idAgent) {
	
		boolean isEvaluateurOrDelegataire = eae.isEvaluateurOrDelegataire(idAgent);
		boolean isEvaluateur = eae.isEvaluateur(idAgent);
		
		switch (EaeEtatEnum.valueOf(getEtat())) {
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

	public Integer getIdEae() {
		return idEae;
	}

	public void setIdEae(Integer idEae) {
		this.idEae = idEae;
	}

	public List<EaeEvaluateurDto> getEaeEvaluateurs() {
		return eaeEvaluateurs;
	}

	public void setEaeEvaluateurs(List<EaeEvaluateurDto> eaeEvaluateurs) {
		this.eaeEvaluateurs = eaeEvaluateurs;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
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

	public boolean isDroitImprimerBirt() {
		return droitImprimerBirt;
	}

	public void setDroitImprimerBirt(boolean droitImprimerBirt) {
		this.droitImprimerBirt = droitImprimerBirt;
	}

	public boolean isDroitImprimerGed() {
		return droitImprimerGed;
	}

	public void setDroitImprimerGed(boolean droitImprimerGed) {
		this.droitImprimerGed = droitImprimerGed;
	}

	public String getIdDocumentGed() {
		return idDocumentGed;
	}

	public void setIdDocumentGed(String idDocumentGed) {
		this.idDocumentGed = idDocumentGed;
	}

	public boolean isEstDetache() {
		return estDetache;
	}

	public void setEstDetache(boolean estDetache) {
		this.estDetache = estDetache;
	}

	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getSectionService() {
		return sectionService;
	}

	public void setSectionService(String sectionService) {
		this.sectionService = sectionService;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public AgentDto getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(AgentDto agentEvalue) {
		this.agentEvalue = agentEvalue;
	}

	public AgentDto getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(AgentDto agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}

	public AgentDto getAgentShd() {
		return agentShd;
	}

	public void setAgentShd(AgentDto agentShd) {
		this.agentShd = agentShd;
	}
	
}
