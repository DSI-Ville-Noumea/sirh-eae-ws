package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;

public class CampagneEaeDto {
	private Integer idCampagneEae;
	private Integer annee;
	private Date dateDebut;
	private Date dateFin;
	private Date dateOuvertureKiosque;
	private Date dateFermetureKiosque;

	public CampagneEaeDto(EaeCampagne camp) {
		this.idCampagneEae = camp.getIdCampagneEae();
		this.annee = camp.getAnnee();
		this.dateDebut = camp.getDateDebut();
		this.dateFin = camp.getDateFin();
		this.dateOuvertureKiosque = camp.getDateOuvertureKiosque();
		this.dateFermetureKiosque = camp.getDateFermetureKiosque();

	}

	public CampagneEaeDto() {
	}

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
