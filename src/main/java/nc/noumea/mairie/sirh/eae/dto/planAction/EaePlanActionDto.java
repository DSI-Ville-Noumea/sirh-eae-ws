package nc.noumea.mairie.sirh.eae.dto.planAction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.dto.IJSONDeserialize;
import nc.noumea.mairie.sirh.eae.dto.IJSONSerialize;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@XmlRootElement
public class EaePlanActionDto implements IJSONDeserialize<EaePlanActionDto>, IJSONSerialize {

	private int idEae;
	private List<PlanActionItemDto> objectifsProfessionnels;
	private List<String> objectifsIndividuels;
	private List<String> moyensMateriels;
	private List<String> moyensFinanciers;
	private List<String> moyensAutres;
	
	public EaePlanActionDto() {
		objectifsProfessionnels = new ArrayList<PlanActionItemDto>();
		objectifsIndividuels = new ArrayList<String>();
		moyensMateriels = new ArrayList<String>();
		moyensFinanciers = new ArrayList<String>();
		moyensAutres = new ArrayList<String>();
	}
	
	public EaePlanActionDto(Eae eae) {
		this();
		
		idEae = eae.getIdEae();
		
		for(EaePlanAction pa : eae.getEaePlanActions()) {
		
			switch(pa.getTypeObjectif().getTypeObjectifAsEnum()) {
				case PROFESSIONNEL:
					objectifsProfessionnels.add(new PlanActionItemDto(pa));
					break;
				case INDIVIDUEL:
					objectifsIndividuels.add(pa.getObjectif());
					break;
				case MATERIELS:
					moyensMateriels.add(pa.getObjectif());
					break;
				case FINANCIERS:
					moyensFinanciers.add(pa.getObjectif());
					break;
				case AUTRES:
					moyensAutres.add(pa.getObjectif());
					break;
			}
		}
		
	}
	
	public static JSONSerializer getJSONSerializerForEaePlanActionDto() {
		return new JSONSerializer()
			.exclude("*.class")	
			.include("*");
	}
	
	@Override
	public String serializeInJSON() {
		return getJSONSerializerForEaePlanActionDto().serialize(this);
	}

	@Override
	public EaePlanActionDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaePlanActionDto>()
				.deserializeInto(json, this);
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<PlanActionItemDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(
			List<PlanActionItemDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}

	public List<String> getMoyensFinanciers() {
		return moyensFinanciers;
	}

	public void setMoyensFinanciers(List<String> moyensFinanciers) {
		this.moyensFinanciers = moyensFinanciers;
	}

	public List<String> getMoyensAutres() {
		return moyensAutres;
	}

	public void setMoyensAutres(List<String> moyensAutres) {
		this.moyensAutres = moyensAutres;
	}

	public List<String> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<String> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}

	public List<String> getMoyensMateriels() {
		return moyensMateriels;
	}

	public void setMoyensMateriels(List<String> moyensMateriels) {
		this.moyensMateriels = moyensMateriels;
	}

}
