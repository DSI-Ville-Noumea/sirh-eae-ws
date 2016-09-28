package nc.noumea.mairie.sirh.eae.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeObjectifEnum;

@XmlRootElement
public class EaeResultatsDto {

	private int idEae;
	private String commentaireGeneral;
	
	private List<EaeResultatDto> objectifsProfessionnels;
	private List<EaeResultatDto> objectifsIndividuels;

	public EaeResultatsDto() {
		objectifsProfessionnels = new ArrayList<EaeResultatDto>();
		objectifsIndividuels = new ArrayList<EaeResultatDto>();
	}

	public EaeResultatsDto(Eae eae) {
		this();
		idEae = eae.getIdEae();
		
		if (eae.getCommentaire() != null)
			commentaireGeneral = eae.getCommentaire().getText();

		for (EaeResultat resultat : eae.getEaeResultats()) {
			if (resultat.getTypeObjectif().getLibelle().equals(EaeTypeObjectifEnum.PROFESSIONNEL.name()))
				objectifsProfessionnels.add(new EaeResultatDto(resultat));
			else if (resultat.getTypeObjectif().getLibelle().equals(EaeTypeObjectifEnum.INDIVIDUEL.name()))
				objectifsIndividuels.add(new EaeResultatDto(resultat));
		}
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

	public List<EaeResultatDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(
			List<EaeResultatDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}

	public List<EaeResultatDto> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<EaeResultatDto> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}
}
