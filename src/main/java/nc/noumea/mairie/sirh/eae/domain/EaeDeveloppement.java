package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeDeveloppementEnum;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_DEVELOPPEMENT", sequenceName = "EAE_S_DEVELOPPEMENT", identifierColumn = "ID_EAE_DEVELOPPEMENT", identifierField = "idEaeDeveloppement", identifierType = Integer.class)
public class EaeDeveloppement {
	
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
}
