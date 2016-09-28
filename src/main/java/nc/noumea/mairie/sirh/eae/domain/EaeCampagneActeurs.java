package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "eae_campagne_acteurs")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeCampagneActeurs {

	@Id
	@Column(name = "id_campagne_acteurs")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idCampagneActeurs;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "id_campagne_action", referencedColumnName = "id_campagne_action")
	private EaeCampagneAction campagneAction;
	
	@NotNull
	@Column(name = "id_agent")
	private Integer idAgent;

	public Integer getIdCampagneActeurs() {
		return idCampagneActeurs;
	}

	public void setIdCampagneActeurs(Integer idCampagneActeurs) {
		this.idCampagneActeurs = idCampagneActeurs;
	}

	public EaeCampagneAction getCampagneAction() {
		return campagneAction;
	}

	public void setCampagneAction(EaeCampagneAction campagneAction) {
		this.campagneAction = campagneAction;
	}

	public Integer getIdAgent() {
		return idAgent;
	}

	public void setIdAgent(Integer idAgent) {
		this.idAgent = idAgent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCampagneActeurs == null) ? 0 : idCampagneActeurs.hashCode());
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
		EaeCampagneActeurs other = (EaeCampagneActeurs) obj;
		if (idCampagneActeurs == null) {
			if (other.idCampagneActeurs != null)
				return false;
		} else if (!idCampagneActeurs.equals(other.idCampagneActeurs))
			return false;
		return true;
	}
	
}
