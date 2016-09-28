package nc.noumea.mairie.sirh.eae.dto.identification;

public class ValeurListeDto {

	private String	code;
	private String	valeur;

	public ValeurListeDto() {

	}

	public ValeurListeDto(String pCode, String pValeur) {
		this.code = pCode;
		this.valeur = pValeur;
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
