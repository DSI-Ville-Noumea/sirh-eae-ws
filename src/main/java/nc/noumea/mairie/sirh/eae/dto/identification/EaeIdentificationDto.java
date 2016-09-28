package nc.noumea.mairie.sirh.eae.dto.identification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.comparator.EaeParcoursProByDateDebutInverseComparator;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.dto.agent.BirtDto;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

@XmlRootElement
public class EaeIdentificationDto {
	private BirtDto							agent;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date							dateEntretien;
	private List<String>					diplomes;
	private List<BirtDto>					evaluateurs;
	private int								idEae;
	private List<String>					parcoursPros;
	private String							position;
	private EaeIdentificationSituationDto	situation;
	private List<String>					formations;
	private EaeIdentificationStatutDto		statut;
	private EaeListeDto						listePosition;
	private EaeListeDto						listeStatut;

	public EaeIdentificationDto() {
		evaluateurs = new ArrayList<BirtDto>();
		diplomes = new ArrayList<String>();
		parcoursPros = new ArrayList<String>();
		formations = new ArrayList<String>();
		listePosition = EaeAgentPositionAdministrativeEnum.getValuesDto();
		listeStatut = EaeAgentStatutEnum.getValuesDto();
	}

	public EaeIdentificationDto(Eae eae) {
		this();
		this.agent = new BirtDto(eae.getEaeEvalue(), eae.getPrimaryFichePoste());
		this.dateEntretien = eae.getDateEntretien();
		createDiplomeList(eae);
		if (null != eae.getEaeEvaluateurs()) {
			for (EaeEvaluateur eaeEval : eae.getEaeEvaluateurs()) {
				this.evaluateurs.add(new BirtDto(eaeEval));
			}
		}
		this.idEae = eae.getIdEae();
		createParcoursProList(eae);
		if (null != eae.getEaeEvalue().getPosition()){
			this.position = eae.getEaeEvalue().getPosition().name();
			this.listePosition.setCourant(this.position);
		}
		this.situation = new EaeIdentificationSituationDto(eae);

		createFormationList(eae);
		this.statut = new EaeIdentificationStatutDto(eae.getEaeEvalue());
		this.listeStatut.setCourant(this.statut.getStatut());
	}

	protected void createDiplomeList(Eae eae) {

		for (EaeDiplome d : eae.getEaeDiplomes()) {
			diplomes.add(d.getLibelleDiplome());
		}
	}

	protected void createParcoursProList(Eae eae) {
		List<EaeParcoursPro> theList = new ArrayList<EaeParcoursPro>();
		theList.addAll(eae.getEaeParcoursPros());
		Collections.sort(theList, new EaeParcoursProByDateDebutInverseComparator());
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		for (EaeParcoursPro p : theList) {
			parcoursPros.add(String.format("%s - %s", df.format(p.getDateDebut()), p.getLibelleParcoursPro()));
		}
	}

	protected void createFormationList(Eae eae) {

		List<EaeFormation> theList = new ArrayList<EaeFormation>();
		theList.addAll(eae.getEaeFormations());
		Collections.sort(theList, new Comparator<EaeFormation>() {

			@Override
			public int compare(EaeFormation o1, EaeFormation o2) {
				return o2.getAnneeFormation() - o1.getAnneeFormation();
			}

		});

		for (EaeFormation f : theList) {
			formations.add(String.format("%s : %s (%s)", f.getAnneeFormation(), f.getLibelleFormation(), f.getDureeFormation()));
		}
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public Date getDateEntretien() {
		return dateEntretien;
	}

	public void setDateEntretien(Date dateEntretien) {
		this.dateEntretien = dateEntretien;
	}

	public BirtDto getAgent() {
		return agent;
	}

	public void setAgent(BirtDto agent) {
		this.agent = agent;
	}

	public List<String> getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(List<String> diplomes) {
		this.diplomes = diplomes;
	}

	public List<String> getParcoursPros() {
		return parcoursPros;
	}

	public void setParcoursPros(List<String> parcoursPros) {
		this.parcoursPros = parcoursPros;
	}

	public List<String> getFormations() {
		return formations;
	}

	public void setFormations(List<String> formations) {
		this.formations = formations;
	}

	public EaeIdentificationSituationDto getSituation() {
		return situation;
	}

	public void setSituation(EaeIdentificationSituationDto situation) {
		this.situation = situation;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public List<BirtDto> getEvaluateurs() {
		return evaluateurs;
	}

	public void setEvaluateurs(List<BirtDto> evaluateurs) {
		this.evaluateurs = evaluateurs;
	}

	public EaeIdentificationStatutDto getStatut() {
		return statut;
	}

	public void setStatut(EaeIdentificationStatutDto statut) {
		this.statut = statut;
	}

	public EaeListeDto getListePosition() {
		return listePosition;
	}

	public void setListePosition(EaeListeDto listePosition) {
		this.listePosition = listePosition;
	}

	public EaeListeDto getListeStatut() {
		return listeStatut;
	}

	public void setListeStatut(EaeListeDto listeStatut) {
		this.listeStatut = listeStatut;
	}
}
