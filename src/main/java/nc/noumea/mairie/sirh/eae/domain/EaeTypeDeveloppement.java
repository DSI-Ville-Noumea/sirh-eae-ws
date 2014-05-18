package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EAE_TYPE_DEVELOPPEMENT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeTypeDeveloppement {

	@Id
	@SequenceGenerator(name = "eaeTypeDeveloppementGen", sequenceName = "EAE_S_TYPE_DEVELOPPEMENT")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eaeTypeDeveloppementGen")
	@Column(name = "ID_EAE_TYPE_DEVELOPPEMENT")
	private Integer idEaeTypeDeveloppement;

	@Column(name = "LIBELLE_TYPE_DEVELOPPEMENT")
	private String libelle;

	public Integer getIdEaeTypeDeveloppement() {
		return idEaeTypeDeveloppement;
	}

	public void setIdEaeTypeDeveloppement(Integer idEaeTypeDeveloppement) {
		this.idEaeTypeDeveloppement = idEaeTypeDeveloppement;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

}
