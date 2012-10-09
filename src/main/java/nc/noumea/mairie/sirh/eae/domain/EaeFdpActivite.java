package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_FDP_ACTIVITE", identifierField = "idEaeFdpActivite", identifierType = Integer.class, table = "EAE_FDP_ACTIVITE", sequenceName="EAE_S_FDP_ACTIVITE")
public class EaeFdpActivite {

    @Column(name = "TYPE_ACTIVITE")
    private String typeActivite;

    @Column(name = "LIBELLE_ACTIVITE")
    private String libelleActivite;
    
    @ManyToOne
	@JoinColumn(name = "ID_EAE_FICHE_POSTE", referencedColumnName = "ID_EAE_FICHE_POSTE")
    private EaeFichePoste eaeFichePoste;
}
