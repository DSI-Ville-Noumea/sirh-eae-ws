package nc.noumea.mairie.sirh.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AGENT")
@PersistenceUnit(unitName = "sirhPersistenceUnit")
public class Agent {

	@Id
	@Column(name = "ID_AGENT")
	private Integer idAgent;

	@NotNull
	@Column(name = "NOMATR")
	private Integer nomatr;

	@Column(name = "NOM_MARITAL")
	private String nomMarital;

	@NotNull
	@Column(name = "NOM_PATRONYMIQUE")
	private String nomPatronymique;

	@Column(name = "NOM_USAGE")
	private String nomUsage;

	@NotNull
	@Column(name = "PRENOM")
	private String prenom;

	@NotNull
	@Column(name = "PRENOM_USAGE")
	private String prenomUsage;

	@NotNull
	@Column(name = "DATE_NAISSANCE")
	@Temporal(TemporalType.DATE)
	private Date dateNaissance;

	@NotNull
	@Column(name = "DATE_DERNIERE_EMBAUCHE")
	@Temporal(TemporalType.DATE)
	private Date dateDerniereEmbauche;

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
}
