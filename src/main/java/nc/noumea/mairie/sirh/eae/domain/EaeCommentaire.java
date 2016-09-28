package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import nc.noumea.mairie.sirh.eae.dto.EaeCommentaireDto;

@Entity
@Table(name = "EAE_COMMENTAIRE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeCommentaire {

	@Id
	@Column(name = "ID_EAE_COMMENTAIRE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer	idEaeCommentaire;

	@Column(name = "TEXT")
	private String	text;

	public EaeCommentaire() {
	}

	public EaeCommentaire(EaeCommentaireDto commentaire) {
		this.text = commentaire.getText();
	}

	public EaeCommentaire(String texte) {
		this.text = texte;
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
