package nc.noumea.mairie.sirh.eae.dto.identification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.comparator.EaeParcoursProByDateDebutInverseComparator;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.dto.IJSONDeserialize;
import nc.noumea.mairie.sirh.eae.dto.IJSONSerialize;
import nc.noumea.mairie.sirh.tools.transformer.EaeEvalueToAgentTransformer;
import nc.noumea.mairie.sirh.tools.transformer.EnumToListAndValueTransformer;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ObjectToPropertyTransformer;
import nc.noumea.mairie.sirh.tools.transformer.SimpleAgentTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ValueEnumTransformer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@XmlRootElement
public class EaeIdentificationDto implements IJSONSerialize, IJSONDeserialize<EaeIdentificationDto> {

	private int idEae;
	private Date dateEntretien;
	private List<EaeEvaluateur> evaluateurs;
	private EaeEvalue agent;
	private List<EaeDiplome> diplomes;
	private List<String> parcoursPros;
	private List<EaeFormation> formations;
	private EaeIdentificationSituationDto situation;
	private EaeIdentificationStatutDto statut;
	private EaeAgentPositionAdministrativeEnum position;
	
	public EaeIdentificationDto() {
		evaluateurs = new ArrayList<EaeEvaluateur>();
		diplomes = new ArrayList<EaeDiplome>();
		parcoursPros = new ArrayList<String>();
		formations = new ArrayList<EaeFormation>();
	}

	public EaeIdentificationDto(Eae eae) {
		this();
		this.idEae = eae.getIdEae();
		this.dateEntretien = eae.getDateEntretien();
		this.evaluateurs.addAll(eae.getEaeEvaluateurs());
		this.agent = eae.getEaeEvalue();
		this.diplomes.addAll(eae.getEaeDiplomes());
		this.formations.addAll(eae.getEaeFormations());
		this.situation = new EaeIdentificationSituationDto(eae);
		this.statut = new EaeIdentificationStatutDto(eae.getEaeEvalue());
		this.position = eae.getEaeEvalue().getPosition();
	
		createParcoursProList(eae);
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

	public static JSONSerializer getSerializerForEaeIdentificationDto() {

		JSONSerializer serializer = new JSONSerializer()
				.exclude("*.class")
				.include("idEae")
				.include("dateEntretien")
				.include("evaluateurs.agent")
				.include("evaluateurs.fonction")
				.include("evaluateurs.dateEntreeService")
				.include("evaluateurs.dateEntreeCollectivite")
				.include("evaluateurs.dateEntreeFonction")
				.include("agent")
				.include("diplomes")
				.include("parcoursPros.*")
				.include("formations")
				.include("situation.*")
				.include("statut.*")
				.include("position")
				.transform(new MSDateTransformer(), Date.class)
				.transform(new SimpleAgentTransformer(true), Agent.class)
				.transform(new EaeEvalueToAgentTransformer(false), EaeEvalue.class)
				.transform(new ObjectToPropertyTransformer("libelleDiplome", EaeDiplome.class), EaeDiplome.class)
				.transform(new ObjectToPropertyTransformer("libelleFormation", EaeFormation.class), EaeFormation.class)
				.transform(new EnumToListAndValueTransformer(EaeAgentStatutEnum.class), EaeAgentStatutEnum.class)
				.transform(new EnumToListAndValueTransformer(EaeAgentPositionAdministrativeEnum.class), EaeAgentPositionAdministrativeEnum.class)
				.transform(new ValueEnumTransformer(), Enum.class)
				.exclude("*");

		return serializer;
	}

	public static JSONDeserializer<EaeIdentificationDto> getDeserializerForEaeIdentificationDto() {

		JSONDeserializer<EaeIdentificationDto> deserializer = new JSONDeserializer<EaeIdentificationDto>()
				.use(Date.class, new MSDateTransformer());

		return deserializer;
	}
	
	@Override
	public EaeIdentificationDto deserializeFromJSON(String json) {
		return getDeserializerForEaeIdentificationDto().deserializeInto(json, this);
	}

	@Override
	public String serializeInJSON() {
		return getSerializerForEaeIdentificationDto().serialize(this);
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

	public List<EaeEvaluateur> getEvaluateurs() {
		return evaluateurs;
	}

	public void setEvaluateurs(List<EaeEvaluateur> evaluateurs) {
		this.evaluateurs = evaluateurs;
	}

	public EaeEvalue getAgent() {
		return agent;
	}

	public void setAgent(EaeEvalue agent) {
		this.agent = agent;
	}

	public List<EaeDiplome> getDiplomes() {
		return diplomes;
	}

	public void setDiplomes(List<EaeDiplome> diplomes) {
		this.diplomes = diplomes;
	}

	public List<String> getParcoursPros() {
		return parcoursPros;
	}

	public void setParcoursPros(List<String> parcoursPros) {
		this.parcoursPros = parcoursPros;
	}

	public List<EaeFormation> getFormations() {
		return formations;
	}

	public void setFormations(List<EaeFormation> formations) {
		this.formations = formations;
	}

	public EaeIdentificationSituationDto getSituation() {
		return situation;
	}

	public void setSituation(EaeIdentificationSituationDto situation) {
		this.situation = situation;
	}

	public EaeIdentificationStatutDto getStatut() {
		return statut;
	}

	public void setStatut(EaeIdentificationStatutDto statut) {
		this.statut = statut;
	}

	public EaeAgentPositionAdministrativeEnum getPosition() {
		return position;
	}

	public void setPosition(EaeAgentPositionAdministrativeEnum position) {
		this.position = position;
	}
}
