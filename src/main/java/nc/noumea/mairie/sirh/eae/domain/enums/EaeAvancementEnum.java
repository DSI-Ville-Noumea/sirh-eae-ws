package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeAvancementEnum {
	
	MINI("Durée minimale"),
	MOY("Durée moyenne"),
	MAXI("Durée maximale");
	
	private String typeAvancement;
	
	private EaeAvancementEnum(String _typeAvancement) {
		this.typeAvancement = _typeAvancement;
	}
	
	@Override
	public String toString() {
		return typeAvancement;
	}
}
