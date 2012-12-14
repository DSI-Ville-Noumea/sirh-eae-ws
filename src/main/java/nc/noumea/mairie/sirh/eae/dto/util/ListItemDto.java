package nc.noumea.mairie.sirh.eae.dto.util;

public class ListItemDto {
	
	private String code;
	private String valeur;
	
	public ListItemDto() {
		
	}
	
	public ListItemDto(String code, String valeur) {
		this.code = code;
		this.valeur = valeur;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValeur() {
		return valeur;
	}
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
}
