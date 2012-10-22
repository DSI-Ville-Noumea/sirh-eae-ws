package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeCompetenceEnum;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_FDP_COMPETENCE", identifierField = "idEaeFdpCompetence", identifierType = Integer.class, table = "EAE_FDP_COMPETENCE", sequenceName="EAE_S_FDP_COMPETENCE")
public class EaeFdpCompetence {
	
	@Column(name = "TYPE_COMPETENCE")
	@Enumerated(EnumType.STRING)
    private EaeTypeCompetenceEnum type;

    @Column(name = "LIBELLE_COMPETENCE")
    private String libelle;
    
    @ManyToOne
	@JoinColumn(name = "ID_EAE_FICHE_POSTE", referencedColumnName = "ID_EAE_FICHE_POSTE")
    private EaeFichePoste eaeFichePoste;
    
    /*
     * Transient properties
     */
    public String getFullLabel() {
    	return String.format("%s - %s", type.toString(), libelle);
    }
}
