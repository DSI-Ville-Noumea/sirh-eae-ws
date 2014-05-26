package nc.noumea.mairie.sirh.eae.dto.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;

public class ValueWithListDto {

	private String courant;
	private List<ListItemDto> liste;

	public ValueWithListDto() {
		liste = new ArrayList<ListItemDto>();
	}

	@SuppressWarnings("rawtypes")
	public ValueWithListDto(Enum value, Class enumClass) {

		this();

		for (Object o : enumClass.getEnumConstants()) {
			Enum e = (Enum) o;
			ListItemDto item = new ListItemDto();
			item.setCode(e.name());
			item.setValeur(e.toString());
			liste.add(item);
		}

		courant = value != null ? value.name() : null;
	}

	public ValueWithListDto(EaeNiveau niveauValue, List<EaeNiveau> niveaux) {

		this();

		for (EaeNiveau n : niveaux) {
			ListItemDto item = new ListItemDto();
			item.setCode(n.getIdEaeNiveau().toString());
			item.setValeur(n.getLibelleNiveauEae());
			liste.add(item);
		}

		courant = niveauValue != null ? niveauValue.getIdEaeNiveau().toString()
				: null;
	}

	public ValueWithListDto(Integer valueId, List<SpbhorDto> choices) {

		this();

		DecimalFormat df = new DecimalFormat("#");
		df.setRoundingMode(RoundingMode.FLOOR);
		
		if(null != choices) {
			for (SpbhorDto n : choices) {
				
				ListItemDto item = new ListItemDto();
				item.setCode(n.getCdThor().toString());
				item.setValeur(String.format("%s - %s%%", n.getLabel(), df.format(n.getTaux() * 100)));
				liste.add(item);
	
				if (n.getCdThor().equals(valueId))
					courant = n.getCdThor().toString();
			}
		}

	}

	public String getCourant() {
		return courant;
	}

	public void setCourant(String courant) {
		this.courant = courant;
	}

	public List<ListItemDto> getListe() {
		return liste;
	}

	public void setListe(List<ListItemDto> liste) {
		this.liste = liste;
	}
}
