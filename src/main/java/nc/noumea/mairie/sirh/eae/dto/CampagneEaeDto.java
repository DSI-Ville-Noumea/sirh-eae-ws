package nc.noumea.mairie.sirh.eae.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagne;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneAction;
import nc.noumea.mairie.sirh.eae.domain.EaeDocument;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class CampagneEaeDto implements Serializable {

	/**
	 * 
	 */
	private static final long			serialVersionUID	= -3730906012723377831L;

	private Integer						idCampagneEae;
	private Integer						annee;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateDebut;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateFin;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateOuvertureKiosque;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateFermetureKiosque;
	private String						commentaire;

	private List<EaeCampagneActionDto>	listeCampagneAction;
	private List<EaeDocumentDto>		listeEaeDocument;

	public CampagneEaeDto() {
		this.listeCampagneAction = new ArrayList<EaeCampagneActionDto>();
		this.listeEaeDocument = new ArrayList<EaeDocumentDto>();
	}

	public CampagneEaeDto(EaeCampagne camp) {
		this();
		this.idCampagneEae = camp.getIdCampagneEae();
		this.annee = camp.getAnnee();
		this.dateDebut = camp.getDateDebut();
		this.dateFin = camp.getDateFin();
		this.dateOuvertureKiosque = camp.getDateOuvertureKiosque();
		this.dateFermetureKiosque = camp.getDateFermetureKiosque();
		this.commentaire = camp.getCommentaire();

		if (null != camp.getListeDocument()) {
			for (EaeDocument document : camp.getListeDocument()) {
				EaeDocumentDto docDto = new EaeDocumentDto(document);
				this.listeEaeDocument.add(docDto);
			}
		}
	}

	public CampagneEaeDto(EaeCampagne camp, boolean modeComplet) {
		this(camp);

		if (modeComplet && null != camp.getListeCampagneAction()) {
			for (EaeCampagneAction campagneAction : camp.getListeCampagneAction()) {
				this.listeCampagneAction.add(new EaeCampagneActionDto(campagneAction));
			}
		}
	}

	public Integer getIdCampagneEae() {
		return idCampagneEae;
	}

	public void setIdCampagneEae(Integer idCampagneEae) {
		this.idCampagneEae = idCampagneEae;
	}

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateOuvertureKiosque() {
		return dateOuvertureKiosque;
	}

	public void setDateOuvertureKiosque(Date dateOuvertureKiosque) {
		this.dateOuvertureKiosque = dateOuvertureKiosque;
	}

	public Date getDateFermetureKiosque() {
		return dateFermetureKiosque;
	}

	public void setDateFermetureKiosque(Date dateFermetureKiosque) {
		this.dateFermetureKiosque = dateFermetureKiosque;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public List<EaeCampagneActionDto> getListeCampagneAction() {
		return listeCampagneAction;
	}

	public void setListeCampagneAction(List<EaeCampagneActionDto> listeCampagneAction) {
		this.listeCampagneAction = listeCampagneAction;
	}

	public List<EaeDocumentDto> getListeEaeDocument() {
		return listeEaeDocument;
	}

	public void setListeEaeDocument(List<EaeDocumentDto> listeEaeDocument) {
		this.listeEaeDocument = listeEaeDocument;
	}

}
