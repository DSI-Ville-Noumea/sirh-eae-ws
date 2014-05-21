package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@Table(name = "EAE_AUTO_EVALUATION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeAutoEvaluation {

	@Id
	@Column(name = "ID_EAE_AUTO_EVALUATION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeAutoEvaluation;

	@Column(name = "PARTICULARITES")
	private String particularites;

	@Column(name = "ACQUIS")
	private String acquis;

	@Column(name = "SUCCES_DIFFICULTES")
	private String succesDifficultes;

	@OneToOne
	@JoinColumn(name = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeAutoEvaluation() {
		return idEaeAutoEvaluation;
	}

	public void setIdEaeAutoEvaluation(Integer idEaeAutoEvaluation) {
		this.idEaeAutoEvaluation = idEaeAutoEvaluation;
	}

	public String getParticularites() {
		return particularites;
	}

	public void setParticularites(String particularites) {
		this.particularites = particularites;
	}

	public String getAcquis() {
		return acquis;
	}

	public void setAcquis(String acquis) {
		this.acquis = acquis;
	}

	public String getSuccesDifficultes() {
		return succesDifficultes;
	}

	public void setSuccesDifficultes(String succesDifficultes) {
		this.succesDifficultes = succesDifficultes;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
