package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;

@Entity
@Table(name = "EAE_DEVELOPPEMENT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeDeveloppement {

	@Id
	@SequenceGenerator(name = "eaeDeveloppementGen", sequenceName = "EAE_S_DEVELOPPEMENT")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eaeDeveloppementGen")
	@Column(name = "ID_EAE_DEVELOPPEMENT")
	private Integer idEaeDeveloppement;

	@Column(name = "LIBELLE", length = 300)
	private String libelle;
	
	@Column(name = "ECHEANCE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date echeance;
	
	@Column(name = "PRIORISATION")
	private int priorisation;
	
	@Column(name = "TYPE_DEVELOPPEMENT")
	@Enumerated(EnumType.STRING)
	private EaeTypeDeveloppementEnum typeDeveloppement;
	
	@NotNull
	@OneToOne
    @JoinColumn(name = "ID_EAE_EVOLUTION")
	private EaeEvolution eaeEvolution;

	public Integer getIdEaeDeveloppement() {
		return idEaeDeveloppement;
	}

	public void setIdEaeDeveloppement(Integer idEaeDeveloppement) {
		this.idEaeDeveloppement = idEaeDeveloppement;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Date getEcheance() {
		return echeance;
	}

	public void setEcheance(Date echeance) {
		this.echeance = echeance;
	}

	public int getPriorisation() {
		return priorisation;
	}

	public void setPriorisation(int priorisation) {
		this.priorisation = priorisation;
	}

	public EaeTypeDeveloppementEnum getTypeDeveloppement() {
		return typeDeveloppement;
	}

	public void setTypeDeveloppement(EaeTypeDeveloppementEnum typeDeveloppement) {
		this.typeDeveloppement = typeDeveloppement;
	}

	public EaeEvolution getEaeEvolution() {
		return eaeEvolution;
	}

	public void setEaeEvolution(EaeEvolution eaeEvolution) {
		this.eaeEvolution = eaeEvolution;
	}
}
