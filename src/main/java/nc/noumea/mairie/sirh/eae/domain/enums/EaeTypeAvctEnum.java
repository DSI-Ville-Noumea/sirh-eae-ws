package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeAvctEnum {

	REVA("REVALORISATION", "REVA"), TITU("TITULARISATION", "TITU"), AVCT(
			"AVANCEMENT", "AVCT"), AD("AVANCEMENT DIFFERENCIE", "AD"), AUTO(
			"AUTOMATIQUE", "AUTO"), PROMO("PROMOTION", "PROMO");

	private String typeAvctLong;
	private String typeAvctCode;

	private EaeTypeAvctEnum(String _typeAvctLong, String _typeAvctCode) {
		this.typeAvctLong = _typeAvctLong;
		this.typeAvctCode = _typeAvctCode;
	}

	@Override
	public String toString() {
		return typeAvctLong + " " + typeAvctCode;
	}

	public String getTypeAvctCode() {
		return typeAvctCode;
	}

	public void setTypeAvctCode(String typeAvctCode) {
		this.typeAvctCode = typeAvctCode;
	}
}
