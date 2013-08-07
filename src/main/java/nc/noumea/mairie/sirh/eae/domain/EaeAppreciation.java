package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAppreciationEnum;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_APPRECIATION", sequenceName = "EAE_S_APPRECIATION", identifierColumn = "ID_EAE_APPRECIATION", identifierField = "idEaeAppreciation", identifierType = Integer.class)
public class EaeAppreciation {

	@Column(name = "TYPE_APPRECIATION")
	@NotNull
	@Enumerated(EnumType.STRING)
	private EaeTypeAppreciationEnum typeAppreciation;

	@Column(name = "NUMERO")
	@NotNull
	private int numero;

	@Column(name = "NOTE_EVALUE")
	private String noteEvalue;

	@Column(name = "NOTE_EVALUATEUR")
	private String noteEvaluateur;

	@ManyToOne
	@JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
	@NotNull
	private Eae eae;
}
