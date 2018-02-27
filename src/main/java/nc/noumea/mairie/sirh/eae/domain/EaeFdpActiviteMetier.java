package nc.noumea.mairie.sirh.eae.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

@Entity
@Table(name = "EAE_FDP_ACTIVITE_METIER")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeFdpActiviteMetier {

	@Id
	@Column(name = "ID_EAE_FDP_ACTIVITE_METIER")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeFdpActiviteMetier;

	@OneToMany(mappedBy = "eaeActiviteMetier", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("libelle asc")
	private Set<EaeFdpSavoirFaire> savoirFaire = new HashSet<>();

	@Column(name = "LIBELLE")
	private String libelle;

	@ManyToOne
	@JoinColumn(name = "ID_EAE_FICHE_POSTE", referencedColumnName = "ID_EAE_FICHE_POSTE")
	private EaeFichePoste eaeFichePoste;
	
	public EaeFdpActiviteMetier() {
		super();
	}
	
	public EaeFdpActiviteMetier(String libelle) {
		this.libelle = libelle;
	}

	public Integer getIdEaeFdpActiviteMetier() {
		return idEaeFdpActiviteMetier;
	}

	public void setIdEaeFdpActiviteMetier(Integer idEaeFdpActiviteMetier) {
		this.idEaeFdpActiviteMetier = idEaeFdpActiviteMetier;
	}

	public Set<EaeFdpSavoirFaire> getSavoirFaire() {
		return savoirFaire;
	}

	public void setSavoirFaire(Set<EaeFdpSavoirFaire> savoirFaire) {
		this.savoirFaire = savoirFaire;
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		EaeFdpActiviteMetier comp = (EaeFdpActiviteMetier) obj;
		
		if (comp.getLibelle() == null || comp.getEaeFichePoste() == null)
			return false;

		return comp.getLibelle().equals(this.libelle) && comp.getEaeFichePoste().equals(this.getEaeFichePoste());
	}
}
