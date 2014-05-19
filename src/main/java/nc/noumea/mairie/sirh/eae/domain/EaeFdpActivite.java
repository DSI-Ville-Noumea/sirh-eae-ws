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

@Entity
@Table(name = "EAE_FDP_ACTIVITE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFdpActivite {

	@Id
	@Column(name = "ID_EAE_FDP_ACTIVITE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeFdpActivite;

	@Column(name = "LIBELLE_ACTIVITE")
	private String libelle;

	@ManyToOne
	@JoinColumn(name = "ID_EAE_FICHE_POSTE", referencedColumnName = "ID_EAE_FICHE_POSTE")
	private EaeFichePoste eaeFichePoste;

	public Integer getIdEaeFdpActivite() {
		return idEaeFdpActivite;
	}

	public void setIdEaeFdpActivite(Integer idEaeFdpActivite) {
		this.idEaeFdpActivite = idEaeFdpActivite;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public EaeFichePoste getEaeFichePoste() {
		return eaeFichePoste;
	}

	public void setEaeFichePoste(EaeFichePoste eaeFichePoste) {
		this.eaeFichePoste = eaeFichePoste;
	}
}
