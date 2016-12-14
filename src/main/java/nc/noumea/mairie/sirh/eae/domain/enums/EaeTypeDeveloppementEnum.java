package nc.noumea.mairie.sirh.eae.domain.enums;

/**
 * Les identifiants doivent correspondre à ceux de la base de donnée (table EAE_TYPE_DEVELOPPEMENT)
 */
public enum EaeTypeDeveloppementEnum {

	CONNAISSANCE(1, "Connaissance"),
	COMPETENCE(2, "Compétence"),
	CONCOURS(3, "Concours"),
	PERSONNEL(4, "Personnel"),
	COMPORTEMENT(5, "Comportement"),
	FORMATEUR(6, "Formateur");

	private Integer id;
	private String type;
	
	private EaeTypeDeveloppementEnum(Integer id, String _type) {
		this.id = id;
		this.type = _type;
	}
	
	@Override
	public String toString() {
		return id + " - " + type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
