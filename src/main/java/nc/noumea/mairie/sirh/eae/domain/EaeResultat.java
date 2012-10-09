package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_RESULTAT", identifierField = "idEaeResultat", identifierType = Integer.class, table = "EAE_RESULTAT", sequenceName="EAE_S_RESULTAT")
public class EaeResultat {
	
	@Column(name = "OBJECTIF")
	private String objectif;
	
	@Column(name = "DETAIL_OBJECTIF")
	private String detailObjectif;
	
	@Column(name = "RESULTAT")
	private String resultat;
	
	@Column(name = "COMMENTAIRE")
	private String commentaire;
	
	@OneToOne
	@JoinColumn(name = "ID_EAE_TYPE_RESULTAT")
    private EaeTypeResultat typeResultat;
    
    @OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
}
