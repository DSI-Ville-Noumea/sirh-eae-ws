// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

privileged aspect EaePlanAction_Roo_JavaBean {
    
    public String EaePlanAction.getObjectif() {
        return this.objectif;
    }
    
    public void EaePlanAction.setObjectif(String objectif) {
        this.objectif = objectif;
    }
    
    public String EaePlanAction.getMesure() {
        return this.mesure;
    }
    
    public void EaePlanAction.setMesure(String mesure) {
        this.mesure = mesure;
    }
    
    public EaeTypeObjectif EaePlanAction.getTypeObjectif() {
        return this.typeObjectif;
    }
    
    public void EaePlanAction.setTypeObjectif(EaeTypeObjectif typeObjectif) {
        this.typeObjectif = typeObjectif;
    }
    
    public Eae EaePlanAction.getEae() {
        return this.eae;
    }
    
    public void EaePlanAction.setEae(Eae eae) {
        this.eae = eae;
    }
    
}