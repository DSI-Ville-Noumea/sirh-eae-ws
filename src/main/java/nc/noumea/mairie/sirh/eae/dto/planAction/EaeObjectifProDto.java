package nc.noumea.mairie.sirh.eae.dto.planAction;

import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

public class EaeObjectifProDto {

	private Integer	idObjectifPro;
	private String	indicateur;
	private String	objectif;

	public EaeObjectifProDto() {

	}

	public EaeObjectifProDto(EaePlanAction planAction) {
		this.idObjectifPro = planAction.getIdEaePlanAction();
		this.objectif = planAction.getObjectif();
		this.indicateur = planAction.getMesure();
	}

	public String getIndicateur() {
		return indicateur;
	}

	public void setIndicateur(String indicateur) {
		this.indicateur = indicateur;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public Integer getIdObjectifPro() {
		return idObjectifPro;
	}

	public void setIdObjectifPro(Integer idObjectifPro) {
		this.idObjectifPro = idObjectifPro;
	}

}
