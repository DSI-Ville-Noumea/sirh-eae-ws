package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeObjectifEnum {

	PROFESSIONNEL("Professionnel"),
	INDIVIDUEL("Individuel"),
	MATERIELS("Matériels"),
	FINANCIERS("Financiers"),
	AUTRES("Autres");
	
	private String type;
	
	private EaeTypeObjectifEnum(String _type) {
		this.type = _type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
}
