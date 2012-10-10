package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_TYPE_OBJECTIF", identifierField = "idEaeTypeObjectif", identifierType = Integer.class, table = "EAE_TYPE_OBJECTIF", versionField = "", sequenceName="EAE_S_TYPE_OBJECTIF")
public class EaeTypeObjectif {
	
	@Column(name = "LIBELLE_TYPE_OBJECTIF")
	private String libelleTypeObjectif;
}
