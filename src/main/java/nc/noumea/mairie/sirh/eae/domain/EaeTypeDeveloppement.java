package nc.noumea.mairie.sirh.eae.domain;

import java.text.Normalizer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;

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

	@Column(name = "CODE_TYPE_DEVELOPPEMENT")
	private String code;
	
	@Column(name = "LIBELLE_TYPE_DEVELOPPEMENT")
	private String libelle;
	
	public EaeTypeDeveloppement(Integer id, String libelle) {
		this.idEaeTypeDeveloppement = id;
		this.libelle = libelle;
		
		/* On met le code par default : libellé sans les accents, en majuscule */
		libelle = Normalizer.normalize(libelle, Normalizer.Form.NFD);
		libelle = libelle.replaceAll("[^\\p{ASCII}]", "");
		this.code = StringUtils.upperCase(libelle);
	}
	
	public EaeTypeDeveloppement(EaeTypeDeveloppementEnum type) {
		this.idEaeTypeDeveloppement = type.getId();
		this.libelle = type.getLibelle();
		this.code = type.name();
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
	
	 @Override
	 public boolean equals(Object obj) {
		 EaeTypeDeveloppement e = (EaeTypeDeveloppement) obj;
	                
         return (e.getLibelle().equals(this.libelle) 
                        && e.getIdEaeTypeDeveloppement().equals(this.idEaeTypeDeveloppement)
                        && e.getCode().equals(this.code));
	 }
}
