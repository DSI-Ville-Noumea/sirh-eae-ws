package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAgentPositionAdministrativeEnum {

	AC("Activité"),
	MD("Mise à disposition"),
	D("Détachement"),
	A("Autre");
	
	private String position;
	
	private EaeAgentPositionAdministrativeEnum(String _position) {
		this.position = _position;
	}
	
	@Override
	public String toString() {
		return position;
	}
	
}
