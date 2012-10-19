package nc.noumea.mairie.sirh.eae.dto.identification;

import java.util.Date;

import nc.noumea.mairie.sirh.eae.domain.Eae;

import org.springframework.roo.addon.json.RooJson;

@RooJson
public class EaeIdentificationSituationDto {

	private String directionService;
	private String fonction;
	private String emploi;
	private Date dateEntreeAdministration;
	private Date dateEntreeFonction;
	private Date dateEntreeFonctionnaire;

	public EaeIdentificationSituationDto() {
		
	}
	
	public EaeIdentificationSituationDto(Eae eae) {
		this.directionService = eae.getEaeFichePoste().getDirectionService();
		this.fonction = eae.getEaeFichePoste().getFonction();
		this.emploi = eae.getEaeFichePoste().getEmploi();
		this.dateEntreeAdministration = eae.getEaeEvalue().getDateEntreeAdministration();
		this.dateEntreeFonction = eae.getEaeFichePoste().getDateEntreeFonction();
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
