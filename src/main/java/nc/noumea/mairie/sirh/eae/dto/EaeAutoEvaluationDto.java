package nc.noumea.mairie.sirh.eae.dto;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAutoEvaluation;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

@XmlRootElement
public class EaeAutoEvaluationDto implements IJSONSerialize, IJSONDeserialize<EaeAutoEvaluationDto> {

	private int idEae;
	private String particularites;
	private String acquis;
	private String succesDifficultes;
	
	public EaeAutoEvaluationDto() {
		
	}
	
	public EaeAutoEvaluationDto(Eae eae) {
		
		idEae = eae.getIdEae();
		EaeAutoEvaluation autoEval = eae.getEaeAutoEvaluation();
		
		if (autoEval != null) {
			particularites = autoEval.getParticularites();
			acquis = autoEval.getAcquis();
			succesDifficultes = autoEval.getSuccesDifficultes();
		}
	}
	
	@Override
	public EaeAutoEvaluationDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaeAutoEvaluationDto>().deserializeInto(json, this);
	}

	@Override
	public String serializeInJSON() {
		return new JSONSerializer()
			.exclude("*.class")
			.serialize(this);
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String getParticularites() {
		return particularites;
	}

	public void setParticularites(String particularites) {
		this.particularites = particularites;
	}

	public String getAcquis() {
		return acquis;
	}

	public void setAcquis(String acquis) {
		this.acquis = acquis;
	}

	public String getSuccesDifficultes() {
		return succesDifficultes;
	}

	public void setSuccesDifficultes(String succesDifficultes) {
		this.succesDifficultes = succesDifficultes;
	}
}
