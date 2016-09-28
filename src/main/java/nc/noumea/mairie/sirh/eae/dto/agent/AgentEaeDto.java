package nc.noumea.mairie.sirh.eae.dto.agent;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class AgentEaeDto {

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date	dateDerniereEmbauche;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date	dateNaissance;
	private Integer	idAgent;
	private String	nomMarital;
	private String	nomPatronymique;
	private String	nomUsage;
	private Integer	nomatr;
	private String	prenom;
	private String	prenomUsage;

	public AgentEaeDto() {

	}

	public AgentEaeDto(Agent agent) {
		this();
		if (null != agent) {
			this.dateDerniereEmbauche = agent.getDateDerniereEmbauche();
			this.dateNaissance = agent.getDateNaissance();
			this.idAgent = agent.getIdAgent();
			this.nomMarital = agent.getNomMarital();
			this.nomPatronymique = agent.getNomPatronymique();
			this.nomUsage = agent.getNomUsage();
			this.nomatr = agent.getNomatr();
			this.prenom = agent.getDisplayPrenom();
			this.prenomUsage = agent.getPrenomUsage();
		}
	}

	public Date getDateDerniereEmbauche() {
		return dateDerniereEmbauche;
	}

	public void setDateDerniereEmbauche(Date dateDerniereEmbauche) {
		this.dateDerniereEmbauche = dateDerniereEmbauche;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public String getNomMarital() {
		return nomMarital;
	}

	public void setNomMarital(String nomMarital) {
		this.nomMarital = nomMarital;
	}

	public String getNomPatronymique() {
		return nomPatronymique;
	}

	public void setNomPatronymique(String nomPatronymique) {
		this.nomPatronymique = nomPatronymique;
	}

	public String getNomUsage() {
		return nomUsage;
	}

	public void setNomUsage(String nomUsage) {
		this.nomUsage = nomUsage;
	}

	public Integer getNomatr() {
		return nomatr;
	}

	public void setNomatr(Integer nomatr) {
		this.nomatr = nomatr;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPrenomUsage() {
		return prenomUsage;
	}

	public void setPrenomUsage(String prenomUsage) {
		this.prenomUsage = prenomUsage;
	}

}
