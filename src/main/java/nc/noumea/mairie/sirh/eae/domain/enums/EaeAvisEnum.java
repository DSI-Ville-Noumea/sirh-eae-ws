package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAvisEnum {
	
	FAVORABLE("Favorable"),
	DEFAVORABLE("Défavorable");
	
	private String avisAvancement;
	
	private EaeAvisEnum(String _avisAvancement) {
		this.avisAvancement = _avisAvancement;
	}
	
	@Override
	public String toString() {
		return avisAvancement;
	}
}
