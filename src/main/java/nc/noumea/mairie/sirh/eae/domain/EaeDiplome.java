package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_DIPLOME", identifierField = "idEaeDiplome", table = "EAE_DIPLOME")
public class EaeDiplome {

	@Column(name = "LIBELLE_DIPLOME")
	@NotNull
	private String libelleDiplome;
}
