package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "EAE_PARCOURS_PRO")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeParcoursPro {

	@Id
	@SequenceGenerator(name = "eaeParcoursProGen", sequenceName = "EAE_S_PARCOURS_PRO")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eaeParcoursProGen")
	@Column(name = "ID_EAE_PARCOURS_PRO")
	private Integer idEaeParcoursPro;

	@Column(name = "DATE_DEBUT")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDebut;

	@Column(name = "DATE_FIN")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFin;

	@Column(name = "LIBELLE_PARCOURS_PRO", length = 255)
	private String libelleParcoursPro;

	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeParcoursPro() {
		return idEaeParcoursPro;
	}

	public void setIdEaeParcoursPro(Integer idEaeParcoursPro) {
		this.idEaeParcoursPro = idEaeParcoursPro;
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

	public String getLibelleParcoursPro() {
		return libelleParcoursPro;
	}

	public void setLibelleParcoursPro(String libelleParcoursPro) {
		this.libelleParcoursPro = libelleParcoursPro;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
