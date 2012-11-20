package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeNiveauEnum {

	EXCELLENT("EXCELLENT"),
	SATISFAISANT("SATISFAISANT"),
	NECESSITANT_DES_PROGRES("NECESSITANT DES PROGRES"),
	INSUFFISANT("INSUFFISANT");
	
	private String niveau;
	
	private EaeNiveauEnum(String _niveau) {
		this.niveau = _niveau;
	}
	
	@Override
	public String toString() {
		return niveau;
	}
}
