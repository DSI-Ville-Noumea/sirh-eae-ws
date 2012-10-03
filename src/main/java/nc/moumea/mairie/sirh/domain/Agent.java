package nc.moumea.mairie.sirh.domain;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit="sirhDb400PersistenceUnit", identifierColumn = "ID_AGENT", schema = "SIRH", identifierField = "idAgent", identifierType = Integer.class, table = "AGENT")
public class Agent {

    @NotNull
    @Column(name = "NOMATR")
    private Integer nomatr;

    @Column(name = "NOM_MARITAL")
    private String nomMarital;

    @NotNull
    @Column(name = "NOM_PATRONYMIQUE")
    private String nomPatronymique;
}
