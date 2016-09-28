package nc.noumea.mairie.sirh.eae.dto.identification;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateDeserializer;
import nc.noumea.mairie.sirh.tools.transformer.JsonDateSerializer;

public class EaeIdentificationSituationDto {
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntreeAdministration;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntreeFonction;
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date dateEntreeFonctionnaire;
	private String directionService;
	private String fonction;
	private String emploi;

	public EaeIdentificationSituationDto() {
		
	}
	
	public EaeIdentificationSituationDto(Eae eae) {
		EaeFichePoste fdp = eae.getPrimaryFichePoste();
		
		if (fdp != null) {
			this.directionService = fdp.getDirectionService();
			this.fonction = fdp.getFonction();
			this.emploi = fdp.getEmploi();
			this.dateEntreeFonction = fdp.getDateEntreeFonction();
		}
		
		this.dateEntreeAdministration = eae.getEaeEvalue().getDateEntreeAdministration();
		this.dateEntreeFonctionnaire = eae.getEaeEvalue().getDateEntreeFonctionnaire();
	}
	
	public String getDirectionService() {
		return directionService;
	}

	public void setDirectionService(String directionService) {
		this.directionService = directionService;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public String getEmploi() {
		return emploi;
	}

	public void setEmploi(String emploi) {
		this.emploi = emploi;
	}

	public Date getDateEntreeAdministration() {
		return dateEntreeAdministration;
	}

	public void setDateEntreeAdministration(Date dateEntreeAdministration) {
		this.dateEntreeAdministration = dateEntreeAdministration;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public Date getDateEntreeFonctionnaire() {
		return dateEntreeFonctionnaire;
	}

	public void setDateEntreeFonctionnaire(Date dateEntreeFonctionnaire) {
		this.dateEntreeFonctionnaire = dateEntreeFonctionnaire;
	}
}
