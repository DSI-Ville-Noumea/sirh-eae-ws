package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_AUTO_EVALUATION", sequenceName = "EAE_S_AUTO_EVALUATION", identifierColumn = "ID_EAE_AUTO_EVALUATION", identifierField = "idEaeAutoEvaluation", identifierType = Integer.class)
public class EaeAutoEvaluation {
	
	@Column(name = "PARTICULARITES")
	@Lob
	private String particularites;
	
	@Column(name = "ACQUIS")
	@Lob
	private String acquis;
	
	@Column(name = "SUCCES_DIFFICULTES")
	@Lob
	private String succesDifficultes;
	
	@OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
}
