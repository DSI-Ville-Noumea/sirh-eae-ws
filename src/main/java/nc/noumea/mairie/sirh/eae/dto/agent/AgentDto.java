package nc.noumea.mairie.sirh.eae.dto.agent;

import nc.noumea.mairie.sirh.domain.Agent;

public class AgentDto {

	private String nom;
	private String prenom;
	private Integer idAgent;
	private String civilite;

	// Pour la gestion des droits des absences
	private boolean selectedDroitAbs;

	public AgentDto() {

	}
	
	public AgentDto(Agent agent) {
		this.nom = agent.getDisplayNom();
		this.prenom = agent.getDisplayPrenom();
		this.idAgent = agent.getIdAgent();
	}

	public AgentDto(AgentWithServiceDto agDto) {
		this.nom = agDto.getNom();
		this.prenom = agDto.getPrenom();
		this.idAgent = agDto.getIdAgent();
		this.civilite = agDto.getCivilite();
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return idAgent.equals(((AgentDto) obj).getIdAgent());
	}

	public String getCivilite() {
		return civilite;
	}

	public void setCivilite(String civilite) {
		this.civilite = civilite;
	}

	public void setSelectedDroitAbs(boolean contains) {
		this.selectedDroitAbs = contains;
	}

	public boolean isSelectedDroitAbs() {
		return selectedDroitAbs;
	}
}
