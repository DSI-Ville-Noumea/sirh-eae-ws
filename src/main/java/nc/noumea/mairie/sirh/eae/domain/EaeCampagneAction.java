package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EAE_CAMPAGNE_ACTION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeCampagneAction {

	@Id
	@Column(name = "id_campagne_action")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer						idCampagneAction;

	@OneToOne
	@JoinColumn(name = "ID_CAMPAGNE_EAE", referencedColumnName = "ID_CAMPAGNE_EAE")
	private EaeCampagne					eaeCampagne;

	@NotNull
	@Column(name = "nom_action")
	private String						nomAction;

	@NotNull
	@Column(name = "date_transmission")
	private Date						dateTransmission;

	@Column(name = "date_a_faire_le")
	@Temporal(TemporalType.TIMESTAMP)
	private Date						dateAFaireLe;

	@Column(name = "date_fait_le")
	@Temporal(TemporalType.TIMESTAMP)
	private Date						dateFaitLe;

	@Column(name = "commentaire")
	private String						commentaire;

	@NotNull
	@Column(name = "id_agent_realisation")
	private Integer						idAgentRealisation;

	@Column(name = "message")
	private String						message;

	@Column(name = "date_mail_envoye")
	private Date						dateMailEnvoye;

	@OneToMany(mappedBy = "campagneAction", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EaeCampagneActeurs>	listActeurs;

	public Integer getIdCampagneAction() {
		return idCampagneAction;
	}

	public void setIdCampagneAction(Integer idCampagneAction) {
		this.idCampagneAction = idCampagneAction;
	}

	public EaeCampagne getEaeCampagne() {
		return eaeCampagne;
	}

	public void setEaeCampagne(EaeCampagne eaeCampagne) {
		this.eaeCampagne = eaeCampagne;
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

	public List<EaeCampagneActeurs> getListActeurs() {
		return listActeurs;
	}

	public void setListActeurs(List<EaeCampagneActeurs> listActeurs) {
		this.listActeurs = listActeurs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCampagneAction == null) ? 0 : idCampagneAction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EaeCampagneAction other = (EaeCampagneAction) obj;
		if (idCampagneAction == null) {
			if (other.idCampagneAction != null)
				return false;
		} else if (!idCampagneAction.equals(other.idCampagneAction))
			return false;
		return true;
	}

}
