package nc.noumea.mairie.sirh.eae.dto;

import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;
import nc.noumea.mairie.sirh.tools.transformer.MinutesToHoursAndMinutesTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ObjectToPropertyTransformer;

import org.joda.time.Period;

import flexjson.JSONSerializer;

public class EaeEvaluationDto implements IJSONSerialize, IJSONDeserialize<EaeEvaluationDto> {

	private int idEae;
	private Integer dureeEntretien;
	private Integer noteAnnee;
	private Integer noteAnneeN1;
	private Integer noteAnneeN2;
	private Integer noteAnneeN3;
	private Boolean avisRevalorisation;
	private ValueWithListDto propositionAvancement;
	private Boolean avisChangementClasse;
	private EaeNiveau niveau;
	private EaeCommentaire commentaireEvaluateur;
	private EaeCommentaire commentaireEvalue;
	private EaeCommentaire commentaireAvctEvaluateur;
	private EaeCommentaire commentaireAvctEvalue;

	public EaeEvaluationDto() {

	}

	public EaeEvaluationDto(EaeEvaluation eaeEvaluation) {
		idEae = eaeEvaluation.getEae().getIdEae();
		dureeEntretien =eaeEvaluation.getEae().getDureeEntretienMinutes();
		noteAnnee = eaeEvaluation.getNoteAnnee();
		noteAnneeN1 = eaeEvaluation.getNoteAnneeN1();
		noteAnneeN2 = eaeEvaluation.getNoteAnneeN2();
		noteAnneeN3 = eaeEvaluation.getNoteAnneeN3();
		avisRevalorisation = eaeEvaluation.getAvisRevalorisation();
		avisChangementClasse = eaeEvaluation.getAvisChangementClasse();
		propositionAvancement = new ValueWithListDto(eaeEvaluation.getPropositionAvancement(), EaeAvancementEnum.class);
		niveau = eaeEvaluation.getNiveauEae();
		commentaireEvaluateur = eaeEvaluation.getCommentaireEvaluateur();
		commentaireEvalue = eaeEvaluation.getCommentaireEvalue();
		commentaireAvctEvaluateur = eaeEvaluation.getCommentaireAvctEvaluateur();
		commentaireAvctEvalue = eaeEvaluation.getCommentaireAvctEvalue();
	}

	public static JSONSerializer getSerializerForEaeEvaluationDto() {
		return new JSONSerializer()
			.exclude("*.class")
			.include("idEae")
			.include("noteAnnee")
			.include("noteAnneeN1")
			.include("noteAnneeN2")
			.include("noteAnneeN3")
			.include("avisRevalorisation")
			.include("propositionAvancement.*")
			.include("avisChangementClasse")
			.include("niveau")
			.include("commentaireEvaluateur")
			.include("commentaireEvalue")
			.include("commentaireAvctEvaluateur")
			.include("commentaireAvctEvalue")
			.include("dureeEntretien")
			.transform(new MinutesToHoursAndMinutesTransformer(), Period.class)
			.transform(new ObjectToPropertyTransformer("text", EaeCommentaire.class), EaeCommentaire.class)
			.exclude("*");
	}

	@Override
	public String serializeInJSON() {
		return getSerializerForEaeEvaluationDto().serialize(this);
	}

	@Override
	public EaeEvaluationDto deserializeFromJSON(String json) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public Integer getDureeEntretien() {
		return dureeEntretien;
	}

	public void setDureeEntretien(Integer dureeEntretien) {
		this.dureeEntretien = dureeEntretien;
	}

	public Integer getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Integer noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public Integer getNoteAnneeN1() {
		return noteAnneeN1;
	}

	public void setNoteAnneeN1(Integer noteAnneeN1) {
		this.noteAnneeN1 = noteAnneeN1;
	}

	public Integer getNoteAnneeN2() {
		return noteAnneeN2;
	}

	public void setNoteAnneeN2(Integer noteAnneeN2) {
		this.noteAnneeN2 = noteAnneeN2;
	}

	public Integer getNoteAnneeN3() {
		return noteAnneeN3;
	}

	public void setNoteAnneeN3(Integer noteAnneeN3) {
		this.noteAnneeN3 = noteAnneeN3;
	}

	public Boolean getAvisRevalorisation() {
		return avisRevalorisation;
	}

	public void setAvisRevalorisation(Boolean avisRevalorisation) {
		this.avisRevalorisation = avisRevalorisation;
	}

	public ValueWithListDto getPropositionAvancement() {
		return propositionAvancement;
	}

	public void setPropositionAvancement(ValueWithListDto propositionAvancement) {
		this.propositionAvancement = propositionAvancement;
	}

	public Boolean getAvisChangementClasse() {
		return avisChangementClasse;
	}

	public void setAvisChangementClasse(Boolean avisChangementClasse) {
		this.avisChangementClasse = avisChangementClasse;
	}

	public EaeNiveau getNiveau() {
		return niveau;
	}

	public void setNiveau(EaeNiveau niveau) {
		this.niveau = niveau;
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

	public void setCommentaireAvctEvaluateur(
			EaeCommentaire commentaireAvctEvaluateur) {
		this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
	}

	public EaeCommentaire getCommentaireAvctEvalue() {
		return commentaireAvctEvalue;
	}

	public void setCommentaireAvctEvalue(EaeCommentaire commentaireAvctEvalue) {
		this.commentaireAvctEvalue = commentaireAvctEvalue;
	}
}
