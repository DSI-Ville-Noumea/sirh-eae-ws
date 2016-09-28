package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EtatAvancementEnum {
	TRAVAIL("T"), SGC("C"), SEF("F"), ARRETE("A"), VALIDE("V"), AFFECTE("E");

	/** L'attribut qui contient la valeur associée à l'enum */
	private final String value;

	/** Le constructeur qui associe une valeur à l'enum */
	private EtatAvancementEnum(String value) {
		this.value = value;
	}

	/** La méthode accesseur qui renvoit la valeur de l'enum */
	public String getValue() {
		return this.value;
	}

	/** La méthode accesseur qui renvoit la liste des valeurs de l'enum */
	public static String[] getValues() {
		String et[] = new String[EtatAvancementEnum.values().length];
		int i = 0;
		for (EtatAvancementEnum elt : EtatAvancementEnum.values()) {
			et[i++] = elt.getValue();
		}
		return et;
	}
}
