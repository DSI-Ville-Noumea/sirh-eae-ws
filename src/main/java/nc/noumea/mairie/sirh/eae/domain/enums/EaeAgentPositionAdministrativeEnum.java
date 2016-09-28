package nc.noumea.mairie.sirh.eae.domain.enums;

import nc.noumea.mairie.sirh.eae.dto.identification.EaeListeDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;

public enum EaeAgentPositionAdministrativeEnum {

	AC("Activité"), MD("Mise à disposition"), D("Détachement"), A("Autre");

	private String position;

	private EaeAgentPositionAdministrativeEnum(String _position) {
		this.position = _position;
	}

	@Override
	public String toString() {
		return position;
	}

	public static EaeListeDto getValuesDto() {
		EaeListeDto res = new EaeListeDto();
		res.getListe().clear();
		for (EaeAgentPositionAdministrativeEnum e : values()) {
			ValeurListeDto v = new ValeurListeDto(e.name(), e.toString());
			res.getListe().add(v);

		}
		return res;
	}

}
