// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import nc.noumea.mairie.sirh.eae.domain.EaeFdpActivite;

privileged aspect EaeFdpActivite_Roo_Jpa_Entity {
    
    declare @type: EaeFdpActivite: @Entity;
    
    declare @type: EaeFdpActivite: @Table(name = "EAE_FDP_ACTIVITE");
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_EAE_FDP_ACTIVITE")
    private Integer EaeFdpActivite.idEaeFdpActivite;
    
    @Version
    @Column(name = "version")
    private Integer EaeFdpActivite.version;
    
    public Integer EaeFdpActivite.getIdEaeFdpActivite() {
        return this.idEaeFdpActivite;
    }
    
    public void EaeFdpActivite.setIdEaeFdpActivite(Integer id) {
        this.idEaeFdpActivite = id;
    }
    
    public Integer EaeFdpActivite.getVersion() {
        return this.version;
    }
    
    public void EaeFdpActivite.setVersion(Integer version) {
        this.version = version;
    }
    
}