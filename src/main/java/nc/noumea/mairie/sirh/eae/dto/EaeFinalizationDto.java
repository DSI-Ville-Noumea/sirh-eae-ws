package nc.noumea.mairie.sirh.eae.dto;

import flexjson.JSONDeserializer;

public class EaeFinalizationDto implements IJSONDeserialize<EaeFinalizationDto> {

	private String idDocument;
	private String versionDocument;
	private String commentaire;
	private Float noteAnnee;
	
	public EaeFinalizationDto() {
		
	}
	
	@Override
	public EaeFinalizationDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaeFinalizationDto>().deserializeInto(json, this);
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
}
