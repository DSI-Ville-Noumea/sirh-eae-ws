package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.domain.Agent;

@Entity
@Table(name = "EAE_EVALUATEUR")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeEvaluateur {

	@Id
	@Column(name = "ID_EAE_EVALUATEUR")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeEvaluateur;

	@Column(name = "ID_AGENT")
	private int idAgent;

	@Column(name = "FONCTION")
	private String fonction;

	@Column(name = "DATE_ENTREE_SERVICE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeService;

	@Column(name = "DATE_ENTREE_COLLECTIVITE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeCollectivite;

	@Column(name = "DATE_ENTREE_FONCTION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateEntreeFonction;

	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;

	/*
	 * Transient properties (will be populated by AS400 entity manager)
	 */
	@Transient
	private Agent agent;

	public Integer getIdEaeEvaluateur() {
		return idEaeEvaluateur;
	}

	public void setIdEaeEvaluateur(Integer idEaeEvaluateur) {
		this.idEaeEvaluateur = idEaeEvaluateur;
	}

	public int getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(int idAgent) {
		this.idAgent = idAgent;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public Date getDateEntreeService() {
		return dateEntreeService;
	}

	public void setDateEntreeService(Date dateEntreeService) {
		this.dateEntreeService = dateEntreeService;
	}

	public Date getDateEntreeCollectivite() {
		return dateEntreeCollectivite;
	}

	public void setDateEntreeCollectivite(Date dateEntreeCollectivite) {
		this.dateEntreeCollectivite = dateEntreeCollectivite;
	}

	public Date getDateEntreeFonction() {
		return dateEntreeFonction;
	}

	public void setDateEntreeFonction(Date dateEntreeFonction) {
		this.dateEntreeFonction = dateEntreeFonction;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}
}
