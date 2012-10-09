package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.domain.Agent;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_FICHE_POSTE", identifierField = "idEaeFichePoste", identifierType = Integer.class, table = "EAE_FICHE_POSTE")
public class EaeFichePoste {

	@Column(name = "TYPE_FDP")
    private String typeFdp;

    @Column(name = "DIRECTION_SERVICE")
    private String directionService;

    @Column(name = "SERVICE")
    private String service;

    @Column(name = "SECTION_SERVICE")
    private String sectionService;

    @Column(name = "EMPLOI")
    private String emploi;

    @Column(name = "FONCTION")
    private String fonction;

    @Column(name = "DATE_ENTREE_FONCTION")
    private Date dateEntreeFonction;

    @Column(name = "GRADE_POSTE")
    private String gradePoste;

    @Column(name = "LOCALISATION")
    private String localisation;

    @Column(name = "FONCTION_RESP")
    private String fonctionResponsable;

    @Column(name = "ID_SHD")
    private Integer idAgentShd;
     
    @OneToOne(optional = false)
    @JoinColumn(name = "ID_EAE", unique = true, nullable = false)
    private Eae eae;
    
    @OneToMany(mappedBy = "eaeFichePoste", fetch = FetchType.LAZY)
	private Set<EaeFdpActivite> eaeFdbActivites;
    
    @Transient
    private Agent agentShd;
}
