package nc.noumea.mairie.sirh.eae.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nc.noumea.mairie.sirh.eae.domain.EaeCampagneActeurs;
import nc.noumea.mairie.sirh.eae.domain.EaeCampagneAction;

public class EaeCampagneActionDto implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5046144068295561176L;
	
	private Integer idCampagneAction;
	private String nomAction;
	private Date dateTransmission;
	private Date dateAFaireLe;
	private Date dateFaitLe;
	private String commentaire;
	private Integer idAgentRealisation;
	private String message;
	private Date dateMailEnvoye;
	private List<EaeCampagneActeursDto> listeCampagneActeurs;
	private List<EaeDocumentDto> listeEaeDocument;
	
	public EaeCampagneActionDto(){
		this.listeCampagneActeurs = new ArrayList<EaeCampagneActeursDto>();
		this.listeEaeDocument = new ArrayList<EaeDocumentDto>();
	}
	
	public EaeCampagneActionDto(EaeCampagneAction campagneAction) {
		this();
		this.idCampagneAction = campagneAction.getIdCampagneAction();
		this.nomAction = campagneAction.getNomAction();
		this.dateTransmission = campagneAction.getDateTransmission();
		this.dateAFaireLe = campagneAction.getDateAFaireLe();
		this.dateFaitLe = campagneAction.getDateFaitLe();
		this.commentaire = campagneAction.getCommentaire();
		this.idAgentRealisation = campagneAction.getIdAgentRealisation();
		this.message = campagneAction.getMessage();
		this.dateMailEnvoye = campagneAction.getDateMailEnvoye();
		
		if(null != campagneAction.getListActeurs()
				&& !campagneAction.getListActeurs().isEmpty()) {
			for(EaeCampagneActeurs acteurs : campagneAction.getListActeurs()) {
				EaeCampagneActeursDto acteurDto = new EaeCampagneActeursDto(acteurs);
				this.listeCampagneActeurs.add(acteurDto);
			}
		}
	}
	
	public Integer getIdCampagneAction() {
		return idCampagneAction;
	}
	public void setIdCampagneAction(Integer idCampagneAction) {
		this.idCampagneAction = idCampagneAction;
	}
	public String getNomAction() {
		return nomAction;
	}
	public void setNomAction(String nomAction) {
		this.nomAction = nomAction;
	}
	public Date getDateTransmission() {
		return dateTransmission;
	}
	public void setDateTransmission(Date dateTransmission) {
		this.dateTransmission = dateTransmission;
	}
	public Date getDateAFaireLe() {
		return dateAFaireLe;
	}
	public void setDateAFaireLe(Date dateAFaireLe) {
		this.dateAFaireLe = dateAFaireLe;
	}
	public Date getDateFaitLe() {
		return dateFaitLe;
	}
	public void setDateFaitLe(Date dateFaitLe) {
		this.dateFaitLe = dateFaitLe;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public Integer getIdAgentRealisation() {
		return idAgentRealisation;
	}
	public void setIdAgentRealisation(Integer idAgentRealisation) {
		this.idAgentRealisation = idAgentRealisation;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getDateMailEnvoye() {
		return dateMailEnvoye;
	}
	public void setDateMailEnvoye(Date dateMailEnvoye) {
		this.dateMailEnvoye = dateMailEnvoye;
	}

	public List<EaeCampagneActeursDto> getListeCampagneActeurs() {
		return listeCampagneActeurs;
	}

	public void setListeCampagneActeurs(
			List<EaeCampagneActeursDto> listeCampagneActeurs) {
		this.listeCampagneActeurs = listeCampagneActeurs;
	}

	public List<EaeDocumentDto> getListeEaeDocument() {
		return listeEaeDocument;
	}

	public void setListeEaeDocument(List<EaeDocumentDto> listeEaeDocument) {
		this.listeEaeDocument = listeEaeDocument;
	}
	
}
