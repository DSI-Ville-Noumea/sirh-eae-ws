package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeEtatEnum {

	NA("Non affecté"), ND("Non débuté"), C("Créé"), EC("En cours"), F("Finalisé"), CO("Contrôlé"), S("Supprimé");

	private String statut;

	private EaeEtatEnum(String _statut) {
		this.statut = _statut;
	}

	public static EaeEtatEnum getEaeEtatEnum(String abbreviation) {
		switch (abbreviation) {
			case "NA":
				return NA;
			case "ND":
				return ND;
			case "C":
				return C;
			case "EC":
				return EC;
			case "F":
				return F;
			case "CO":
				return CO;
			case "S":
				return S;

		}
		return null;
	}

	@Override
	public String toString() {
		return statut;
	}
}
