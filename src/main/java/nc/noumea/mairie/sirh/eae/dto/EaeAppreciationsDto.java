package nc.noumea.mairie.sirh.eae.dto;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class EaeAppreciationsDto implements IJSONSerialize, IJSONDeserialize<EaeAppreciationsDto> {

	private int idEae;
	private String[] techniqueEvalue;
	private String[] techniqueEvaluateur;
	private String[] savoirEtreEvalue;
	private String[] savoirEtreEvaluateur;
	private String[] managerialEvalue;
	private String[] managerialEvaluateur;
	private String[] resultatsEvalue;
	private String[] resultatsEvaluateur;

	public EaeAppreciationsDto() {
		techniqueEvalue = new String[4];
		techniqueEvaluateur = new String[4];
		savoirEtreEvalue = new String[4];
		savoirEtreEvaluateur = new String[4];
		managerialEvalue = new String[4];
		managerialEvaluateur = new String[4];
		resultatsEvalue = new String[4];
		resultatsEvaluateur = new String[4];
	}

	public EaeAppreciationsDto(Eae eae) {
		
		this();
		idEae = eae.getIdEae();
		
		for (EaeAppreciation app : eae.getEaeAppreciations()) {
			switch (app.getTypeAppreciation()) {
			case TE:
				setNoteAt(app, techniqueEvalue, techniqueEvaluateur);
				break;
			case SE:
				setNoteAt(app, savoirEtreEvalue, savoirEtreEvaluateur);
				break;
			case MA:
				setNoteAt(app, managerialEvalue, managerialEvaluateur);
				break;
			case RE:
				setNoteAt(app, resultatsEvalue, resultatsEvaluateur);
				break;
			}
		}
	}

	private void setNoteAt(EaeAppreciation app, String[] notesEvalueArray, String[] notesEvaluateurArray) {
		notesEvalueArray[app.getNumero()] = app.getNoteEvalue();
		notesEvaluateurArray[app.getNumero()] = app.getNoteEvaluateur();
	}

	public static JSONSerializer getSerializerForEaeAppreciationsDto() {
		return new JSONSerializer()
			.exclude("*.class")
			.include("*");
	}
	
	@Override
	public String serializeInJSON() {
		return getSerializerForEaeAppreciationsDto().serialize(this);
	}
	
	@Override
	public EaeAppreciationsDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaeAppreciationsDto>().deserializeInto(json, this);
	}
	
	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String[] getTechniqueEvalue() {
		return techniqueEvalue;
	}

	public void setTechniqueEvalue(String[] techniqueEvalue) {
		this.techniqueEvalue = techniqueEvalue;
	}

	public String[] getTechniqueEvaluateur() {
		return techniqueEvaluateur;
	}

	public void setTechniqueEvaluateur(String[] techniqueEvaluateur) {
		this.techniqueEvaluateur = techniqueEvaluateur;
	}

	public String[] getSavoirEtreEvalue() {
		return savoirEtreEvalue;
	}

	public void setSavoirEtreEvalue(String[] savoirEtreEvalue) {
		this.savoirEtreEvalue = savoirEtreEvalue;
	}

	public String[] getSavoirEtreEvaluateur() {
		return savoirEtreEvaluateur;
	}

	public void setSavoirEtreEvaluateur(String[] savoirEtreEvaluateur) {
		this.savoirEtreEvaluateur = savoirEtreEvaluateur;
	}

	public String[] getManagerialEvalue() {
		return managerialEvalue;
	}

	public void setManagerialEvalue(String[] managerialEvalue) {
		this.managerialEvalue = managerialEvalue;
	}

	public String[] getManagerialEvaluateur() {
		return managerialEvaluateur;
	}

	public void setManagerialEvaluateur(String[] managerialEvaluateur) {
		this.managerialEvaluateur = managerialEvaluateur;
	}

	public String[] getResultatsEvalue() {
		return resultatsEvalue;
	}

	public void setResultatsEvalue(String[] resultatsEvalue) {
		this.resultatsEvalue = resultatsEvalue;
	}

	public String[] getResultatsEvaluateur() {
		return resultatsEvaluateur;
	}

	public void setResultatsEvaluateur(String[] resultatsEvaluateur) {
		this.resultatsEvaluateur = resultatsEvaluateur;
	}
}
