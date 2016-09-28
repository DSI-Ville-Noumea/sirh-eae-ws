package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeFinalizationDto {

	private String	idDocument;
	private String	versionDocument;
	private String	commentaire;
	private Float	noteAnnee;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date	dateFinalisation;
	private String	annee;
	
	byte[] bFile;
	private String typeFile;

	public EaeFinalizationDto() {
	}

	public EaeFinalizationDto(EaeFinalisation finalisation) {
		this.idDocument = finalisation.getNodeRefAlfresco();
		this.commentaire = finalisation.getCommentaire();
		this.versionDocument = "";
		this.dateFinalisation = finalisation.getDateFinalisation();

		if (null != finalisation.getEae() && null != finalisation.getEae().getEaeCampagne()) {
			this.annee = "" + finalisation.getEae().getEaeCampagne().getAnnee();
		}
	}

	public String getIdDocument() {
		return idDocument;
	}

	public void setIdDocument(String idDocument) {
		this.idDocument = idDocument;
	}

	public String getVersionDocument() {
		return versionDocument;
	}

	public void setVersionDocument(String versionDocument) {
		this.versionDocument = versionDocument;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Float getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Float noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public Date getDateFinalisation() {
		return dateFinalisation;
	}

	public void setDateFinalisation(Date dateFinalisation) {
		this.dateFinalisation = dateFinalisation;
	}

	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
	}
	
	public byte[] getbFile() {
		return bFile;
	}

	public void setbFile(byte[] bFile) {
		this.bFile = bFile;
	}
	
	public String getTypeFile() {
		return typeFile;
	}

	public void setTypeFile(String typeFile) {
		this.typeFile = typeFile;
	}

	@Override
	public String toString() {
		return "EaeFinalizationDto [idDocument=" + idDocument + ", versionDocument=" + versionDocument + ", commentaire=" + commentaire
				+ ", noteAnnee=" + noteAnnee + ", dateFinalisation=" + dateFinalisation + ", annee=" + annee + "]";
	}

}
