package nc.noumea.mairie.sirh.eae.dto;

import java.io.Serializable;

import nc.noumea.mairie.sirh.eae.domain.EaeDocument;


public class EaeDocumentDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -591270859103581836L;
	
	private Integer idEaeDocument;
	private Integer idCampagneAction;
	private Integer idDocument;
	private String typeDocument;
	
	public EaeDocumentDto() {
		
	}
	
	public EaeDocumentDto(EaeDocument document) {
		this.idEaeDocument = document.getIdEaeDocument();
		this.idCampagneAction = document.getIdCampagneAction();
		this.idDocument = document.getIdDocument();
		this.typeDocument = document.getTypeDocument();
	}
	
	public Integer getIdEaeDocument() {
		return idEaeDocument;
	}
	public void setIdEaeDocument(Integer idEaeDocument) {
		this.idEaeDocument = idEaeDocument;
	}
	public Integer getIdCampagneAction() {
		return idCampagneAction;
	}
	public void setIdCampagneAction(Integer idCampagneAction) {
		this.idCampagneAction = idCampagneAction;
	}
	public Integer getIdDocument() {
		return idDocument;
	}
	public void setIdDocument(Integer idDocument) {
		this.idDocument = idDocument;
	}
	public String getTypeDocument() {
		return typeDocument;
	}
	public void setTypeDocument(String typeDocument) {
		this.typeDocument = typeDocument;
	}
}
