package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAvctEnum;

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
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeService;

    @Column(name = "DATE_ENTREE_COLLECTIVITE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeCollectivite;

    @Column(name = "DATE_ENTREE_FONCTIONNAIRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeFonctionnaire;

    @Column(name = "DATE_ENTREE_ADMINISTRATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntreeAdministration;

    @Column(name = "STATUT")
    @Enumerated(EnumType.STRING)
    private EaeAgentStatutEnum statut;
    
    @Column(name = "STATUT_PRECISION")
    private String statutPrecision;

    @Column(name = "ANCIENNETE_ECHELON_JOURS")
    private Integer ancienneteEchelonJours;

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
    @Enumerated(EnumType.STRING)
    private EaeTypeAvctEnum typeAvancement;
    
    @Column(name = "POSITION")
    @Enumerated(EnumType.STRING)
    private EaeAgentPositionAdministrativeEnum position;
    
    @Column(name = "AVCT_DUR_MIN")
    private Integer avctDureeMin;
    
    @Column(name = "AVCT_DUR_MOY")
    private Integer avctDureeMoy;
    
    @Column(name = "AVCT_DUR_MAX")
    private Integer avctDureeMax;
    
    @OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
    
    /*
     * Transient properties (will be populated by AS400 entity manager)
     */
    @Transient
    private Agent agent;
    
    @Transient
    public String getAvctDureeMinDisplay() {
    	return String.format("%s %s", EaeAvancementEnum.MINI.toString(), getAvctDureeDisplay(getAvctDureeMin()));
    }
    
    @Transient
    public String getAvctDureeMoyDisplay() {
    	return String.format("%s %s", EaeAvancementEnum.MOY.toString(), getAvctDureeDisplay(getAvctDureeMoy()));
    }
    
    @Transient
    public String getAvctDureeMaxDisplay() {
    	return String.format("%s %s", EaeAvancementEnum.MAXI.toString(), getAvctDureeDisplay(getAvctDureeMax()));
    }
    
    protected String getAvctDureeDisplay(Integer duree) {
    	
    	if (statut != EaeAgentStatutEnum.F)
    		return "";
    	
    	if (duree != null && duree != 0)
    		return String.format("(%d mois)", duree);
    	
    	return "(NR)";
    }
}
