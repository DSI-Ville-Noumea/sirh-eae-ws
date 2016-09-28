package nc.noumea.mairie.sirh.eae.dto.planAction;

import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

public class EaeItemPlanActionDto {

	private Integer idItemPlanAction;
	private String libelle;

	public EaeItemPlanActionDto() {
		
	}
	
	public EaeItemPlanActionDto(EaePlanAction planAction) {
		this.idItemPlanAction = planAction.getIdEaePlanAction();
		this.libelle = planAction.getObjectif();
	}
	
	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Integer getIdItemPlanAction() {
		return idItemPlanAction;
	}

	public void setIdItemPlanAction(Integer idItemPlanAction) {
		this.idItemPlanAction = idItemPlanAction;
	}
	
}
