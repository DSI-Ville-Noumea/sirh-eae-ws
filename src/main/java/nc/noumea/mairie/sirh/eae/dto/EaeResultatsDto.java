package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeObjectifEnum;
import nc.noumea.mairie.sirh.tools.transformer.ObjectToPropertyTransformer;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class EaeResultatsDto implements IJSONSerialize, IJSONDeserialize<EaeResultatsDto> {

	private int idEae;
	private String commentaireGeneral;
	private List<EaeResultat> objectifsProfessionnels;
	private List<EaeResultat> objectifsIndividuels;

	public EaeResultatsDto() {
		objectifsProfessionnels = new ArrayList<EaeResultat>();
		objectifsIndividuels = new ArrayList<EaeResultat>();
	}

	public EaeResultatsDto(Eae eae) {
		this();
		idEae = eae.getIdEae();
		
		if (eae.getCommentaire() != null)
			commentaireGeneral = eae.getCommentaire().getText();

		for (EaeResultat resultat : eae.getEaeResultats()) {
			if (resultat.getTypeObjectif().getLibelle().equals(EaeTypeObjectifEnum.PROFESSIONNEL.name()))
				objectifsProfessionnels.add(resultat);
			else if (resultat.getTypeObjectif().getLibelle().equals(EaeTypeObjectifEnum.INDIVIDUEL.name()))
				objectifsIndividuels.add(resultat);
		}
	}

	public static JSONSerializer getSerializerForEaeResultatDto() {
		return new JSONSerializer().include("idEae")
				.include("commentaireGeneral")
				.include("objectifsIndividuels.idEaeResultat")
				.include("objectifsIndividuels.objectif")
				.include("objectifsIndividuels.resultat")
				.include("objectifsIndividuels.commentaire")
				.include("objectifsProfessionnels.idEaeResultat")
				.include("objectifsProfessionnels.objectif")
				.include("objectifsProfessionnels.resultat")
				.include("objectifsProfessionnels.commentaire")
				.transform(new ObjectToPropertyTransformer("text", EaeCommentaire.class), EaeCommentaire.class)
				.exclude("*.class")
				.exclude("*");
	}

	@Override
	public String serializeInJSON() {
		return getSerializerForEaeResultatDto().serialize(this);
	}
	
	@Override
	public EaeResultatsDto deserializeFromJSON(String json) {
		return new JSONDeserializer<EaeResultatsDto>().use(EaeCommentaire.class, new ObjectToPropertyTransformer("text", EaeCommentaire.class)).deserializeInto(json, this);
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public String getCommentaireGeneral() {
		return commentaireGeneral;
	}

	public void setCommentaireGeneral(String commentaireGeneral) {
		this.commentaireGeneral = commentaireGeneral;
	}

	public List<EaeResultat> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(
			List<EaeResultat> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}

	public List<EaeResultat> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<EaeResultat> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}
}
