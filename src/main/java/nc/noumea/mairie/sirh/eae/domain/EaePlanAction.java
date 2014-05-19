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
@Table(name = "EAE_PLAN_ACTION")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaePlanAction {

	@Id
	@Column(name = "ID_EAE_PLAN_ACTION")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaePlanAction;

	@Column(name = "OBJECTIF", length = 1000)
	private String objectif;

	@Column(name = "MESURE", length = 1000)
	private String mesure;

	@OneToOne
	@JoinColumn(name = "ID_EAE_TYPE_OBJECTIF")
	private EaeTypeObjectif typeObjectif;

	@OneToOne
	@JoinColumn(name = "ID_EAE")
	private Eae eae;

	public Integer getIdEaePlanAction() {
		return idEaePlanAction;
	}

	public void setIdEaePlanAction(Integer idEaePlanAction) {
		this.idEaePlanAction = idEaePlanAction;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getMesure() {
		return mesure;
	}

	public void setMesure(String mesure) {
		this.mesure = mesure;
	}

	public EaeTypeObjectif getTypeObjectif() {
		return typeObjectif;
	}

	public void setTypeObjectif(EaeTypeObjectif typeObjectif) {
		this.typeObjectif = typeObjectif;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
