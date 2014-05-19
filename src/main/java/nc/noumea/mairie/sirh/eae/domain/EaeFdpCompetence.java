package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;

@Entity
@Table(name = "EAE_FDP_COMPETENCE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFdpCompetence {

	@Id
	@Column(name = "ID_EAE_FDP_COMPETENCE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeFdpCompetence;

	@Column(name = "TYPE_COMPETENCE")
	@Enumerated(EnumType.STRING)
	private EaeTypeCompetenceEnum type;

	@Column(name = "LIBELLE_COMPETENCE")
	private String libelle;

	@ManyToOne
	@JoinColumn(name = "ID_EAE_FICHE_POSTE", referencedColumnName = "ID_EAE_FICHE_POSTE")
	private EaeFichePoste eaeFichePoste;

	public Integer getIdEaeFdpCompetence() {
		return idEaeFdpCompetence;
	}

	public void setIdEaeFdpCompetence(Integer idEaeFdpCompetence) {
		this.idEaeFdpCompetence = idEaeFdpCompetence;
	}

	public EaeTypeCompetenceEnum getType() {
		return type;
	}

	public void setType(EaeTypeCompetenceEnum type) {
		this.type = type;
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
