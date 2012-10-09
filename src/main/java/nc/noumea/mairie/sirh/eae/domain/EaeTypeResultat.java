package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_TYPE_RESULTAT", identifierField = "idEaeTypeResultat", identifierType = Integer.class, table = "EAE_TYPE_RESULTAT", versionField = "")
public class EaeTypeResultat {
	
	@Column(name = "LIBELLE_TYPE_RESULTAT")
	private String libelleTypeResultat;
}
