package nc.noumea.mairie.sirh.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.json.RooJson;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit="sirhPersistenceUnit", identifierColumn = "ID_AGENT", schema = "SIRH", identifierField = "idAgent", identifierType = Integer.class, table = "AGENT", versionField="")
@RooJson
public class Agent {

    @NotNull
    @Column(name = "NOMATR")
    private Integer nomatr;

    @Column(name = "NOM_MARITAL")
    private String nomMarital;

    @NotNull
    @Column(name = "NOM_PATRONYMIQUE")
    private String nomPatronymique;
    
    @Column(name = "NOM_USAGE")
    private String nomUsage;
    
    @NotNull
    @Column(name = "PRENOM")
    private String prenom;
    
    @NotNull
    @Column(name = "PRENOM_USAGE")
    private String prenomUsage;
    
    @NotNull
    @Column(name = "DATE_NAISSANCE")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
}
