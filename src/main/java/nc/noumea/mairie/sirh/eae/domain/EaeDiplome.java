package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EAE_DIPLOME")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeDiplome {

	@Id
	@SequenceGenerator(name = "eaeDiplomeGen", sequenceName = "EAE_S_DIPLOME")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eaeDiplomeGen")
	@Column(name = "ID_EAE_DIPLOME")
	private Integer idEaeDiplome;

	@Column(name = "LIBELLE_DIPLOME", length = 255)
	@NotNull
	private String libelleDiplome;
	
	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;

	public Integer getIdEaeDiplome() {
		return idEaeDiplome;
	}

	public void setIdEaeDiplome(Integer idEaeDiplome) {
		this.idEaeDiplome = idEaeDiplome;
	}

	public String getLibelleDiplome() {
		return libelleDiplome;
	}

	public void setLibelleDiplome(String libelleDiplome) {
		this.libelleDiplome = libelleDiplome;
	}

	public Eae getEae() {
		return eae;
	}

	public void setEae(Eae eae) {
		this.eae = eae;
	}
}
