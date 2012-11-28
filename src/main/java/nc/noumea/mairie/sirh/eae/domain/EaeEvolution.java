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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;

import org.hibernate.annotations.Type;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", table = "EAE_EVOLUTION", sequenceName = "EAE_S_EVOLUTION", identifierColumn = "ID_EAE_EVOLUTION", identifierField = "idEaeEvolution", identifierType = Integer.class)
public class EaeEvolution {
	
	@Column(name = "MOBILIE_GEO", nullable = false)
    @Type(type="boolean")
    private boolean mobiliteGeo;
	
	@Column(name = "MOBILIE_FONCT", nullable = false)
    @Type(type="boolean")
    private boolean mobiliteFonctionnelle;
	
	@Column(name = "CHANGEMENT_METIER", nullable = false)
    @Type(type="boolean")
    private boolean changementMetier;
	
	@Column(name = "DELAI_ENVISAGE")
	@Enumerated(EnumType.STRING)
	private EaeDelaiEnum delaiEnvisage;
	
	@Column(name = "MOBILITE_SERVICE", nullable = false)
    @Type(type="boolean")
    private boolean mobiliteService;
	
	@Column(name = "MOBILITE_DIRECTION", nullable = false)
    @Type(type="boolean")
    private boolean mobiliteDirection;
	
	@Column(name = "MOBILITE_COLLECTIVITE", nullable = false)
    @Type(type="boolean")
    private boolean mobiliteCollectivite;
	
	@Column(name = "NOM_COLLECTIVITE")
	private String nomCollectivite;
	
	@Column(name = "MOBILITE_AUTRE", nullable = false)
    @Type(type="boolean")
    private boolean mobiliteAutre;
	
	@Column(name = "CONCOURS", nullable = false)
    @Type(type="boolean")
	private boolean concours;
	
	@Column(name = "NOM_CONCOURS")
	private String nomConcours;
	
	@Column(name = "VAE", nullable = false)
    @Type(type="boolean")
	private boolean vae;
	
	@Column(name = "NOM_VAE")
	private String nomVae;
	
	@Column(name = "TEMPS_PARTIEL", nullable = false)
    @Type(type="boolean")
	private boolean tempsPartiel;
	
	@Column(name = "POURC_TEMPS_PARTIEL")
	private int pourcentageTempsPartiel;
	
	@Column(name = "RETRAITE", nullable = false)
    @Type(type="boolean")
	private boolean retraite;
	
	@Column(name = "DATE_RETRAITE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRetraite;
	
	@Column(name = "AUTRE_PERSPECTIVE", nullable = false)
    @Type(type="boolean")
	private boolean autrePerspective;
	
	@Column(name = "LIB_AUTRE_PERSPECTIVE")
	private String libelleAutrePerspective;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_EVOLUTION")
	private EaeCommentaire commentaireEvolution;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_EVALUATEUR")
	private EaeCommentaire commentaireEvaluateur;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_EVALUE")
	private EaeCommentaire commentaireEvalue;
	
	@OneToMany(mappedBy = "eaeEvolution", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaeEvolutionSouhait ASC")
	private Set<EaeEvolutionSouhait> eaeEvolutionSouhaits = new HashSet<EaeEvolutionSouhait>();
	
	@OneToMany(mappedBy = "eaeEvolution", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("idEaeDeveloppement ASC")
	private Set<EaeDeveloppement> eaeDeveloppements = new HashSet<EaeDeveloppement>();
	
	@ManyToOne
    @JoinColumn(name = "ID_EAE", referencedColumnName = "ID_EAE")
    private Eae eae;
}
