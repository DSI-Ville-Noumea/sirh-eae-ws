package nc.noumea.mairie.sirh.eae.dto.agent;

import java.util.Date;

public class DiplomeDto {
	
	private Integer idDiplome;
	private Date dateObtention;
	private String libTitreDiplome;
	private String libSpeDiplome;
	
	public Integer getIdDiplome() {
		return idDiplome;
	}
	public void setIdDiplome(Integer idDiplome) {
		this.idDiplome = idDiplome;
	}
	public Date getDateObtention() {
		return dateObtention;
	}
	public void setDateObtention(Date dateObtention) {
		this.dateObtention = dateObtention;
	}
	public String getLibTitreDiplome() {
		return libTitreDiplome;
	}
	public void setLibTitreDiplome(String libTitreDiplome) {
		this.libTitreDiplome = libTitreDiplome;
	}
	public String getLibSpeDiplome() {
		return libSpeDiplome;
	}
	public void setLibSpeDiplome(String libSpeDiplome) {
		this.libSpeDiplome = libSpeDiplome;
	}
	
	
}
