package nc.noumea.mairie.sirh.eae.dto.agent;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;


public class AutreAdministrationAgentDto {

	private Integer idAutreAdmin;
	private Integer idAgent;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntree;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateSortie;
	private Integer fonctionnaire;
	private String libelleAdministration;
	
	public Integer getIdAutreAdmin() {
		return idAutreAdmin;
	}
	public void setIdAutreAdmin(Integer idAutreAdmin) {
		this.idAutreAdmin = idAutreAdmin;
	}
	public Integer getIdAgent() {
		return idAgent;
	}
	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}
	public Date getDateEntree() {
		return dateEntree;
	}
	public void setDateEntree(Date dateEntree) {
		this.dateEntree = dateEntree;
	}
	public Date getDateSortie() {
		return dateSortie;
	}
	public void setDateSortie(Date dateSortie) {
		this.dateSortie = dateSortie;
	}
	public Integer getFonctionnaire() {
		return fonctionnaire;
	}
	public void setFonctionnaire(Integer fonctionnaire) {
		this.fonctionnaire = fonctionnaire;
	}
	public String getLibelleAdministration() {
		return libelleAdministration;
	}
	public void setLibelleAdministration(String libelleAdministration) {
		this.libelleAdministration = libelleAdministration;
	}
	
	
}
