package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Lob;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_COMMENTAIRE", identifierColumn = "ID_EAE_COMMENTAIRE", identifierField = "idEaeCommentaire", identifierType = Integer.class, sequenceName = "EAE_S_COMMENTAIRE")
public class EaeCommentaire {
	
	@Column(name = "TEXT")
	@Lob
	private String text;
}
