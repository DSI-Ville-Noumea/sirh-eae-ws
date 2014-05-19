package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EAE_FINALISATION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFinalisation {

	@Id
	@Column(name = "ID_EAE_FINALISATION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeFinalisation;

	@NotNull
	@Column(name = "DATE_FINALISATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateFinalisation;

	@NotNull
	@Column(name = "ID_AGENT")
	private int idAgent;

	@NotNull
	@Column(name = "ID_GED_DOCUMENT")
	private String idGedDocument;

	@NotNull
	@Column(name = "VERSION_GED_DOCUMENT")
	private String versionGedDocument;

	@Lob
	@Column(name = "COMMENTAIRE")
	private String commentaire;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeFinalisation() {
		return idEaeFinalisation;
	}

	public void setIdEaeFinalisation(Integer idEaeFinalisation) {
		this.idEaeFinalisation = idEaeFinalisation;
	}

	public Date getDateFinalisation() {
		return dateFinalisation;
	}

	public void setDateFinalisation(Date dateFinalisation) {
		this.dateFinalisation = dateFinalisation;
	}

	public int getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(int idAgent) {
		this.idAgent = idAgent;
	}

	public String getIdGedDocument() {
		return idGedDocument;
	}

	public void setIdGedDocument(String idGedDocument) {
		this.idGedDocument = idGedDocument;
	}

	public String getVersionGedDocument() {
		return versionGedDocument;
	}

	public void setVersionGedDocument(String versionGedDocument) {
		this.versionGedDocument = versionGedDocument;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
