package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeNiveauEnum;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_EVALUATION", identifierField = "idEaeEvaluation", identifierType = Integer.class, table = "EAE_EVALUATION", sequenceName="EAE_S_EVALUATION")
public class EaeEvaluation {

    @Column(name = "NOTE_ANNEE", scale = 2, precision = 4, columnDefinition="NUMBER(4,2)")
    private Float noteAnnee;

    @Column(name = "NOTE_ANNEE_N1", scale = 2, precision = 4, columnDefinition="NUMBER(4,2)")
    private Float noteAnneeN1;

    @Column(name = "NOTE_ANNEE_N2", scale = 2, precision = 4, columnDefinition="NUMBER(4,2)")
    private Float noteAnneeN2;

    @Column(name = "NOTE_ANNEE_N3", scale = 2, precision = 4, columnDefinition="NUMBER(4,2)")
    private Float noteAnneeN3;

    @Column(name = "AVIS_REVALORISATION")
    private Boolean avisRevalorisation;
    
    @Column(name = "AVIS_SHD")
    private String avisShd;

    @Column(name = "PROPOSITION_AVANCEMENT")
    @Enumerated(EnumType.STRING)
    private EaeAvancementEnum propositionAvancement;

    @Column(name = "AVIS_CHANGEMENT_CLASSE")
    private Boolean avisChangementClasse;

    @Column(name = "NIVEAU")
    @Enumerated(EnumType.STRING)
    private EaeNiveauEnum niveauEae;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_EVALUATEUR")
    private EaeCommentaire commentaireEvaluateur;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_EVALUE")
    private EaeCommentaire commentaireEvalue;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_AVCT_EVALUATEUR")
    private EaeCommentaire commentaireAvctEvaluateur;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ID_EAE_COM_AVCT_EVALUE")
    private EaeCommentaire commentaireAvctEvalue;
    
    @OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
}
