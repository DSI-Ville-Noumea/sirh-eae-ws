package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAgentStatutEnum {
	
	F("Fonctionnaire"),
	C("Contractuel"),
	CC("Convention collective"),
	AL("Allocataire"),
	A("Autre");
	
	private String statut;
	
	private EaeAgentStatutEnum(String _statut) {
		this.statut = _statut;
	}
	
	@Override
	public String toString() {
		return statut;
	}
}
