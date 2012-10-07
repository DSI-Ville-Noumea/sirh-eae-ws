package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "eaePersistenceUnit", identifierColumn = "ID_EAE_EVALUATION", identifierField = "idEaeEvaluation", identifierType = Integer.class, table = "EAE_EVALUATION")
public class EaeEvaluation {

    @Column(name = "NOTE_ANNEE")
    private Integer noteAnnee;

    @Column(name = "NOTE_ANNEE_N1")
    private Integer noteAnneeN1;

    @Column(name = "NOTE_ANNEE_N2")
    private Integer noteAnneeN2;

    @Column(name = "NOTE_ANNEE_N3")
    private Integer noteAnneeN3;

    @Column(name = "AVIS")
    private String avis;
    
    @Column(name = "AVIS_SHD")
    private String avisShd;

    @Column(name = "AVANCEMENT_DIFF")
    private String avancementDiff;

    @Column(name = "CHANGEMENT_CLASSE")
    private String changementClasse;

    @OneToOne
    @JoinColumn(name = "ID_EAE")
    private Eae eae;
}
