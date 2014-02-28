package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE", identifierField = "idEae", identifierType = Integer.class, table = "EAE", sequenceName="EAE_S_EAE")
public class Eae {

	 /*
     * Mapped properties
     */
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    
    @Column(name = "DATE_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFin;
    
    @Column(name = "DATE_ENTRETIEN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEntretien;

    @Column(name = "DUREE_ENTRETIEN", nullable = true)
    private Integer dureeEntretienMinutes;
    
    @Column(name = "DATE_FINALISE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateFinalisation;
    
    @Column(name = "DATE_CONTROLE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateControle;

    @Column(name = "HEURE_CONTROLE")
    private String heureControle;
    
    @Column(name = "USER_CONTROLE")
    private String userControle;
    
    @Column(name = "ID_DELEGATAIRE")
    private Integer idAgentDelegataire;
    
    @OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private EaeEvaluation eaeEvaluation;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COMMENTAIRE")
    private EaeCommentaire commentaire;
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeEvaluateur> eaeEvaluateurs = new HashSet<EaeEvaluateur>();
    
    @OneToOne(mappedBy = "eae", fetch = FetchType.LAZY)
    private EaeEvalue eaeEvalue;
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<EaeFichePoste> eaeFichePostes = new HashSet<EaeFichePoste>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("idEaeDiplome asc")
	private Set<EaeDiplome> eaeDiplomes = new HashSet<EaeDiplome>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<EaeParcoursPro> eaeParcoursPros = new HashSet<EaeParcoursPro>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<EaeFormation> eaeFormations = new HashSet<EaeFormation>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idEaeResultat asc")
   	private Set<EaeResultat> eaeResultats = new HashSet<EaeResultat>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("idEaePlanAction asc")
   	private Set<EaePlanAction> eaePlanActions = new HashSet<EaePlanAction>();
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
   	private Set<EaeAppreciation> eaeAppreciations = new HashSet<EaeAppreciation>();
    
    @OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private EaeAutoEvaluation eaeAutoEvaluation;
    
    @OneToOne(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private EaeEvolution eaeEvolution;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CAMPAGNE_EAE")
    private EaeCampagne eaeCampagne;
    
    @OneToMany(mappedBy = "eae", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<EaeFinalisation> eaeFinalisations = new HashSet<EaeFinalisation>();
    
    /*
     * Transient properties (will be populated by AS400 entity manager)
     */        
    @Transient
    private Agent agentDelegataire;
    
    public EaeFichePoste getPrimaryFichePoste() {
    	
    	for(EaeFichePoste fdp : this.getEaeFichePostes()) {
    		if (fdp.isPrimary())
    			return fdp;
    	}
    	
    	return null;
    }
    
    public boolean isEvaluateur(int idAgent) {

    	for (EaeEvaluateur e : this.getEaeEvaluateurs()) {
			if (e.getIdAgent() == idAgent)
				return true;
		}
    	
    	return false;
    }
    
    public boolean isDelegataire(int idAgent) {
    	return (getIdAgentDelegataire() != null &&  getIdAgentDelegataire() == idAgent);
    }
    
	public boolean isEvaluateurOrDelegataire(int idAgent) {
		return isEvaluateur(idAgent) || isDelegataire(idAgent);		
	}
	
	public EaeFinalisation getLatestFinalisation() {
		
		EaeFinalisation latestFinalisation = null;
		
		for(EaeFinalisation finalisation : this.getEaeFinalisations()) {
    		if (latestFinalisation == null 
    				|| finalisation.getDateFinalisation().after(latestFinalisation.getDateFinalisation()))
    			latestFinalisation = finalisation;
    	}
    	
    	return latestFinalisation;
	}
}
