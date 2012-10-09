package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_PARCOURS_PRO", identifierField = "idEaeParcoursPro", identifierType = Integer.class, table = "EAE_PARCOURS_PRO")
public class EaeParcoursPro {
	
	@Column(name = "DATE_DEBUT")
	private Date dateDebut;
	
	@Column(name = "DATE_FIN")
	private Date dateFin;
	
	@Column(name = "LIBELLE_PARCOURS_PRO")
	private String libelleParcoursPro;
	
	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	private Eae eae;
}
