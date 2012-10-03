package nc.moumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.PersistenceContext;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit="eaeOraclePersistenceUnit", identifierColumn = "ID_EAE", identifierField = "idEae", identifierType = Integer.class, table = "EAE")
@RooJson
public class Eae {

	@Column(name = "AGENT")
    private String Agent;

}
