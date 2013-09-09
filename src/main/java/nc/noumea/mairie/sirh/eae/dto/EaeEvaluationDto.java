package nc.noumea.mairie.sirh.eae.dto;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;
import nc.noumea.mairie.sirh.eae.dto.util.ListItemDto;
import nc.noumea.mairie.sirh.eae.dto.util.ValueWithListDto;
import nc.noumea.mairie.sirh.tools.transformer.MinutesToHoursAndMinutesTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ObjectToPropertyTransformer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@XmlRootElement
public class EaeEvaluationDto implements IJSONSerialize, IJSONDeserialize<EaeEvaluationDto> {

	private int idEae;
	private Integer dureeEntretien;
	private Float noteAnnee;
	private Float noteAnneeN1;
	private Float noteAnneeN2;
	private Float noteAnneeN3;
	private Boolean avisRevalorisation;
	private ValueWithListDto propositionAvancement;
	private Boolean avisChangementClasse;
	private ValueWithListDto niveau;
	private EaeCommentaire commentaireEvaluateur;
	private EaeCommentaire commentaireEvalue;
	private EaeCommentaire commentaireAvctEvaluateur;
	private EaeCommentaire commentaireAvctEvalue;
	private int anneeAvancement;
	private String statut;
	private String typeAvct;

	public EaeEvaluationDto() {

	}

	public EaeEvaluationDto(Eae eae) {
		this(eae.getEaeEvaluation());
		this.propositionAvancement = getDureesAvancement(eae.getEaeEvaluation(), eae.getEaeEvalue());
		anneeAvancement = eae.getEaeCampagne().getAnnee();
		statut = eae.getEaeEvalue().getStatut().name();
		typeAvct = eae.getEaeEvalue().getTypeAvancement() == null ? null : eae.getEaeEvalue().getTypeAvancement().name();
	}

	protected EaeEvaluationDto(EaeEvaluation eaeEvaluation) {

		idEae = eaeEvaluation.getEae().getIdEae();
		dureeEntretien = eaeEvaluation.getEae().getDureeEntretienMinutes();
		noteAnnee = eaeEvaluation.getNoteAnnee();
		noteAnneeN1 = eaeEvaluation.getNoteAnneeN1();
		noteAnneeN2 = eaeEvaluation.getNoteAnneeN2();
		noteAnneeN3 = eaeEvaluation.getNoteAnneeN3();
		avisRevalorisation = eaeEvaluation.getAvisRevalorisation() == null ? true : eaeEvaluation.getAvisRevalorisation();
		avisChangementClasse = eaeEvaluation.getAvisChangementClasse() == null ? true : eaeEvaluation.getAvisChangementClasse();
		commentaireEvaluateur = eaeEvaluation.getCommentaireEvaluateur();
		commentaireEvalue = eaeEvaluation.getCommentaireEvalue();
		commentaireAvctEvaluateur = eaeEvaluation.getCommentaireAvctEvaluateur();
		commentaireAvctEvalue = eaeEvaluation.getCommentaireAvctEvalue();

		if (eaeEvaluation.getNiveauEae() == null)
			niveau = new ValueWithListDto(EaeNiveauEnum.SATISFAISANT, EaeNiveauEnum.class);
		else
			niveau = new ValueWithListDto(eaeEvaluation.getNiveauEae(), EaeNiveauEnum.class);
	}

	protected ValueWithListDto getDureesAvancement(EaeEvaluation eaeEvaluation, EaeEvalue eaeEvalue) {

		ValueWithListDto subDto = new ValueWithListDto();
		subDto.getListe().add(new ListItemDto(EaeAvancementEnum.MINI.name(), eaeEvalue.getAvctDureeMinDisplay()));
		subDto.getListe().add(new ListItemDto(EaeAvancementEnum.MOY.name(), eaeEvalue.getAvctDureeMoyDisplay()));
		subDto.getListe().add(new ListItemDto(EaeAvancementEnum.MAXI.name(), eaeEvalue.getAvctDureeMaxDisplay()));

		if (eaeEvaluation.getPropositionAvancement() == null)
			subDto.setCourant(EaeAvancementEnum.MOY.name());
		else
			subDto.setCourant(eaeEvaluation.getPropositionAvancement().name());

		return subDto;
	}

	public static JSONSerializer getSerializerForEaeEvaluationDto() {
		return new JSONSerializer().exclude("*.class").include("idEae").include("noteAnnee").include("noteAnneeN1").include("noteAnneeN2").include("noteAnneeN3")
				.include("avisRevalorisation").include("propositionAvancement.*").include("avisChangementClasse").include("niveau.*")
				.include("commentaireEvaluateur").include("commentaireEvalue").include("commentaireAvctEvaluateur").include("commentaireAvctEvalue")
				.include("dureeEntretien").include("anneeAvancement").include("statut").include("typeAvct")
				.transform(new MinutesToHoursAndMinutesTransformer(), "dureeEntretien")
				.transform(new ObjectToPropertyTransformer("text", EaeCommentaire.class), EaeCommentaire.class).exclude("*");
	}

	@Override
	public String serializeInJSON() {
		return getSerializerForEaeEvaluationDto().serialize(this);
	}

	@Override
	public EaeEvaluationDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaeEvaluationDto>().use(EaeCommentaire.class, new ObjectToPropertyTransformer("text", EaeCommentaire.class))
				.use("dureeEntretien", new MinutesToHoursAndMinutesTransformer()).deserializeInto(json, this);
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

	public ValueWithListDto getNiveau() {
		return niveau;
	}

	public void setNiveau(ValueWithListDto niveau) {
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

	public void setCommentaireAvctEvaluateur(EaeCommentaire commentaireAvctEvaluateur) {
		this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
	}

	public EaeCommentaire getCommentaireAvctEvalue() {
		return commentaireAvctEvalue;
	}

	public void setCommentaireAvctEvalue(EaeCommentaire commentaireAvctEvalue) {
		this.commentaireAvctEvalue = commentaireAvctEvalue;
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
}
