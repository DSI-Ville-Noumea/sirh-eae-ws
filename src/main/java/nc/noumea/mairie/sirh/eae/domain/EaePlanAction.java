package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_PLAN_ACTION", identifierField = "idEaePlanAction", identifierType = Integer.class, table = "EAE_PLAN_ACTION", sequenceName="EAE_S_PLAN_ACTION")
public class EaePlanAction {
	
	@Column(name = "OBJECTIF")
	private String objectif;
	
	@Column(name = "MESURE")
	private String mesure;
	
	@OneToOne
	@JoinColumn(name = "ID_EAE_TYPE_OBJECTIF")
    private EaeTypeObjectif typeObjectif;
    
    @OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
}
