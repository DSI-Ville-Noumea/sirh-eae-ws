package nc.noumea.mairie.sirh.eae.dto.identification;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.EaeRefDelai;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;
import nc.noumea.mairie.sirh.eae.dto.poste.SpbhorDto;

public class EaeListeDto {

	private String					courant;
	private List<ValeurListeDto>	liste;

	public EaeListeDto() {
		liste = new ArrayList<ValeurListeDto>();
	}

	@SuppressWarnings("rawtypes")
	public EaeListeDto(Enum value, Class enumClass) {

		this();

		for (Object o : enumClass.getEnumConstants()) {
			Enum e = (Enum) o;
			ValeurListeDto item = new ValeurListeDto(e.name(), e.toString());
			liste.add(item);
		}

		courant = value != null ? value.name() : null;
	}

	/**
	 * Ce constructeur est utilisé uniquement pour les ref. délai
	 * @param value
	 */
	@SuppressWarnings("rawtypes")
	public EaeListeDto(EaeRefDelai value) {

		this();

		for (Object o : EaeDelaiEnum.class.getEnumConstants()) {
			Enum e = (Enum) o;
			ValeurListeDto item = new ValeurListeDto(e.name(), e.toString());
			liste.add(item);
		}

		courant = value != null ? value.getCode() : null;
	}

	public EaeListeDto(Integer valueId, List<SpbhorDto> choices) {

		this();

		if (null != valueId)
			courant = valueId.toString();

		DecimalFormat df = new DecimalFormat("#");
		df.setRoundingMode(RoundingMode.FLOOR);

		if (null != choices) {
			for (SpbhorDto n : choices) {

				ValeurListeDto item = new ValeurListeDto();
				item.setCode(n.getCdThor().toString());
				item.setValeur(String.format("%s - %s%%", n.getLabel(), df.format(n.getTaux() * 100)));
				liste.add(item);

				// if (n.getCdThor().equals(valueId))
				// courant = n.getCdThor().toString();
			}
		}
	}

	public String getCourant() {
		return courant;
	}

	public void setCourant(String courant) {
		this.courant = courant;
	}

	public List<ValeurListeDto> getListe() {
		return liste;
	}

	public void setListe(List<ValeurListeDto> liste) {
		this.liste = liste;
	}
}
