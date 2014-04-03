package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.domain.Agent;

import org.hibernate.annotations.Type;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_FICHE_POSTE", identifierField = "idEaeFichePoste", identifierType = Integer.class, table = "EAE_FICHE_POSTE", sequenceName = "EAE_S_FICHE_POSTE")
public class EaeFichePoste {

    @Column(name = "PRIMAIRE", nullable = false)
    @Type(type="boolean")
    private boolean primary;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeFonction;
    
    @Column(name = "DATE_ENTREE_COLLECT_RESP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeCollectiviteResponsable;
    
    @Column(name = "DATE_ENTREE_FONCTION_RESP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeFonctionResponsable;
    
    @Column(name = "DATE_ENTREE_SERVICE_RESP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeServiceResponsable;

    @Column(name = "GRADE_POSTE")
    private String gradePoste;

    @Column(name = "LOCALISATION")
    private String localisation;

    @Column(name = "FONCTION_RESP")
    private String fonctionResponsable;

    @Column(name = "ID_SHD")
    private Integer idAgentShd;
    
    @Column(name = "ID_SIRH_FICHE_POSTE")
    private Integer idSirhFichePoste;
    
    @Column(name = "MISSIONS")
    @Lob
    private String missions;

    @ManyToOne
    @JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
    private Eae eae;

    @OneToMany(mappedBy = "eaeFichePoste", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EaeFdpActivite> eaeFdpActivites = new HashSet<EaeFdpActivite>();
    
    @OneToMany(mappedBy = "eaeFichePoste", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EaeFdpCompetence> eaeFdpCompetences = new HashSet<EaeFdpCompetence>();

    @Column(name = "CODE_SERVICE")
    private String codeService; 
    
    @Transient
    private Agent agentShd;
}
