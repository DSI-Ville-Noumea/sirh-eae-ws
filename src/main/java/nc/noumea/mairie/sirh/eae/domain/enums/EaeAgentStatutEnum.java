package nc.noumea.mairie.sirh.eae.domain.enums;

import nc.noumea.mairie.sirh.eae.dto.identification.EaeListeDto;
import nc.noumea.mairie.sirh.eae.dto.identification.ValeurListeDto;

public enum EaeAgentStatutEnum {

	F("Fonctionnaire"), C("Contractuel"), CC("Convention collective"), AL("Allocataire"), A("Autre");

	private String statut;

	private EaeAgentStatutEnum(String _statut) {
		this.statut = _statut;
	}

	@Override
	public String toString() {
		return statut;
	}

	public static EaeListeDto getValuesDto() {
		EaeListeDto res = new EaeListeDto();
		res.getListe().clear();
		for (EaeAgentStatutEnum e : values()) {
			ValeurListeDto v = new ValeurListeDto(e.name(), e.toString());
			res.getListe().add(v);

		}
		return res;
	}
}
