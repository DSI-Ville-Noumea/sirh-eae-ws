package nc.noumea.mairie.sirh.eae.domain;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_AUTO_EVALUATION", sequenceName = "EAE_S_AUTO_EVALUATION", identifierColumn = "ID_EAE_AUTO_EVALUATION", identifierField = "idEaeAutoEvaluation", identifierType = Integer.class)
public class EaeAutoEvaluation {
	
//	@Column(name = "PARTICULARITES")
//	private Set<String> particularites;
//	
//	@Column(name = "ACQUIS")
//	private Set<String> aquis;
//	
//	@Column(name = "SUCCESS_DIFFICULTES")
//	private Set<String> succesDifficultes;
}
