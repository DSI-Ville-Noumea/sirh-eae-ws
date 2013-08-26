package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

public class CampagneEaeDto {
	private Integer idCampagneEae;
	private Integer annee;
	private Date dateDebut;
	private Date dateFin;
	private Date dateOuvertureKiosque;
	private Date dateFermetureKiosque;

	public Integer getIdCampagneEae() {
		return idCampagneEae;
	}

	public void setIdCampagneEae(Integer idCampagneEae) {
		this.idCampagneEae = idCampagneEae;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateOuvertureKiosque() {
		return dateOuvertureKiosque;
	}

	public void setDateOuvertureKiosque(Date dateOuvertureKiosque) {
		this.dateOuvertureKiosque = dateOuvertureKiosque;
	}

	public Date getDateFermetureKiosque() {
		return dateFermetureKiosque;
	}

	public void setDateFermetureKiosque(Date dateFermetureKiosque) {
		this.dateFermetureKiosque = dateFermetureKiosque;
	}
}
