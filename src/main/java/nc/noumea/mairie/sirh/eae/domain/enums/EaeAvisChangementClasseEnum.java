package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAvisChangementClasseEnum {
	
	FAVORABLE("Favorable"),
	DEFAVORABLE("Défavorable");
	
	private String avisAvancement;
	
	private EaeAvisChangementClasseEnum(String _avisAvancement) {
		this.avisAvancement = _avisAvancement;
	}
	
	@Override
	public String toString() {
		return avisAvancement;
	}
}
