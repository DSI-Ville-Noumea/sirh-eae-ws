package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.eae.dto.EaeResultatDto;

@Entity
@Table(name = "EAE_RESULTAT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeResultat {

	@Id
	@Column(name = "ID_EAE_RESULTAT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeResultat;

	@Column(name = "OBJECTIF", length = 1000)
	private String objectif;

	@Column(name = "RESULTAT", length = 1000)
	private String resultat;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "ID_EAE_COMMENTAIRE")
	private EaeCommentaire commentaire;

	@OneToOne
	@JoinColumn(name = "ID_EAE_TYPE_OBJECTIF")
	@NotNull
	private EaeTypeObjectif typeObjectif;

	@OneToOne
	@JoinColumn(name = "ID_EAE")
	@NotNull
	private Eae eae;

	public EaeResultat() {
	}
	
	public EaeResultat(EaeResultatDto dto) {
		this.idEaeResultat = dto.getIdEaeResultat();
		this.objectif = dto.getObjectif();
		this.resultat = dto.getResultat();
		
		if(null != dto.getCommentaire()) {
			this.commentaire = new EaeCommentaire(dto.getCommentaire());
		}
	}
	
	public Integer getIdEaeResultat() {
		return idEaeResultat;
	}

	public void setIdEaeResultat(Integer idEaeResultat) {
		this.idEaeResultat = idEaeResultat;
	}

	public String getObjectif() {
		return objectif;
	}

	public void setObjectif(String objectif) {
		this.objectif = objectif;
	}

	public String getResultat() {
		return resultat;
	}

	public void setResultat(String resultat) {
		this.resultat = resultat;
	}

	public EaeCommentaire getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(EaeCommentaire commentaire) {
		this.commentaire = commentaire;
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
