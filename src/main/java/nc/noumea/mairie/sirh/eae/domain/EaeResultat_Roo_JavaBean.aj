// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeResultat;

privileged aspect EaeResultat_Roo_JavaBean {
    
    public String EaeResultat.getObjectif() {
        return this.objectif;
    }
    
    public void EaeResultat.setObjectif(String objectif) {
        this.objectif = objectif;
    }
    
    public String EaeResultat.getDetailObjectif() {
        return this.detailObjectif;
    }
    
    public void EaeResultat.setDetailObjectif(String detailObjectif) {
        this.detailObjectif = detailObjectif;
    }
    
    public String EaeResultat.getResultat() {
        return this.resultat;
    }
    
    public void EaeResultat.setResultat(String resultat) {
        this.resultat = resultat;
    }
    
    public String EaeResultat.getCommentaire() {
        return this.commentaire;
    }
    
    public void EaeResultat.setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    public EaeTypeResultat EaeResultat.getTypeResultat() {
        return this.typeResultat;
    }
    
    public void EaeResultat.setTypeResultat(EaeTypeResultat typeResultat) {
        this.typeResultat = typeResultat;
    }
    
    public Eae EaeResultat.getEae() {
        return this.eae;
    }
    
    public void EaeResultat.setEae(Eae eae) {
        this.eae = eae;
    }
    
}