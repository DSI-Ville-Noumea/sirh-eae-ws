package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAvisChangementClasse {
	
	FAVORABLE("Favorable"),
	DEFAVORABLE("Défavorable");
	
	private String avisAvancement;
	
	private EaeAvisChangementClasse(String _avisAvancement) {
		this.avisAvancement = _avisAvancement;
	}
	
	@Override
	public String toString() {
		return avisAvancement;
	}
}
