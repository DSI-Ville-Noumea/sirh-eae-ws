package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeCompetenceEnum {

	SA("Savoir"),
	SF("Savoir faire"),
	CP("Compétence professionnelle");
	
	private String type;
	
	private EaeTypeCompetenceEnum(String _type) {
		this.type = _type;
	}
	
	@Override
	public String toString() {
		return type;
	}
}
