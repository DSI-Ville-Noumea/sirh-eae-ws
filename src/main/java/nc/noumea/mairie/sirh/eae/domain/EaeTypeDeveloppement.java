package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_TYPE_DEVELOPPEMENT", sequenceName = "EAE_S_TYPE_DEVELOPPEMENT", identifierColumn = "ID_EAE_TYPE_DEVELOPPEMENT", identifierField = "idEaeTypeDeveloppement", identifierType = Integer.class, versionField = "")
public class EaeTypeDeveloppement {
	
	@Column(name = "LIBELLE_TYPE_DEVELOPPEMENT")
	private String libelle;
	
}
