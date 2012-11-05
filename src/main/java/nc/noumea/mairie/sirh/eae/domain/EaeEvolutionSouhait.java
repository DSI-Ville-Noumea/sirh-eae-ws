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
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_EVOL_SOUHAIT", sequenceName = "EAE_S_EVOL_SOUHAIT", identifierColumn = "ID_EAE_EVOL_SOUHAIT", identifierField = "idEaeEvolutionSouhait", identifierType = Integer.class)
public class EaeEvolutionSouhait {
	
	@Column(name = "LIB_SOUHAIT")
	@Lob
	private String souhait;
	
	@Column(name = "LIB_SUGGESTION")
	@Lob
	private String suggestion;
	
	@OneToOne
    @JoinColumn(name = "ID_EAE_EVOLUTION")
	private EaeEvolution eaeEvolution;
}
