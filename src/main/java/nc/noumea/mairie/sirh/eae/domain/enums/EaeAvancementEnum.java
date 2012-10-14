package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAvancementEnum {
	
	MINI("Mini"),
	MOY("Moy"),
	MAXI("Maxi");
	
	private String typeAvancement;
	
	private EaeAvancementEnum(String _typeAvancement) {
		this.typeAvancement = _typeAvancement;
	}
	
	@Override
	public String toString() {
		return typeAvancement;
	}
}
