package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeTypeAppreciationEnum {

	TE("Technique"),
	SE("Savoir être"),
	MA("Managerial"),
	RE("Resultats");
	
	private String note;
	
	private EaeTypeAppreciationEnum(String _note) {
		this.note = _note;
	}
	
	@Override
	public String toString() {
		return note;
	}
}
