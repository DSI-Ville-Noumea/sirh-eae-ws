package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;

@Entity
@Table(name = "EAE_NIVEAU")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeNiveau {

	@Id
	@Column(name = "ID_EAE_NIVEAU")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeNiveau;

	@Column(name = "LIBELLE_NIVEAU_EAE")
	private String libelleNiveauEae;

	@Transient
	public EaeNiveauEnum getEaeNiveauAsEnum() {
		return EaeNiveauEnum.valueOf(libelleNiveauEae);
	}

	public Integer getIdEaeNiveau() {
		return idEaeNiveau;
	}

	public void setIdEaeNiveau(Integer idEaeNiveau) {
		this.idEaeNiveau = idEaeNiveau;
	}

	public String getLibelleNiveauEae() {
		return libelleNiveauEae;
	}

	public void setLibelleNiveauEae(String libelleNiveauEae) {
		this.libelleNiveauEae = libelleNiveauEae;
	}
}
