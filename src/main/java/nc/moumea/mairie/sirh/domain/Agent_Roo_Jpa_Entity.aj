// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.moumea.mairie.sirh.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import nc.moumea.mairie.sirh.domain.Agent;

privileged aspect Agent_Roo_Jpa_Entity {
    
    declare @type: Agent: @Entity;
    
    declare @type: Agent: @Table(schema = "SIRH", name = "AGENT");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_AGENT")
    private Integer Agent.idAgent;
    
    public Integer Agent.getIdAgent() {
        return this.idAgent;
    }
    
    public void Agent.setIdAgent(Integer id) {
        this.idAgent = id;
    }
    
}
