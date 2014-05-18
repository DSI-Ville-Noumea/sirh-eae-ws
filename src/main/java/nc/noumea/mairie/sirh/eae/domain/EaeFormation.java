package nc.noumea.mairie.sirh.eae.domain;

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

@Entity
@Table(name = "EAE_FORMATION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFormation {

	@Id
	@SequenceGenerator(name = "eaeFormationGen", sequenceName = "EAE_S_FORMATION")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eaeFormationGen")
	@Column(name = "ID_EAE_FORMATION")
	private Integer idEaeFormation;

	@Column(name = "ANNEE_FORMATION")
	private int anneeFormation;
	
	@Column(name = "DUREE_FORMATION")
	private String dureeFormation;
	
	@Column(name = "LIBELLE_FORMATION", length = 255)
	private String libelleFormation;
	
	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeFormation() {
		return idEaeFormation;
	}

	public void setIdEaeFormation(Integer idEaeFormation) {
		this.idEaeFormation = idEaeFormation;
	}

	public int getAnneeFormation() {
		return anneeFormation;
	}

	public void setAnneeFormation(int anneeFormation) {
		this.anneeFormation = anneeFormation;
	}

	public String getDureeFormation() {
		return dureeFormation;
	}

	public void setDureeFormation(String dureeFormation) {
		this.dureeFormation = dureeFormation;
	}

	public String getLibelleFormation() {
		return libelleFormation;
	}

	public void setLibelleFormation(String libelleFormation) {
		this.libelleFormation = libelleFormation;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
