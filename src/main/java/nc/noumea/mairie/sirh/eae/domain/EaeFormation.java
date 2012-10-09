package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_FORMATION", identifierField = "idEaeFormation", identifierType = Integer.class, table = "EAE_FORMATION")
public class EaeFormation {
	
	@Column(name = "ANNEE_FORMATION")
	private int anneeFormation;
	
	@Column(name = "DUREE_FORMATION")
	private int dureeFormation;
	
	@Column(name = "LIBELLE_FORMATION")
	private String libelleFormation;
	
	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;
}
