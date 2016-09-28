package nc.noumea.mairie.sirh.eae.dto;

import java.io.Serializable;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagneActeurs;


public class EaeCampagneActeursDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1087328007993526302L;
	
	private Integer idCampagneActeurs;
	private Integer idAgent;
	
	public EaeCampagneActeursDto() {
		
	}
	
	public EaeCampagneActeursDto(EaeCampagneActeurs acteurs) {
		this.idCampagneActeurs = acteurs.getIdCampagneActeurs();
		this.idAgent = acteurs.getIdAgent();
	}

	public Integer getIdCampagneActeurs() {
		return idCampagneActeurs;
	}

	public void setIdCampagneActeurs(Integer idCampagneActeurs) {
		this.idCampagneActeurs = idCampagneActeurs;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}
}
