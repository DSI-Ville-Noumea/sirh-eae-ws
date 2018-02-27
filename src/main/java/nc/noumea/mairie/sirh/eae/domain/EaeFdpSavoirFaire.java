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
@Table(name = "EAE_FDP_SAVOIR_FAIRE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFdpSavoirFaire {

	@Id
	@Column(name = "ID_EAE_FDP_SAVOIR_FAIRE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeFdpSavoirFaire;

	@Column(name = "LIBELLE")
	private String libelle;

	@ManyToOne
	@JoinColumn(name = "ID_EAE_FDP_ACTIVITE_METIER", referencedColumnName = "ID_EAE_FDP_ACTIVITE_METIER")
	private EaeFdpActiviteMetier eaeActiviteMetier;
	
	public EaeFdpSavoirFaire() {
		super();
	}
	
	public EaeFdpSavoirFaire(String libelle) {
		this.libelle = libelle;
	}

	public Integer getIdEaeFdpSavoirFaire() {
		return idEaeFdpSavoirFaire;
	}

	public void setIdEaeFdpSavoirFaire(Integer idEaeFdpSavoirFaire) {
		this.idEaeFdpSavoirFaire = idEaeFdpSavoirFaire;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public EaeFdpActiviteMetier getEaeActiviteMetier() {
		return eaeActiviteMetier;
	}

	public void setEaeActiviteMetier(EaeFdpActiviteMetier eaeActiviteMetier) {
		this.eaeActiviteMetier = eaeActiviteMetier;
	}
	
	
}
