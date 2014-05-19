package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeObjectifEnum;

@Entity
@Table(name = "EAE_TYPE_OBJECTIF")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeTypeObjectif {

	@Id
	@Column(name = "ID_EAE_TYPE_OBJECTIF")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeTypeObjectif;

	@Column(name = "LIBELLE_TYPE_OBJECTIF")
	private String libelle;

	@Transient
	public EaeTypeObjectifEnum getTypeObjectifAsEnum() {
		return EaeTypeObjectifEnum.valueOf(libelle);
	}

	public Integer getIdEaeTypeObjectif() {
		return idEaeTypeObjectif;
	}

	public void setIdEaeTypeObjectif(Integer idEaeTypeObjectif) {
		this.idEaeTypeObjectif = idEaeTypeObjectif;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
}
