package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;

@Entity
@Table(name = "EAE_REF_DELAI")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeRefDelai {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "LIBELLE")
	private String libelle;
	
	public EaeRefDelai(EaeDelaiEnum _enum) {
		if (_enum == null)
			return;
		this.id = _enum.getId();
		this.code = _enum.name();
		this.libelle = _enum.getLibelle();
	}
	
	public EaeRefDelai() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	
}
