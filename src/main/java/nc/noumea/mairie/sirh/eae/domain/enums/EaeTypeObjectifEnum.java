package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeObjectifEnum {

	PROFESSIONNEL("Professionnel"),
	INDIVIDUEL("Individuel");
	
	private String type;
	
	private EaeTypeObjectifEnum(String _type) {
		this.type = _type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
}
