package nc.noumea.mairie.sirh.eae.dto.poste;

public class ActiviteMetierSavoirFaire {
	private String activiteMetier;
	private String savoirFaire;

	public ActiviteMetierSavoirFaire() {
		this.activiteMetier = "";
		this.savoirFaire = "";
	}

	public ActiviteMetierSavoirFaire(String activiteMetier, String savoirFaire) {
		this.activiteMetier = activiteMetier;
		this.savoirFaire = savoirFaire;
	}

	public String getActiviteMetier() {
		return activiteMetier;
	}

	public void setActiviteMetier(String activiteMetier) {
		this.activiteMetier = activiteMetier;
	}

	public String getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(String savoirFaire) {
		this.savoirFaire = savoirFaire;
	}
}
