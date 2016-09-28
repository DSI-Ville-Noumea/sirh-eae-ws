package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.dto.agent.AgentDto;

public class FinalizationInformationDto {

	private int idEae;
	private int annee;
	
	private AgentDto agentEvalue;
	private AgentDto agentDelegataire;
	private List<AgentDto> agentsEvaluateurs;
	private List<AgentDto> agentsShd;
	private Float noteAnnee;

	public FinalizationInformationDto() {
		agentsEvaluateurs = new ArrayList<AgentDto>();
		agentsShd = new ArrayList<AgentDto>();
	}

	public FinalizationInformationDto(Eae eae) {
		this();
		idEae = eae.getIdEae();
		annee = eae.getEaeCampagne().getAnnee();
		agentEvalue = null == eae.getEaeEvalue().getAgent() ? null : new AgentDto(eae.getEaeEvalue().getAgent());
		agentDelegataire = null == eae.getEaeEvalue().getAgent() ? null : new AgentDto(eae.getAgentDelegataire());

		for (EaeEvaluateur eval : eae.getEaeEvaluateurs()) {
			agentsEvaluateurs.add(new AgentDto(eval.getAgent()));
		}
		EaeEvaluation eaeEvaluation = eae.getEaeEvaluation();
		noteAnnee = eaeEvaluation.getNoteAnnee();
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
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

	public List<AgentDto> getAgentsEvaluateurs() {
		return agentsEvaluateurs;
	}

	public void setAgentsEvaluateurs(List<AgentDto> agentsEvaluateurs) {
		this.agentsEvaluateurs = agentsEvaluateurs;
	}

	public List<AgentDto> getAgentsShd() {
		return agentsShd;
	}

	public void setAgentsShd(List<AgentDto> agentsShd) {
		this.agentsShd = agentsShd;
	}

	public Float getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Float noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

}
