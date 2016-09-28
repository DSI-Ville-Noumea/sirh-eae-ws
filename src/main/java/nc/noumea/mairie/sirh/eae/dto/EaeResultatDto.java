package nc.noumea.mairie.sirh.eae.dto;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.EaeResultat;

@XmlRootElement
public class EaeResultatDto {

	private EaeCommentaireDto	commentaire;
	private Integer				idEaeResultat;
	private String				objectif;
	private String				resultat;
	private EaeTypeObjectifDto	typeObjectif;

	public EaeResultatDto() {

	}

	public EaeResultatDto(EaeResultat resultat) {
		this.idEaeResultat = resultat.getIdEaeResultat();
		this.objectif = resultat.getObjectif();
		this.resultat = resultat.getResultat();
		this.commentaire = new EaeCommentaireDto(resultat.getCommentaire());
		this.typeObjectif = null == resultat.getTypeObjectif() ? null : new EaeTypeObjectifDto(resultat.getTypeObjectif());
	}

	public Integer getIdEaeResultat() {
		return idEaeResultat;
	}

	public void setIdEaeResultat(Integer idEaeResultat) {
		this.idEaeResultat = idEaeResultat;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

	public EaeCommentaireDto getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(EaeCommentaireDto commentaire) {
		this.commentaire = commentaire;
	}

	public EaeTypeObjectifDto getTypeObjectif() {
		return typeObjectif;
	}

	public void setTypeObjectif(EaeTypeObjectifDto typeObjectif) {
		this.typeObjectif = typeObjectif;
	}
}
