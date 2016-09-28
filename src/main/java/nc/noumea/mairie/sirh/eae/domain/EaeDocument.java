package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EAE_DOCUMENT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
@NamedQueries({ @NamedQuery(name = "getEaeDocumentByIdDocument", query = "select e from EaeDocument e where e.idDocument = :idDocument " ) })
public class EaeDocument {

	@Id
	@Column(name = "ID_EAE_DOCUMENT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer		idEaeDocument;

	@OneToOne
	@JoinColumn(name = "ID_CAMPAGNE_EAE", referencedColumnName = "ID_CAMPAGNE_EAE")
	private EaeCampagne	eaeCampagne;

	@Column(name = "id_campagne_action")
	private Integer idCampagneAction;

	@NotNull
	@Column(name = "id_document")
	private Integer idDocument;

	@Column(name = "type_document")
	private String typeDocument;

	public EaeDocument() {
		super();
	}

	public Integer getIdEaeDocument() {
		return idEaeDocument;
	}

	public void setIdEaeDocument(Integer idEaeDocument) {
		this.idEaeDocument = idEaeDocument;
	}

	public EaeCampagne getEaeCampagne() {
		return eaeCampagne;
	}

	public void setEaeCampagne(EaeCampagne eaeCampagne) {
		this.eaeCampagne = eaeCampagne;
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

	public Integer getIdCampagneAction() {
		return idCampagneAction;
	}

	public void setIdCampagneAction(Integer idCampagneAction) {
		this.idCampagneAction = idCampagneAction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEaeDocument == null) ? 0 : idEaeDocument.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EaeDocument other = (EaeDocument) obj;
		if (idEaeDocument == null) {
			if (other.idEaeDocument != null)
				return false;
		} else if (!idEaeDocument.equals(other.idEaeDocument))
			return false;
		return true;
	}
	
}
