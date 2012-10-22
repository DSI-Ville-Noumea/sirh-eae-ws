package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_RESULTAT", identifierField = "idEaeResultat", identifierType = Integer.class, table = "EAE_RESULTAT", sequenceName="EAE_S_RESULTAT")
public class EaeResultat {
	
	@Column(name = "OBJECTIF")
	private String objectif;
	
	@Column(name = "RESULTAT")
	private String resultat;

	@OneToOne
	@JoinColumn(name = "ID_EAE_COMMENTAIRE")
	private EaeCommentaire commentaire;
	
	@OneToOne
	@JoinColumn(name = "ID_EAE_TYPE_OBJECTIF")
	@NotNull
    private EaeTypeObjectif typeObjectif;
    
    @OneToOne
    @JoinColumn(name = "ID_EAE")
    @NotNull
    private Eae eae;
}
