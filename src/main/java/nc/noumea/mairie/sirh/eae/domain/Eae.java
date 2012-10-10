package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;
import nc.noumea.mairie.sirh.tools.transformer.EaeEvaluateurToAgentFlatTransformer;
import nc.noumea.mairie.sirh.tools.transformer.EaeFichePosteToEaeListTransformer;
import nc.noumea.mairie.sirh.tools.transformer.MSDateTransformer;
import nc.noumea.mairie.sirh.tools.transformer.NullableIntegerTransformer;
import nc.noumea.mairie.sirh.tools.transformer.SimpleAgentTransformer;
import nc.noumea.mairie.sirh.tools.transformer.ValueEnumTransformer;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

import flexjson.JSONSerializer;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE", identifierField = "idEae", identifierType = Integer.class, table = "EAE", sequenceName="EAE_S_EAE")
@RooJson
public class Eae {

	 /*
     * Mapped properties
     */
	@NotNull
    @Column(name = "ID_AGENT")
    private int idAgent;
    
    @Column(name = "STATUT")
	private String statut;
    
    @NotNull
    @Column(name = "ETAT")
    @Enumerated(EnumType.STRING)
    private EaeEtatEnum etat;
    
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
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY)
	private Set<EaeEvaluateur> eaeEvaluateurs = new HashSet<EaeEvaluateur>();
    
    @OneToOne(mappedBy = "eae", fetch = FetchType.LAZY)
    private EaeEvalue eaeEvalue;
    
    @OneToOne(optional = false, mappedBy = "eae", fetch = FetchType.LAZY)
    private EaeFichePoste eaeFichePoste;
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY)
	private Set<EaeDiplome> eaeDiplomes = new HashSet<EaeDiplome>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY)
	private Set<EaeParcoursPro> eaeParcoursPros = new HashSet<EaeParcoursPro>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY)
	private Set<EaeFormation> eaeFormations = new HashSet<EaeFormation>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY)
   	private Set<EaeResultat> eaeResultats = new HashSet<EaeResultat>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY)
   	private Set<EaePlanAction> eaePlanActions = new HashSet<EaePlanAction>();
    
    /*
     * Transient properties (will be populated by AS400 entity manager)
     */
    @Transient
    private Agent agentEvalue;
        
    @Transient
    private Agent agentDelegataire;
    
    public static JSONSerializer getSerializerForEaeList() {
    	
    	JSONSerializer serializer = new JSONSerializer()
	    	.include("agentEvalue")
	    	.include("etat")
	    	.include("cap")
	    	.include("docAttache")
	    	.include("dateCreation")
	    	.include("dateFinalisation")
	    	.include("dateControle")
	    	.include("dureeEntretien")
	    	.include("agentDelegataire")
	    	.include("eaeEvaluation.avisShd")
	    	.include("idEae")
	    	.include("eaeEvaluateurs")
	    	.include("eaeFichePoste")
	    	.transform(new MSDateTransformer(), Date.class)
	    	.transform(new NullableIntegerTransformer(), Integer.class)
	    	.transform(new SimpleAgentTransformer(), Agent.class)
	    	.transform(new EaeEvaluateurToAgentFlatTransformer(), EaeEvaluateur.class)
	    	.transform(new EaeFichePosteToEaeListTransformer(), EaeFichePoste.class)
	    	.transform(new ValueEnumTransformer(), Enum.class)
	    	.exclude("*");
    	
    	return serializer;
    }
}
