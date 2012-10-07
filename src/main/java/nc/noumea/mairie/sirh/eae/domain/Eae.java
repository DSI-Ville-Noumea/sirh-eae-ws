package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;
import nc.noumea.mairie.sirh.tools.transformer.NullableIntegerTransformer;
import nc.noumea.mairie.sirh.tools.transformer.SimpleAgentTransformer;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE", identifierField = "idEae", identifierType = Integer.class, table = "EAE")
@RooJson
public class Eae {

	 /*
     * Mapped properties
     */
	@NotNull
    @Column(name = "ID_AGENT")
    private int idAgent;
    
	@NotNull
    @Column(name = "ID_SHD")
    private Integer idAgentShd;
    
    @Column(name = "DIRECTION_SERVICE")
    private String directionService;
    
    @Column(name = "SECTION_SERVICE")
    private String sectionService;
    
    @Column(name = "SERVICE")
    private String service;
    
    @Column(name = "STATUT")
    private String statut;
    
    @NotNull
    @Column(name = "ETAT")
    private String etat;
    
    @NotNull
    @Column(name = "CAP")
    private boolean cap;
        
    @NotNull
    @Column(name = "DOC_ATTACHE")
    private boolean docAttache;
    
    @Column(name = "DATE_CREATION")
    private Date dateCreation;
    
    @Column(name = "DATE_FIN")
    private Date dateFin;
    
    @Column(name = "DATE_ENTRETIEN")
    private Date dateEntretien;

    @Column(name = "DUREE_ENTRETIEN", nullable = true)
    private Integer dureeEntretien;
    
    @Column(name = "DATE_FINALISE")
    private Date dateFinalisation;
    
    @Column(name = "DATE_CONTROLE")
    private Date dateControle;

    @Column(name = "HEURE_CONTROLE")
    private String heureControle;
    
    @Column(name = "USER_CONTROLE")
    private String userControle;
    
    @Column(name = "ID_DELEGATAIRE")
    private Integer idAgentDelegataire;
    
    @OneToOne(mappedBy = "eae", fetch = FetchType.LAZY)
    private EaeEvaluation eaeEvaluation;
    
    /*
     * Transient properties (will be populated by AS400 entity manager)
     */
    @Transient
    private Agent agentEvalue;
    
    @Transient
    private Agent agentShd;
    
    @Transient
    private Agent agentDelegataire;
    
    public static JSONSerializer getSerializerForEaeList() {
    	
    	JSONSerializer serializer = new JSONSerializer()
	    	.include("agentEvalue")
	    	.include("directionService")
	    	.include("sectionService")
	    	.include("service")
	    	.include("etat")
	    	.include("cap")
	    	.include("docAttache")
	    	.include("dateCreation")
	    	.include("dateFinalisation")
	    	.include("dateControle")
	    	.include("dureeEntretien")
	    	.include("agentShd")
	    	.include("agentDelegataire")
	    	.include("eaeEvaluation.avisShd")
	    	.include("idEae")
	    	.transform(new MSDateTransformer(), Date.class)
	    	.transform(new NullableIntegerTransformer(), Integer.class)
	    	.transform(new SimpleAgentTransformer(), Agent.class)
	    	.exclude("*");
    	
    	return serializer;
    }
}
