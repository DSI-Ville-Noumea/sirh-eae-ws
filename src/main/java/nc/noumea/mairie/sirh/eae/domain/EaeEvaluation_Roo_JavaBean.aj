// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeNiveau;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;

privileged aspect EaeEvaluation_Roo_JavaBean {
    
    public Integer EaeEvaluation.getNoteAnnee() {
        return this.noteAnnee;
    }
    
    public void EaeEvaluation.setNoteAnnee(Integer noteAnnee) {
        this.noteAnnee = noteAnnee;
    }
    
    public Integer EaeEvaluation.getNoteAnneeN1() {
        return this.noteAnneeN1;
    }
    
    public void EaeEvaluation.setNoteAnneeN1(Integer noteAnneeN1) {
        this.noteAnneeN1 = noteAnneeN1;
    }
    
    public Integer EaeEvaluation.getNoteAnneeN2() {
        return this.noteAnneeN2;
    }
    
    public void EaeEvaluation.setNoteAnneeN2(Integer noteAnneeN2) {
        this.noteAnneeN2 = noteAnneeN2;
    }
    
    public Integer EaeEvaluation.getNoteAnneeN3() {
        return this.noteAnneeN3;
    }
    
    public void EaeEvaluation.setNoteAnneeN3(Integer noteAnneeN3) {
        this.noteAnneeN3 = noteAnneeN3;
    }
    
    public Boolean EaeEvaluation.getAvisRevalorisation() {
        return this.avisRevalorisation;
    }
    
    public void EaeEvaluation.setAvisRevalorisation(Boolean avisRevalorisation) {
        this.avisRevalorisation = avisRevalorisation;
    }
    
    public String EaeEvaluation.getAvisShd() {
        return this.avisShd;
    }
    
    public void EaeEvaluation.setAvisShd(String avisShd) {
        this.avisShd = avisShd;
    }
    
    public EaeAvancementEnum EaeEvaluation.getPropositionAvancement() {
        return this.propositionAvancement;
    }
    
    public void EaeEvaluation.setPropositionAvancement(EaeAvancementEnum propositionAvancement) {
        this.propositionAvancement = propositionAvancement;
    }
    
    public Boolean EaeEvaluation.getAvisChangementClasse() {
        return this.avisChangementClasse;
    }
    
    public void EaeEvaluation.setAvisChangementClasse(Boolean avisChangementClasse) {
        this.avisChangementClasse = avisChangementClasse;
    }
    
    public EaeNiveau EaeEvaluation.getNiveauEae() {
        return this.niveauEae;
    }
    
    public void EaeEvaluation.setNiveauEae(EaeNiveau niveauEae) {
        this.niveauEae = niveauEae;
    }
    
    public EaeCommentaire EaeEvaluation.getCommentaireEvaluateur() {
        return this.commentaireEvaluateur;
    }
    
    public void EaeEvaluation.setCommentaireEvaluateur(EaeCommentaire commentaireEvaluateur) {
        this.commentaireEvaluateur = commentaireEvaluateur;
    }
    
    public EaeCommentaire EaeEvaluation.getCommentaireEvalue() {
        return this.commentaireEvalue;
    }
    
    public void EaeEvaluation.setCommentaireEvalue(EaeCommentaire commentaireEvalue) {
        this.commentaireEvalue = commentaireEvalue;
    }
    
    public EaeCommentaire EaeEvaluation.getCommentaireAvctEvaluateur() {
        return this.commentaireAvctEvaluateur;
    }
    
    public void EaeEvaluation.setCommentaireAvctEvaluateur(EaeCommentaire commentaireAvctEvaluateur) {
        this.commentaireAvctEvaluateur = commentaireAvctEvaluateur;
    }
    
    public EaeCommentaire EaeEvaluation.getCommentaireAvctEvalue() {
        return this.commentaireAvctEvalue;
    }
    
    public void EaeEvaluation.setCommentaireAvctEvalue(EaeCommentaire commentaireAvctEvalue) {
        this.commentaireAvctEvalue = commentaireAvctEvalue;
    }
    
    public Eae EaeEvaluation.getEae() {
        return this.eae;
    }
    
    public void EaeEvaluation.setEae(Eae eae) {
        this.eae = eae;
    }
    
}
