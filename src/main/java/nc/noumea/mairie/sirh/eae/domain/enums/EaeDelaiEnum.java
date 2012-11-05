package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeDelaiEnum {

	MOINS1AN("inférieur à 1 an"),
	ENTRE1ET2ANS("entre 1 et 2 ans"),
	ENTRE2ET4ANS("entre 2 et 4 ans");
	
	private String delai;
	
	private EaeDelaiEnum(String _delai) {
		this.delai = _delai;
	}
	
	@Override
	public String toString() {
		return delai;
	}
}
