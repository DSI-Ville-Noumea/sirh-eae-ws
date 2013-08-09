package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.tools.transformer.SimpleAgentTransformer;
import flexjson.JSONSerializer;

public class FinalizationInformationDto implements IJSONSerialize {

	private int idEae;
	private int annee;
	private Agent agentEvalue;
	private Agent agentDelegataire;
	private List<Agent> agentsEvaluateurs;
	private List<Agent> agentsShd;
	private Float noteAnnee;

	public FinalizationInformationDto() {
		agentsEvaluateurs = new ArrayList<Agent>();
		agentsShd = new ArrayList<Agent>();
	}

	public FinalizationInformationDto(Eae eae) {
		this();
		idEae = eae.getIdEae();
		annee = eae.getEaeCampagne().getAnnee();
		agentEvalue = eae.getEaeEvalue().getAgent();
		agentDelegataire = eae.getAgentDelegataire();

		for (EaeEvaluateur eval : eae.getEaeEvaluateurs()) {
			agentsEvaluateurs.add(eval.getAgent());
		}
		EaeEvaluation eaeEvaluation = eae.getEaeEvaluation();
		noteAnnee = eaeEvaluation.getNoteAnnee();
	}

	public static JSONSerializer getSerializerForFinalizationInformationDto() {
		return new JSONSerializer().exclude("*.class").include("*").transform(new SimpleAgentTransformer(), Agent.class);
	}

	@Override
	public String serializeInJSON() {
		return getSerializerForFinalizationInformationDto().serialize(this);
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

	public Agent getAgentEvalue() {
		return agentEvalue;
	}

	public void setAgentEvalue(Agent agentEvalue) {
		this.agentEvalue = agentEvalue;
	}

	public Agent getAgentDelegataire() {
		return agentDelegataire;
	}

	public void setAgentDelegataire(Agent agentDelegataire) {
		this.agentDelegataire = agentDelegataire;
	}

	public List<Agent> getAgentsEvaluateurs() {
		return agentsEvaluateurs;
	}

	public void setAgentsEvaluateurs(List<Agent> agentsEvaluateurs) {
		this.agentsEvaluateurs = agentsEvaluateurs;
	}

	public List<Agent> getAgentsShd() {
		return agentsShd;
	}

	public void setAgentsShd(List<Agent> agentsShd) {
		this.agentsShd = agentsShd;
	}

	public Float getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Float noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

}
