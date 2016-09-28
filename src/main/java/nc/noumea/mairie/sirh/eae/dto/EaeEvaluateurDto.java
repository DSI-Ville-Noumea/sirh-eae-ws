package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.dto.agent.AgentDto;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeEvaluateurDto {
	
	private Integer idEaeEvaluateur;
	private int idAgent;
	private String fonction;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntreeService;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntreeCollectivite;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntreeFonction;
	private AgentDto agent;
	
	public EaeEvaluateurDto() {
	}
	
	public EaeEvaluateurDto(EaeEvaluateur eaeEval) {
		this.idEaeEvaluateur = eaeEval.getIdEaeEvaluateur();
		this.idAgent = eaeEval.getIdAgent();
		this.fonction = eaeEval.getFonction();
		this.dateEntreeService = eaeEval.getDateEntreeService();
		this.dateEntreeCollectivite = eaeEval.getDateEntreeCollectivite();
		this.dateEntreeFonction = eaeEval.getDateEntreeFonction();
		this.agent = null == eaeEval.getAgent() ? null : new AgentDto(eaeEval.getAgent());
	}
	
	public Integer getIdEaeEvaluateur() {
		return idEaeEvaluateur;
	}
	public void setIdEaeEvaluateur(Integer idEaeEvaluateur) {
		this.idEaeEvaluateur = idEaeEvaluateur;
	}
	public int getIdAgent() {
		return idAgent;
	}
	public void setIdAgent(int idAgent) {
		this.idAgent = idAgent;
	}
	public String getFonction() {
		return fonction;
	}
	public void setFonction(String fonction) {
		this.fonction = fonction;
	}
	public Date getDateEntreeService() {
		return dateEntreeService;
	}
	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
	}
	public Date getDateEntreeCollectivite() {
		return dateEntreeCollectivite;
	}
	public void setDateEntreeCollectivite(Date dateEntreeCollectivite) {
		this.dateEntreeCollectivite = dateEntreeCollectivite;
	}
	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}
	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}
	public AgentDto getAgent() {
		return agent;
	}
	public void setAgent(AgentDto agent) {
		this.agent = agent;
	}
}
