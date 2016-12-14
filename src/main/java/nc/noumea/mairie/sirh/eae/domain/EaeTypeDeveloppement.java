package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;

@Entity
@Table(name = "EAE_TYPE_DEVELOPPEMENT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
@NamedQueries({ @NamedQuery(name = "getListeTypeDeveloppement", query = "select e from EaeTypeDeveloppement e order by libelle " ) })
public class EaeTypeDeveloppement {

	@Id
	@Column(name = "ID_EAE_TYPE_DEVELOPPEMENT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeTypeDeveloppement;

	@Column(name = "LIBELLE_TYPE_DEVELOPPEMENT")
	private String libelle;
	
	public EaeTypeDeveloppement(Integer id, String libelle) {
		this.idEaeTypeDeveloppement = id;
		this.libelle = libelle;
	}
	
	public EaeTypeDeveloppement(EaeTypeDeveloppementEnum type) {
		this.idEaeTypeDeveloppement = type.getId();
		this.libelle = type.getType();
	}
	
	public EaeTypeDeveloppement() {
		super();
	}

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
