// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;

privileged aspect EaeFormation_Roo_JavaBean {
    
    public int EaeFormation.getAnneeFormation() {
        return this.anneeFormation;
    }
    
    public void EaeFormation.setAnneeFormation(int anneeFormation) {
        this.anneeFormation = anneeFormation;
    }
    
    public String EaeFormation.getDureeFormation() {
        return this.dureeFormation;
    }
    
    public void EaeFormation.setDureeFormation(String dureeFormation) {
        this.dureeFormation = dureeFormation;
    }
    
    public String EaeFormation.getLibelleFormation() {
        return this.libelleFormation;
    }
    
    public void EaeFormation.setLibelleFormation(String libelleFormation) {
        this.libelleFormation = libelleFormation;
    }
    
    public Eae EaeFormation.getEae() {
        return this.eae;
    }
    
    public void EaeFormation.setEae(Eae eae) {
        this.eae = eae;
    }
    
}
