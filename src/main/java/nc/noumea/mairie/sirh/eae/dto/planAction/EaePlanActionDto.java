package nc.noumea.mairie.sirh.eae.dto.planAction;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

@XmlRootElement
public class EaePlanActionDto {

	private int							idEae;
	private List<String>				moyensAutres;
	private List<String>				moyensFinanciers;
	private List<String>				moyensMateriels;
	private List<String>				objectifsIndividuels;
	private List<EaeObjectifProDto>		objectifsProfessionnels;
	// Version en liste pour kiosque et SIRH, ce qui a au dessus est utile à
	// BIRT
	private List<EaeItemPlanActionDto>	listeObjectifsIndividuels;
	private List<EaeItemPlanActionDto>	listeMoyensAutres;
	private List<EaeItemPlanActionDto>	listeMoyensFinanciers;
	private List<EaeItemPlanActionDto>	listeMoyensMateriels;

	public EaePlanActionDto() {
		objectifsProfessionnels = new ArrayList<>();
		objectifsIndividuels = new ArrayList<>();
		moyensMateriels = new ArrayList<>();
		moyensFinanciers = new ArrayList<>();
		moyensAutres = new ArrayList<>();
		listeObjectifsIndividuels = new ArrayList<>();
		listeMoyensAutres = new ArrayList<>();
		listeMoyensFinanciers = new ArrayList<>();
		listeMoyensMateriels = new ArrayList<>();
	}

	public EaePlanActionDto(Eae eae) {
		this();

		idEae = eae.getIdEae();

		if (null != eae.getEaePlanActions()) {
			for (EaePlanAction pa : eae.getEaePlanActions()) {
				switch (pa.getTypeObjectif().getTypeObjectifAsEnum()) {
					case PROFESSIONNEL:
						objectifsProfessionnels.add(new EaeObjectifProDto(pa));

						break;
					case INDIVIDUEL:
						objectifsIndividuels.add(pa.getObjectif());
						listeObjectifsIndividuels.add(new EaeItemPlanActionDto(pa));
						break;
					case MATERIELS:
						moyensMateriels.add(pa.getObjectif());
						listeMoyensMateriels.add(new EaeItemPlanActionDto(pa));
						break;
					case FINANCIERS:
						moyensFinanciers.add(pa.getObjectif());
						listeMoyensFinanciers.add(new EaeItemPlanActionDto(pa));
						break;
					case AUTRES:
						moyensAutres.add(pa.getObjectif());
						listeMoyensAutres.add(new EaeItemPlanActionDto(pa));
						break;
				}
			}
		}
	}

	public int getIdEae() {
		return idEae;
	}

	public void setIdEae(int idEae) {
		this.idEae = idEae;
	}

	public List<String> getMoyensAutres() {
		return moyensAutres;
	}

	public void setMoyensAutres(List<String> moyensAutres) {
		this.moyensAutres = moyensAutres;
	}

	public List<String> getMoyensFinanciers() {
		return moyensFinanciers;
	}

	public void setMoyensFinanciers(List<String> moyensFinanciers) {
		this.moyensFinanciers = moyensFinanciers;
	}

	public List<String> getMoyensMateriels() {
		return moyensMateriels;
	}

	public void setMoyensMateriels(List<String> moyensMateriels) {
		this.moyensMateriels = moyensMateriels;
	}

	public List<String> getObjectifsIndividuels() {
		return objectifsIndividuels;
	}

	public void setObjectifsIndividuels(List<String> objectifsIndividuels) {
		this.objectifsIndividuels = objectifsIndividuels;
	}

	public List<EaeObjectifProDto> getObjectifsProfessionnels() {
		return objectifsProfessionnels;
	}

	public void setObjectifsProfessionnels(List<EaeObjectifProDto> objectifsProfessionnels) {
		this.objectifsProfessionnels = objectifsProfessionnels;
	}

	@Override
	public String toString() {
		return "EaePlanActionDto [idEae=" + idEae + ", moyensAutres=" + moyensAutres + ", moyensFinanciers=" + moyensFinanciers + ", moyensMateriels="
				+ moyensMateriels + ", objectifsIndividuels=" + objectifsIndividuels + ", objectifsProfessionnels=" + objectifsProfessionnels + "]";
	}

	public List<EaeItemPlanActionDto> getListeObjectifsIndividuels() {
		return listeObjectifsIndividuels;
	}

	public void setListeObjectifsIndividuels(List<EaeItemPlanActionDto> listeObjectifsIndividuels) {
		this.listeObjectifsIndividuels = listeObjectifsIndividuels;
	}

	public List<EaeItemPlanActionDto> getListeMoyensAutres() {
		return listeMoyensAutres;
	}

	public void setListeMoyensAutres(List<EaeItemPlanActionDto> listeMoyensAutres) {
		this.listeMoyensAutres = listeMoyensAutres;
	}

	public List<EaeItemPlanActionDto> getListeMoyensFinanciers() {
		return listeMoyensFinanciers;
	}

	public void setListeMoyensFinanciers(List<EaeItemPlanActionDto> listeMoyensFinanciers) {
		this.listeMoyensFinanciers = listeMoyensFinanciers;
	}

	public List<EaeItemPlanActionDto> getListeMoyensMateriels() {
		return listeMoyensMateriels;
	}

	public void setListeMoyensMateriels(List<EaeItemPlanActionDto> listeMoyensMateriels) {
		this.listeMoyensMateriels = listeMoyensMateriels;
	}

}
