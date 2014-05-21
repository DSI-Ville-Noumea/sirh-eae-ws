package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;

@Entity
@Table(name = "EAE_EVALUATION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeEvaluation {

	@Id
	@Column(name = "ID_EAE_EVALUATION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idEaeEvaluation;

	@Column(name = "NOTE_ANNEE", columnDefinition = "numeric")
	private Float noteAnnee;

	@Column(name = "NOTE_ANNEE_N1", columnDefinition = "numeric")
	private Float noteAnneeN1;

	@Column(name = "NOTE_ANNEE_N2", columnDefinition = "numeric")
	private Float noteAnneeN2;

	@Column(name = "NOTE_ANNEE_N3", columnDefinition = "numeric")
	private Float noteAnneeN3;

	@Column(name = "AVIS_REVALORISATION")
	private Integer avisRevalorisation;

	@Column(name = "AVIS_SHD")
	private String avisShd;

	@Column(name = "PROPOSITION_AVANCEMENT")
	@Enumerated(EnumType.STRING)
	private EaeAvancementEnum propositionAvancement;

	@Column(name = "AVIS_CHANGEMENT_CLASSE")
	private Integer avisChangementClasse;

	@Column(name = "NIVEAU")
	@Enumerated(EnumType.STRING)
	private EaeNiveauEnum niveauEae;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_EVALUATEUR")
	private EaeCommentaire commentaireEvaluateur;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_EVALUE")
	private EaeCommentaire commentaireEvalue;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_AVCT_EVALUATEUR")
	private EaeCommentaire commentaireAvctEvaluateur;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COM_AVCT_EVALUE")
	private EaeCommentaire commentaireAvctEvalue;

	@OneToOne
	@JoinColumn(name = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeEvaluation() {
		return idEaeEvaluation;
	}

	public void setIdEaeEvaluation(Integer idEaeEvaluation) {
		this.idEaeEvaluation = idEaeEvaluation;
	}

	public Float getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Float noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public Float getNoteAnneeN1() {
		return noteAnneeN1;
	}

	public void setNoteAnneeN1(Float noteAnneeN1) {
		this.noteAnneeN1 = noteAnneeN1;
	}

	public Float getNoteAnneeN2() {
		return noteAnneeN2;
	}

	public void setNoteAnneeN2(Float noteAnneeN2) {
		this.noteAnneeN2 = noteAnneeN2;
	}

	public Float getNoteAnneeN3() {
		return noteAnneeN3;
	}

	public void setNoteAnneeN3(Float noteAnneeN3) {
		this.noteAnneeN3 = noteAnneeN3;
	}

	public Integer getAvisRevalorisation() {
		return avisRevalorisation;
	}

	public void setAvisRevalorisation(Integer avisRevalorisation) {
		this.avisRevalorisation = avisRevalorisation;
	}

	public String getAvisShd() {
		return avisShd;
	}

	public void setAvisShd(String avisShd) {
		this.avisShd = avisShd;
	}

	public EaeAvancementEnum getPropositionAvancement() {
		return propositionAvancement;
	}

	public void setPropositionAvancement(EaeAvancementEnum propositionAvancement) {
		this.propositionAvancement = propositionAvancement;
	}

	public Integer getAvisChangementClasse() {
		return avisChangementClasse;
	}

	public void setAvisChangementClasse(Integer avisChangementClasse) {
		this.avisChangementClasse = avisChangementClasse;
	}

	public EaeNiveauEnum getNiveauEae() {
		return niveauEae;
	}

	public void setNiveauEae(EaeNiveauEnum niveauEae) {
		this.niveauEae = niveauEae;
	}

	public EaeCommentaire getCommentaireEvaluateur() {
		return commentaireEvaluateur;
	}

	public void setCommentaireEvaluateur(EaeCommentaire commentaireEvaluateur) {
		this.commentaireEvaluateur = commentaireEvaluateur;
	}

	public EaeCommentaire getCommentaireEvalue() {
		return commentaireEvalue;
	}

	public void setCommentaireEvalue(EaeCommentaire commentaireEvalue) {
		this.commentaireEvalue = commentaireEvalue;
	}

	public EaeCommentaire getCommentaireAvctEvaluateur() {
		return commentaireAvctEvaluateur;
	}

	public void setCommentaireAvctEvaluateur(EaeCommentaire commentaireAvctEvaluateur) {
		this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
	}

	public EaeCommentaire getCommentaireAvctEvalue() {
		return commentaireAvctEvalue;
	}

	public void setCommentaireAvctEvalue(EaeCommentaire commentaireAvctEvalue) {
		this.commentaireAvctEvalue = commentaireAvctEvalue;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
