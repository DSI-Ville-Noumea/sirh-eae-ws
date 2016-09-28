package nc.noumea.mairie.sirh.eae.dto;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

@XmlRootElement
public class EaeTypeObjectifDto {

	private Integer	idEaeTypeObjectif;
	private String	libelle;

	public EaeTypeObjectifDto() {

	}

	public EaeTypeObjectifDto(EaeTypeObjectif typeObjectif) {
		this.idEaeTypeObjectif = typeObjectif.getIdEaeTypeObjectif();
		this.libelle = typeObjectif.getLibelle();
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
