package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_NIVEAU_EAE", identifierField = "idEaeNiveau", identifierType = Integer.class, table = "EAE_NIVEAU_EAE", versionField = "", sequenceName="EAE_S_NIVEAU_EAE")
public class EaeNiveau {
	
	@Column(name = "LIBELLE_NIVEAU_EAE")
	private String libelleNiveauEae;
}
