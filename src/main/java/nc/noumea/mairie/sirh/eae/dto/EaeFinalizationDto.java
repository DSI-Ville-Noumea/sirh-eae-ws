package nc.noumea.mairie.sirh.eae.dto;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.tools.transformer.NullableIntegerTransformer;

public class EaeFinalizationDto implements IJSONDeserialize<EaeFinalizationDto> {

	private String	idDocument;
	private String	versionDocument;
	private String	commentaire;
	private Float	noteAnnee;
	private String	annee;

	public EaeFinalizationDto() {

	}

	public EaeFinalizationDto(EaeFinalisation finalisation) {
		this.idDocument = finalisation.getIdGedDocument();
		this.versionDocument = finalisation.getVersionGedDocument();
		this.commentaire = finalisation.getCommentaire();
		this.annee = "" + finalisation.getEae().getEaeCampagne().getAnnee();
	}

	public static JSONSerializer getSerializerForEaeFinalizationDto() {

		JSONSerializer serializer = new JSONSerializer().include("idDocument").include("versionDocument").include("commentaire").include("noteAnnee")
				.include("annee").transform(new NullableIntegerTransformer(), Integer.class).exclude("*");

		return serializer;
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

	public String getAnnee() {
		return annee;
	}

	public void setAnnee(String annee) {
		this.annee = annee;
	}
}
