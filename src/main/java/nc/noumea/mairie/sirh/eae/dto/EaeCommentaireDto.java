package nc.noumea.mairie.sirh.eae.dto;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;

@XmlRootElement
public class EaeCommentaireDto {

	private Integer	idEaeCommentaire;
	private String	text;

	public EaeCommentaireDto() {

	}

	public EaeCommentaireDto(EaeCommentaire commentaire) {
		if (commentaire != null) {
			this.idEaeCommentaire = commentaire.getIdEaeCommentaire();
			this.text = commentaire.getText();
		}
	}

	public Integer getIdEaeCommentaire() {
		return idEaeCommentaire;
	}

	public void setIdEaeCommentaire(Integer idEaeCommentaire) {
		this.idEaeCommentaire = idEaeCommentaire;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
