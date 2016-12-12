package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.dto.agent.BirtDto;
import nc.noumea.mairie.sirh.eae.dto.planAction.EaePlanActionDto;
import nc.noumea.mairie.sirh.eae.dto.poste.EaeFichePosteDto;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeDto {

	private Integer						idEae;
	private String						etat;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateEntretien;

	private CampagneEaeDto				campagne;
	private BirtDto						evalue;

	private EaeFichePosteDto			fichePoste;
	private List<BirtDto>				evaluateurs;

	private EaeEvaluationDto			evaluation;
	private EaePlanActionDto			planAction;
	private EaeEvolutionDto				evolution;
	private List<EaeFinalizationDto>	finalisation;

	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateCreation;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateFinalisation;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date						dateControle;

	private String						userControle;
	private Integer						idAgentDelegataire;

	private boolean						cap;
	private boolean						docAttache;

	public EaeDto() {
		this.evaluateurs = new ArrayList<BirtDto>();
		this.finalisation = new ArrayList<EaeFinalizationDto>();
	}

	public EaeDto(Eae eae, boolean isModecomplet) {

		this();
		this.idEae = eae.getIdEae();
		this.etat = eae.getEtat().name();
		this.dateEntretien = eae.getDateEntretien();

		if (null != eae.getEaeCampagne())
			this.campagne = new CampagneEaeDto(eae.getEaeCampagne());

		if (null != eae.getEaeEvaluateurs()) {
			for (EaeEvaluateur eaeEval : eae.getEaeEvaluateurs()) {
				this.evaluateurs.add(new BirtDto(eaeEval));
			}
		}
		this.dateCreation = eae.getDateCreation();
		this.dateControle = eae.getDateControle();
		this.dateFinalisation = eae.getDateFinalisation();
		this.userControle = eae.getUserControle();
		this.idAgentDelegataire = eae.getIdAgentDelegataire();
		this.cap = eae.isCap();
		this.docAttache = eae.isDocAttache();

		if (isModecomplet) {
			if (null != eae.getEaeEvalue())
				this.evalue = new BirtDto(eae.getEaeEvalue(), eae.getPrimaryFichePoste());

			if (null != eae.getPrimaryFichePoste())
				this.fichePoste = new EaeFichePosteDto(eae.getPrimaryFichePoste());

			if (null != eae.getEaeEvaluation())
				this.evaluation = new EaeEvaluationDto(eae);

			this.planAction = new EaePlanActionDto(eae);

			this.evolution = new EaeEvolutionDto(eae, null);
			for (EaeFinalisation finalisation : eae.getEaeFinalisations()) {
				this.finalisation.add(new EaeFinalizationDto(finalisation));
			}
		}
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public CampagneEaeDto getCampagne() {
		return campagne;
	}

	public void setCampagne(CampagneEaeDto campagne) {
		this.campagne = campagne;
	}

	public BirtDto getEvalue() {
		return evalue;
	}

	public void setEvalue(BirtDto evalue) {
		this.evalue = evalue;
	}

	public EaeFichePosteDto getFichePoste() {
		return fichePoste;
	}

	public void setFichePoste(EaeFichePosteDto fichePoste) {
		this.fichePoste = fichePoste;
	}

	public List<BirtDto> getEvaluateurs() {
		return evaluateurs;
	}

	public void setEvaluateurs(List<BirtDto> evaluateurs) {
		this.evaluateurs = evaluateurs;
	}

	public EaeEvaluationDto getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(EaeEvaluationDto evaluation) {
		this.evaluation = evaluation;
	}

	public EaePlanActionDto getPlanAction() {
		return planAction;
	}

	public void setPlanAction(EaePlanActionDto planAction) {
		this.planAction = planAction;
	}

	public EaeEvolutionDto getEvolution() {
		return evolution;
	}

	public void setEvolution(EaeEvolutionDto evolution) {
		this.evolution = evolution;
	}

	public List<EaeFinalizationDto> getFinalisation() {
		return finalisation;
	}

	public void setFinalisation(List<EaeFinalizationDto> finalisation) {
		this.finalisation = finalisation;
	}

	public Integer getIdEae() {
		return idEae;
	}

	public void setIdEae(Integer idEae) {
		this.idEae = idEae;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDateFinalisation() {
		return dateFinalisation;
	}

	public void setDateFinalisation(Date dateFinalisation) {
		this.dateFinalisation = dateFinalisation;
	}

	public Date getDateControle() {
		return dateControle;
	}

	public void setDateControle(Date dateControle) {
		this.dateControle = dateControle;
	}

	public String getUserControle() {
		return userControle;
	}

	public void setUserControle(String userControle) {
		this.userControle = userControle;
	}

	public Integer getIdAgentDelegataire() {
		return idAgentDelegataire;
	}

	public void setIdAgentDelegataire(Integer idAgentDelegataire) {
		this.idAgentDelegataire = idAgentDelegataire;
	}

	public boolean isCap() {
		return cap;
	}

	public void setCap(boolean cap) {
		this.cap = cap;
	}

	public boolean isDocAttache() {
		return docAttache;
	}

	public void setDocAttache(boolean docAttache) {
		this.docAttache = docAttache;
	}

	@Override
	public String toString() {
		return "EaeDto [idEae=" + idEae + ", etat=" + etat + ", dateEntretien=" + dateEntretien + ", campagne=" + campagne + ", evalue=" + evalue
				+ ", fichePoste=" + fichePoste + ", evaluateurs=" + evaluateurs + ", evaluation=" + evaluation + ", planAction=" + planAction
				+ ", evolution=" + evolution + ", finalisation=" + finalisation + ", dateCreation=" + dateCreation + ", dateFinalisation="
				+ dateFinalisation + ", dateControle=" + dateControle + ", userControle=" + userControle
				+ ", idAgentDelegataire=" + idAgentDelegataire + ", cap=" + cap + ", docAttache=" + docAttache + "]";
	}

}
