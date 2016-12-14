package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeDeveloppementDto {

	private Integer idEaeDeveloppement;
	private String libelle;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date echeance;
	private int priorisation;
	private String typeDeveloppement;
	
	public EaeDeveloppementDto() {
		
	}
	
	public EaeDeveloppementDto(EaeDeveloppement eaeDev) {
		this.idEaeDeveloppement = eaeDev.getIdEaeDeveloppement();
		this.libelle = eaeDev.getLibelle();
		this.echeance = eaeDev.getEcheance();
		this.priorisation = eaeDev.getPriorisation();
		this.typeDeveloppement = eaeDev.getTypeDeveloppement().getLibelle();
	}
	
	public Integer getIdEaeDeveloppement() {
		return idEaeDeveloppement;
	}
	public void setIdEaeDeveloppement(Integer idEaeDeveloppement) {
		this.idEaeDeveloppement = idEaeDeveloppement;
	}
	
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	
	public Date getEcheance() {
		return echeance;
	}
	public void setEcheance(Date echeance) {
		this.echeance = echeance;
	}
	
	public int getPriorisation() {
		return priorisation;
	}
	public void setPriorisation(int priorisation) {
		this.priorisation = priorisation;
	}
	
	public String getTypeDeveloppement() {
		return typeDeveloppement;
	}
	public void setTypeDeveloppement(String typeDeveloppement) {
		this.typeDeveloppement = typeDeveloppement;
	}
	
	
}
