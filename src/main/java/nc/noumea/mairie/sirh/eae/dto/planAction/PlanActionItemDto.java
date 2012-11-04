package nc.noumea.mairie.sirh.eae.dto.planAction;

import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

public class PlanActionItemDto {

	private String objectif;
	private String indicateur;

	public PlanActionItemDto() {
		
	}
	
	public PlanActionItemDto(EaePlanAction planAction) {
		objectif = planAction.getObjectif();
		indicateur = planAction.getMesure();
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getIndicateur() {
		return indicateur;
	}

	public void setIndicateur(String indicateur) {
		this.indicateur = indicateur;
	}
}
