package nc.noumea.mairie.sirh.eae.dto;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.dto.identification.EaeListeDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;

@XmlRootElement
public class EaeEvaluationDto {
	private Integer				dureeEntretien;
	private EaeCommentaireDto	commentaireEvaluateur;
	private EaeCommentaireDto	commentaireEvalue;
	private EaeListeDto			niveau;
	private Float				noteAnnee;
	private Float				noteAnneeN1;
	private Float				noteAnneeN2;
	private Float				noteAnneeN3;
	private Boolean				avisRevalorisation;
	private Boolean				avisChangementClasse;
	private EaeListeDto			propositionAvancement;
	private int					anneeAvancement;
	private EaeCommentaireDto	commentaireAvctEvaluateur;
	private EaeCommentaireDto	commentaireAvctEvalue;
	private String				statut;
	private String				typeAvct;
	private boolean				cap;
	private int					idEae;
	private String				avisShd;

	public EaeEvaluationDto() {

	}

	public EaeEvaluationDto(Eae eae) {
		this(eae.getEaeEvaluation());
		this.propositionAvancement = getDureesAvancement(eae.getEaeEvaluation(), eae.getEaeEvalue());
		this.anneeAvancement = eae.getEaeCampagne().getAnnee();
		this.statut = eae.getEaeEvalue().getStatut() == null ? "" : eae.getEaeEvalue().getStatut().name();
		this.typeAvct = eae.getEaeEvalue().getTypeAvancement() == null ? null : eae.getEaeEvalue().getTypeAvancement().name();
		this.cap = eae.isCap();
	}

	protected EaeEvaluationDto(EaeEvaluation eaeEvaluation) {

		idEae = eaeEvaluation.getEae().getIdEae();
		dureeEntretien = eaeEvaluation.getEae().getDureeEntretienMinutes();
		noteAnnee = eaeEvaluation.getNoteAnnee();
		noteAnneeN1 = eaeEvaluation.getNoteAnneeN1();
		noteAnneeN2 = eaeEvaluation.getNoteAnneeN2();
		noteAnneeN3 = eaeEvaluation.getNoteAnneeN3();
		avisRevalorisation = eaeEvaluation.getAvisRevalorisation() == null ? true : eaeEvaluation.getAvisRevalorisation() == 0 ? false : true;
		avisChangementClasse = eaeEvaluation.getAvisChangementClasse() == null ? true : eaeEvaluation.getAvisChangementClasse() == 0 ? false : true;
		commentaireEvaluateur = new EaeCommentaireDto(eaeEvaluation.getCommentaireEvaluateur());
		commentaireEvalue = new EaeCommentaireDto(eaeEvaluation.getCommentaireEvalue());
		commentaireAvctEvaluateur = new EaeCommentaireDto(eaeEvaluation.getCommentaireAvctEvaluateur());
		commentaireAvctEvalue = new EaeCommentaireDto(eaeEvaluation.getCommentaireAvctEvalue());

		if (eaeEvaluation.getNiveauEae() == null)
			niveau = new EaeListeDto(EaeNiveauEnum.SATISFAISANT, EaeNiveauEnum.class);
		else
			niveau = new EaeListeDto(eaeEvaluation.getNiveauEae(), EaeNiveauEnum.class);

		avisShd = eaeEvaluation.getAvisShd();
	}

	protected EaeListeDto getDureesAvancement(EaeEvaluation eaeEvaluation, EaeEvalue eaeEvalue) {

		EaeListeDto subDto = new EaeListeDto();
		subDto.getListe().add(new ValeurListeDto(EaeAvancementEnum.MINI.name(), eaeEvalue.getAvctDureeMinDisplay()));
		subDto.getListe().add(new ValeurListeDto(EaeAvancementEnum.MOY.name(), eaeEvalue.getAvctDureeMoyDisplay()));
		subDto.getListe().add(new ValeurListeDto(EaeAvancementEnum.MAXI.name(), eaeEvalue.getAvctDureeMaxDisplay()));

		if (eaeEvaluation.getPropositionAvancement() == null)
			subDto.setCourant(EaeAvancementEnum.MOY.name());
		else
			subDto.setCourant(eaeEvaluation.getPropositionAvancement().name());

		return subDto;
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
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

	public int getAnneeAvancement() {
		return anneeAvancement;
	}

	public void setAnneeAvancement(int anneeAvancement) {
		this.anneeAvancement = anneeAvancement;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	public String getTypeAvct() {
		return typeAvct;
	}

	public void setTypeAvct(String typeAvct) {
		this.typeAvct = typeAvct;
	}

	public Float getNoteAnnee() {
		return noteAnnee;
	}

	public void setNoteAnnee(Float noteAnnee) {
		this.noteAnnee = noteAnnee;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public EaeCommentaireDto getCommentaireEvaluateur() {
		return commentaireEvaluateur;
	}

	public void setCommentaireEvaluateur(EaeCommentaireDto commentaireEvaluateur) {
		this.commentaireEvaluateur = commentaireEvaluateur;
	}

	public EaeCommentaireDto getCommentaireEvalue() {
		return commentaireEvalue;
	}

	public void setCommentaireEvalue(EaeCommentaireDto commentaireEvalue) {
		this.commentaireEvalue = commentaireEvalue;
	}

	public EaeCommentaireDto getCommentaireAvctEvaluateur() {
		return commentaireAvctEvaluateur;
	}

	public void setCommentaireAvctEvaluateur(EaeCommentaireDto commentaireAvctEvaluateur) {
		this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
	}

	public EaeCommentaireDto getCommentaireAvctEvalue() {
		return commentaireAvctEvalue;
	}

	public void setCommentaireAvctEvalue(EaeCommentaireDto commentaireAvctEvalue) {
		this.commentaireAvctEvalue = commentaireAvctEvalue;
	}

	public Integer getDureeEntretien() {
		return dureeEntretien;
	}

	public void setDureeEntretien(Integer dureeEntretien) {
		this.dureeEntretien = dureeEntretien;
	}

	public EaeListeDto getNiveau() {
		return niveau;
	}

	public void setNiveau(EaeListeDto niveau) {
		this.niveau = niveau;
	}

	public EaeListeDto getPropositionAvancement() {
		return propositionAvancement;
	}

	public void setPropositionAvancement(EaeListeDto propositionAvancement) {
		this.propositionAvancement = propositionAvancement;
	}

	public Boolean getAvisChangementClasse() {
		return avisChangementClasse;
	}

	public void setAvisChangementClasse(Boolean avisChangementClasse) {
		this.avisChangementClasse = avisChangementClasse;
	}

	public Boolean getAvisRevalorisation() {
		return avisRevalorisation;
	}

	public void setAvisRevalorisation(Boolean avisRevalorisation) {
		this.avisRevalorisation = avisRevalorisation;
	}

	public String getAvisShd() {
		return avisShd;
	}

	public void setAvisShd(String avisShd) {
		this.avisShd = avisShd;
	}

	@Override
	public String toString() {
		return "EaeEvaluationDto [idEae=" + idEae + ", anneeAvancement=" + anneeAvancement + ", avisChangementClasse=" + avisChangementClasse
				+ ", avisRevalorisation=" + avisRevalorisation + ", cap=" + cap + ", commentaireAvctEvaluateur=" + commentaireAvctEvaluateur
				+ ", commentaireAvctEvalue=" + commentaireAvctEvalue + ", commentaireEvaluateur=" + commentaireEvaluateur + ", commentaireEvalue="
				+ commentaireEvalue + ", dureeEntretien=" + dureeEntretien + ", niveau=" + niveau + ", noteAnnee=" + noteAnnee + ", noteAnneeN1="
				+ noteAnneeN1 + ", noteAnneeN2=" + noteAnneeN2 + ", noteAnneeN3=" + noteAnneeN3 + ", propositionAvancement=" + propositionAvancement
				+ ", statut=" + statut + ", typeAvct=" + typeAvct + ", avisShd=" + avisShd + "]";
	}

}
