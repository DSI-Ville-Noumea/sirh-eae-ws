package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(identifierColumn = "ID_CAMPAGNE_EAE", identifierField = "idCampagneEae", identifierType = Integer.class, table = "EAE_CAMPAGNE_EAE", persistenceUnit = "eaePersistenceUnit", versionField = "", sequenceName = "EAE_S_CAMPAGNE_EAE" , clearMethod = "", countMethod = "", findAllMethod = "", findMethod = "", flushMethod = "", removeMethod = "")
public class EaeCampagne {
	
	@Column(name = "ANNEE")
	private int annee;
	
	@Column(name = "DATE_DEBUT")
	private Date dateDebut;

	@Column(name = "DATE_FIN")
	private Date dateFin;

	@Column(name = "DATE_OUVERTURE_KIOSQUE")
	private Date dateOuvertureKiosque;

	@Column(name = "DATE_FERMETURE_KIOSQUE")
	private Date dateFermetureKiosque;
}
