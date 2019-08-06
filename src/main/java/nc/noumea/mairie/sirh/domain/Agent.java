package nc.noumea.mairie.sirh.domain;

import java.util.Date;

public class Agent {

	private Integer idAgent;
	private Integer nomatr;
	private String nomMarital;
	private String nomPatronymique;
	private String nomUsage;
	private String prenom;
	private String prenomUsage;
	private Date dateNaissance;
	private Date dateDerniereEmbauche;
	private String idTiarhe;

	public String getDisplayPrenom() {
		if (getPrenomUsage() != null && !getPrenomUsage().isEmpty())
			return getPrenomUsage();
		else
			return getPrenom();
	}

	public String getDisplayNom() {
		if (getNomMarital() != null && !getNomMarital().isEmpty())
			return getNomMarital();
		else if (getNomUsage() != null && !getNomUsage().isEmpty())
			return getNomUsage();
		else
			return getNomPatronymique();
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	public Integer getNomatr() {
		return nomatr;
	}

	public void setNomatr(Integer nomatr) {
		this.nomatr = nomatr;
	}

	public String getNomMarital() {
		return nomMarital;
	}

	public void setNomMarital(String nomMarital) {
		this.nomMarital = nomMarital;
	}

	public String getNomPatronymique() {
		return nomPatronymique;
	}

	public void setNomPatronymique(String nomPatronymique) {
		this.nomPatronymique = nomPatronymique;
	}

	public String getNomUsage() {
		return nomUsage;
	}

	public void setNomUsage(String nomUsage) {
		this.nomUsage = nomUsage;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPrenomUsage() {
		return prenomUsage;
	}

	public void setPrenomUsage(String prenomUsage) {
		this.prenomUsage = prenomUsage;
	}

	public Date getDateNaissance() {
		return dateNaissance;
	}

	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}

	public Date getDateDerniereEmbauche() {
		return dateDerniereEmbauche;
	}

	public void setDateDerniereEmbauche(Date dateDerniereEmbauche) {
		this.dateDerniereEmbauche = dateDerniereEmbauche;
	}

	public String getIdTiarhe() {
		return idTiarhe;
	}

	public void setIdTiarhe(String idTiarhe) {
		this.idTiarhe = idTiarhe;
	}
}
