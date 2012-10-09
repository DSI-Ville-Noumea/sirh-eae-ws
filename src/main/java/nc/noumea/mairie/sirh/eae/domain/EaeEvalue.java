package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_EVALUE", identifierField = "idEaeEvalue", identifierType = Integer.class, table = "EAE_EVALUE", sequenceName="EAE_S_EVALUE")
public class EaeEvalue {

    @Column(name = "ID_AGENT")
    private int idAgent;

    @Column(name = "DATE_ENTREE_SERVICE")
    private Date dateEntreeService;

    @Column(name = "DATE_ENTREE_COLLECTIVITE")
    private Date dateEntreeCollectivite;

    @Column(name = "DATE_ENTREE_FONCTIONNAIRE")
    private Date dateEntreeFonctionnaire;

    @Column(name = "DATE_ENTREE_ADMINISTRATION")
    private Date dateEntreeAdministration;

    @Column(name = "STATUT")
    private String statut;

    @Column(name = "ANCIENNETE_ECHELON")
    private Integer ancienneteEchelon;

    @Column(name = "CADRE")
    private String cadre;

    @Column(name = "CATEGORIE")
    private String categorie;

    @Column(name = "CLASSIFICATION")
    private String classification;

    @Column(name = "GRADE")
    private String grade;

    @Column(name = "ECHELON")
    private String echelon;

    @Column(name = "DATE_EFFET_AVCT")
    private Date dateEffetAvancement;

    @Column(name = "NOUV_GRADE")
    private String nouvGrade;

    @Column(name = "NOUV_ECHELON")
    private String nouvEchelon;

    @Column(name = "TYPE_AVCT")
    private Integer typeAvancement;
    
    @OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
}