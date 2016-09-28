package nc.noumea.mairie.sirh.eae.dto;

import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;

public class EaeEvolutionSouhaitDto {

	private Integer idEaeEvolutionSouhait;
	private String souhait;
	private String suggestion;
	
	public EaeEvolutionSouhaitDto(){
		
	}
	
	public EaeEvolutionSouhaitDto(EaeEvolutionSouhait evolSouhait){
		this.idEaeEvolutionSouhait = evolSouhait.getIdEaeEvolutionSouhait();
		this.souhait = evolSouhait.getSouhait();
		this.suggestion = evolSouhait.getSuggestion();
	}
	
	public Integer getIdEaeEvolutionSouhait() {
		return idEaeEvolutionSouhait;
	}
	public void setIdEaeEvolutionSouhait(Integer idEaeEvolutionSouhait) {
		this.idEaeEvolutionSouhait = idEaeEvolutionSouhait;
	}
	public String getSouhait() {
		return souhait;
	}
	public void setSouhait(String souhait) {
		this.souhait = souhait;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
}
