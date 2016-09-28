package nc.noumea.mairie.sirh.eae.dto;

import java.util.List;

public class FormRehercheGestionEae {

	private Integer idCampagneEae;
	private String etat;
	private String statut;
	private List<Integer> listeSousService;
	private Boolean cap;
	private Integer idAgentEvaluateur; 
	private Integer idAgentEvalue;
	private Boolean isEstDetache;
	
	public Integer getIdCampagneEae() {
		return idCampagneEae;
	}
	public void setIdCampagneEae(Integer idCampagneEae) {
		this.idCampagneEae = idCampagneEae;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	public String getStatut() {
		return statut;
	}
	public void setStatut(String statut) {
		this.statut = statut;
	}
	public List<Integer> getListeSousService() {
		return listeSousService;
	}
	public void setListeSousService(List<Integer> listeSousService) {
		this.listeSousService = listeSousService;
	}
	public Boolean getCap() {
		return cap;
	}
	public void setCap(Boolean cap) {
		this.cap = cap;
	}
	public Integer getIdAgentEvaluateur() {
		return idAgentEvaluateur;
	}
	public void setIdAgentEvaluateur(Integer idAgentEvaluateur) {
		this.idAgentEvaluateur = idAgentEvaluateur;
	}
	public Integer getIdAgentEvalue() {
		return idAgentEvalue;
	}
	public void setIdAgentEvalue(Integer idAgentEvalue) {
		this.idAgentEvalue = idAgentEvalue;
	}
	public Boolean getIsEstDetache() {
		return isEstDetache;
	}
	public void setIsEstDetache(Boolean isEstDetache) {
		this.isEstDetache = isEstDetache;
	}
}
