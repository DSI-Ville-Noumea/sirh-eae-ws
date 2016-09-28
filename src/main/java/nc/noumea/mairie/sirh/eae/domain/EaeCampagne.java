package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

@Entity
@Table(name = "EAE_CAMPAGNE_EAE")
@PersistenceUnit(unitName = "eaePersistenceUnit")
@NamedQueries({ @NamedQuery(name = "getListeCampagneEae", query = "select e from EaeCampagne e order by annee desc ") })
public class EaeCampagne {

	@Id
	@Column(name = "ID_CAMPAGNE_EAE")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer					idCampagneEae;

	@Column(name = "ANNEE")
	private int						annee;

	@Column(name = "DATE_DEBUT")
	private Date					dateDebut;

	@Column(name = "DATE_FIN")
	private Date					dateFin;

	@Column(name = "DATE_OUVERTURE_KIOSQUE")
	private Date					dateOuvertureKiosque;

	@Column(name = "DATE_FERMETURE_KIOSQUE")
	private Date					dateFermetureKiosque;

	@Column(name = "COMMENTAIRE")
	private String					commentaire;

	@OneToMany(mappedBy = "eaeCampagne", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeCampagneAction>	listeCampagneAction	= new HashSet<EaeCampagneAction>();
	

	@OneToMany(mappedBy = "eaeCampagne", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Where(clause = " TYPE_DOCUMENT = 'CAMP' ")
	private Set<EaeDocument>		listeDocument		= new HashSet<EaeDocument>();

	public Integer getIdCampagneEae() {
		return idCampagneEae;
	}

	public void setIdCampagneEae(Integer idCampagneEae) {
		this.idCampagneEae = idCampagneEae;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public Date getDateOuvertureKiosque() {
		return dateOuvertureKiosque;
	}

	public void setDateOuvertureKiosque(Date dateOuvertureKiosque) {
		this.dateOuvertureKiosque = dateOuvertureKiosque;
	}

	public Date getDateFermetureKiosque() {
		return dateFermetureKiosque;
	}

	public void setDateFermetureKiosque(Date dateFermetureKiosque) {
		this.dateFermetureKiosque = dateFermetureKiosque;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public Set<EaeCampagneAction> getListeCampagneAction() {
		return listeCampagneAction;
	}

	public void setListeCampagneAction(Set<EaeCampagneAction> listeCampagneAction) {
		this.listeCampagneAction = listeCampagneAction;
	}

	public Set<EaeDocument> getListeDocument() {
		return listeDocument;
	}

	public void setListeDocument(Set<EaeDocument> listeDocument) {
		this.listeDocument = listeDocument;
	}

}
