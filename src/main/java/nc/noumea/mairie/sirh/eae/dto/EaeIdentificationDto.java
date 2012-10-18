package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;
import nc.noumea.mairie.sirh.tools.transformer.SimpleAgentTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ValueEnumTransformer;

import org.springframework.roo.addon.json.RooJson;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@RooJson
public class EaeIdentificationDto implements IJSONSerialize, IJSONDeserialize<EaeIdentificationDto> {

	private int idEae;
	private Date dateEntretien;
	private List<EaeEvaluateur> evaluateurs;
	private EaeEvalue agent;
	private List<EaeDiplome> diplomes;
	private List<EaeParcoursPro> parcoursPros;
	private List<EaeFormation> formations;

	public EaeIdentificationDto() {
		evaluateurs = new ArrayList<EaeEvaluateur>();
		diplomes = new ArrayList<EaeDiplome>();
		parcoursPros = new ArrayList<EaeParcoursPro>();
		formations = new ArrayList<EaeFormation>();
	}

	public EaeIdentificationDto(Eae eae) {
		this();
		this.idEae = eae.getIdEae();
		this.dateEntretien = eae.getDateEntretien();
		this.evaluateurs.addAll(eae.getEaeEvaluateurs());
		this.agent = eae.getEaeEvalue();
		this.diplomes.addAll(eae.getEaeDiplomes());
		this.parcoursPros.addAll(eae.getEaeParcoursPros());
		this.formations.addAll(eae.getEaeFormations());
	}

	public static JSONSerializer getSerializerForEaeIdentificationDto() {

		JSONSerializer serializer = new JSONSerializer().include("idEae")
				.include("dateEntretien").include("evaluateurs.agent")
				.include("evaluateurs.fonction")
				.include("evaluateurs.dateEntreeService")
				.include("evaluateurs.dateEntreeCollectivite")
				.include("evaluateurs.dateEntreeFonction").include("agent")
				.include("diplomes").include("parcoursPros")
				.include("formations")
				.transform(new MSDateTransformer(), Date.class)
				.transform(new SimpleAgentTransformer(true), Agent.class)
				.transform(new ValueEnumTransformer(), Enum.class).exclude("*");

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

	public List<EaeParcoursPro> getParcoursPros() {
		return parcoursPros;
	}

	public void setParcoursPros(List<EaeParcoursPro> parcoursPros) {
		this.parcoursPros = parcoursPros;
	}

	public List<EaeFormation> getFormations() {
		return formations;
	}

	public void setFormations(List<EaeFormation> formations) {
		this.formations = formations;
	}
}
