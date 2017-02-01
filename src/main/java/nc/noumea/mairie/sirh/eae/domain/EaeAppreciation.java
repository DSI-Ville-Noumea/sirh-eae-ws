package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;

@Entity
@Table(name = "EAE_APPRECIATION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeAppreciation {

	@Id
	@Column(name = "ID_EAE_APPRECIATION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeAppreciation;

	@Column(name = "TYPE_APPRECIATION")
	@NotNull
	@Enumerated(EnumType.STRING)
	private EaeTypeAppreciationEnum typeAppreciation;

	@Column(name = "NUMERO")
	@NotNull
	private int numero;

	@Column(name = "NOTE_EVALUE")
	private String noteEvalue;

	@Column(name = "NOTE_EVALUATEUR")
	private String noteEvaluateur;

	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	@NotNull
	private Eae eae;

	public Integer getIdEaeAppreciation() {
		return idEaeAppreciation;
	}

	public void setIdEaeAppreciation(Integer idEaeAppreciation) {
		this.idEaeAppreciation = idEaeAppreciation;
	}

	public EaeTypeAppreciationEnum getTypeAppreciation() {
		return typeAppreciation;
	}

	public void setTypeAppreciation(EaeTypeAppreciationEnum typeAppreciation) {
		this.typeAppreciation = typeAppreciation;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNoteEvalue() {
		return noteEvalue == null ? "NA" : noteEvalue;
	}

	public void setNoteEvalue(String noteEvalue) {
		this.noteEvalue = noteEvalue;
	}

	public String getNoteEvaluateur() {
		return noteEvaluateur == null ? "NA" : noteEvalue;
	}

	public void setNoteEvaluateur(String noteEvaluateur) {
		this.noteEvaluateur = noteEvaluateur;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
