package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeDelaiEnum {

	MOINS1AN(1, "inférieur à 1 an"),
	ENTRE1ET2ANS(2, "entre 1 et 2 ans"),
	ENTRE2ET4ANS(3, "entre 2 et 4 ans");
	
	private Integer id;
	private String libelle;
	
	private EaeDelaiEnum(Integer id, String _delai) {
		this.id = id;
		this.libelle = _delai;
	}
	
	@Override
	public String toString() {
		return libelle;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}
