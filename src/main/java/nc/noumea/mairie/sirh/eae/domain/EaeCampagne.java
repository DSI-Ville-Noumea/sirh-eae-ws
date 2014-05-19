package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@Table(name = "EAE_CAMPAGNE_EAE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeCampagne {

	@Id
	@Column(name = "ID_CAMPAGNE_EAE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCampagneEae;

	@Column(name = "ANNEE")
	private int annee;

	@Column(name = "DATE_DEBUT")
	private Date dateDebut;

	@Column(name = "DATE_FIN")
	private Date dateFin;

	@Column(name = "DATE_OUVERTURE_KIOSQUE")
	private Date dateOuvertureKiosque;

	@Column(name = "DATE_FERMETURE_KIOSQUE")
	private Date dateFermetureKiosque;

	public Integer getIdCampagneEae() {
		return idCampagneEae;
	}

	public void setIdCampagneEae(Integer idCampagneEae) {
		this.idCampagneEae = idCampagneEae;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
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
