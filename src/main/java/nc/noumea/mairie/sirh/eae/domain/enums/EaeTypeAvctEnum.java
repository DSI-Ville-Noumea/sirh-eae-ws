package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeAvctEnum {

	REVA("REVALORISATION"),
	TITU("TITULARISATION"),
	AVCT("AVANCEMENT"),
	AD("AVANCEMENT DIFFERENCIE"),
	AUTO("AUTOMATIQUE"),
	PROMO("PROMOTION");
	
	private String typeAvct;
	
	private EaeTypeAvctEnum(String _typeAvct) {
		this.typeAvct = _typeAvct;
	}
	
	@Override
	public String toString() {
		return typeAvct;
	}
}
