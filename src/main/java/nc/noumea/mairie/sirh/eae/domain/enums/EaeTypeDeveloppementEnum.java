package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeDeveloppementEnum {

	CONNAISSANCE("Connaissance"),
	COMPETENCE("Compétence"),
	CONCOURS("Concours"),
	PERSONNEL("Personnel"),
	COMPORTEMENT("Comportement"),
	FORMATEUR("Formateur");
	
	private String type;
	
	private EaeTypeDeveloppementEnum(String _type) {
		this.type = _type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
