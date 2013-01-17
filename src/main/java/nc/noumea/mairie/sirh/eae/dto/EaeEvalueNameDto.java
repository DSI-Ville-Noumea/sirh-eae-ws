package nc.noumea.mairie.sirh.eae.dto;

import flexjson.JSONSerializer;

public class EaeEvalueNameDto implements IJSONSerialize {

	private String prenom;
	private String nom;
	
	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String _prenom) {
		prenom = _prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String _nom) {
		nom = _nom;
	}

	@Override
	public String serializeInJSON() {
		return new JSONSerializer().exclude("*.class").serialize(this);
	}

}
