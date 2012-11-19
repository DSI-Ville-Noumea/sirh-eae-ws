package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_NIVEAU", identifierField = "idEaeNiveau", identifierType = Integer.class, table = "EAE_NIVEAU", versionField = "", sequenceName="EAE_S_NIVEAU")
public class EaeNiveau {
	
	@Column(name = "LIBELLE_NIVEAU_EAE")
	private String libelleNiveauEae;
	
	@Transient
	public EaeNiveauEnum getEaeNiveauAsEnum() {
    	return EaeNiveauEnum.valueOf(libelleNiveauEae);
    }
}
