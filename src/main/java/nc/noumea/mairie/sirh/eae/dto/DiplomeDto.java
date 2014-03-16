package nc.noumea.mairie.sirh.eae.dto;

import java.util.Date;

public class DiplomeDto {
	
	private Integer idDiplome;
	private Date dateObtention;
	private String LibTitreDiplome;
	private String LibSpeDiplome;
	
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
		return LibTitreDiplome;
	}
	public void setLibTitreDiplome(String libTitreDiplome) {
		LibTitreDiplome = libTitreDiplome;
	}
	public String getLibSpeDiplome() {
		return LibSpeDiplome;
	}
	public void setLibSpeDiplome(String libSpeDiplome) {
		LibSpeDiplome = libSpeDiplome;
	}
	
	
}
