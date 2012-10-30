// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.Set;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import nc.noumea.mairie.sirh.eae.domain.EaeAutoEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDiplome;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import nc.noumea.mairie.sirh.eae.domain.EaeFormation;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;
import nc.noumea.mairie.sirh.eae.domain.EaeResultat;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

privileged aspect Eae_Roo_JavaBean {
    
    public EaeEtatEnum Eae.getEtat() {
        return this.etat;
    }
    
    public void Eae.setEtat(EaeEtatEnum etat) {
        this.etat = etat;
    }
    
    public boolean Eae.isCap() {
        return this.cap;
    }
    
    public void Eae.setCap(boolean cap) {
        this.cap = cap;
    }
    
    public boolean Eae.isDocAttache() {
        return this.docAttache;
    }
    
    public void Eae.setDocAttache(boolean docAttache) {
        this.docAttache = docAttache;
    }
    
    public Date Eae.getDateCreation() {
        return this.dateCreation;
    }
    
    public void Eae.setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }
    
    public Date Eae.getDateFin() {
        return this.dateFin;
    }
    
    public void Eae.setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    
    public Date Eae.getDateEntretien() {
        return this.dateEntretien;
    }
    
    public void Eae.setDateEntretien(Date dateEntretien) {
        this.dateEntretien = dateEntretien;
    }
    
    public Integer Eae.getDureeEntretienMinutes() {
        return this.dureeEntretienMinutes;
    }
    
    public void Eae.setDureeEntretienMinutes(Integer dureeEntretienMinutes) {
        this.dureeEntretienMinutes = dureeEntretienMinutes;
    }
    
    public Date Eae.getDateFinalisation() {
        return this.dateFinalisation;
    }
    
    public void Eae.setDateFinalisation(Date dateFinalisation) {
        this.dateFinalisation = dateFinalisation;
    }
    
    public Date Eae.getDateControle() {
        return this.dateControle;
    }
    
    public void Eae.setDateControle(Date dateControle) {
        this.dateControle = dateControle;
    }
    
    public String Eae.getHeureControle() {
        return this.heureControle;
    }
    
    public void Eae.setHeureControle(String heureControle) {
        this.heureControle = heureControle;
    }
    
    public String Eae.getUserControle() {
        return this.userControle;
    }
    
    public void Eae.setUserControle(String userControle) {
        this.userControle = userControle;
    }
    
    public Integer Eae.getIdAgentDelegataire() {
        return this.idAgentDelegataire;
    }
    
    public void Eae.setIdAgentDelegataire(Integer idAgentDelegataire) {
        this.idAgentDelegataire = idAgentDelegataire;
    }
    
    public EaeEvaluation Eae.getEaeEvaluation() {
        return this.eaeEvaluation;
    }
    
    public void Eae.setEaeEvaluation(EaeEvaluation eaeEvaluation) {
        this.eaeEvaluation = eaeEvaluation;
    }
    
    public EaeCommentaire Eae.getCommentaire() {
        return this.commentaire;
    }
    
    public void Eae.setCommentaire(EaeCommentaire commentaire) {
        this.commentaire = commentaire;
    }
    
    public Set<EaeEvaluateur> Eae.getEaeEvaluateurs() {
        return this.eaeEvaluateurs;
    }
    
    public void Eae.setEaeEvaluateurs(Set<EaeEvaluateur> eaeEvaluateurs) {
        this.eaeEvaluateurs = eaeEvaluateurs;
    }
    
    public EaeEvalue Eae.getEaeEvalue() {
        return this.eaeEvalue;
    }
    
    public void Eae.setEaeEvalue(EaeEvalue eaeEvalue) {
        this.eaeEvalue = eaeEvalue;
    }
    
    public Set<EaeFichePoste> Eae.getEaeFichePostes() {
        return this.eaeFichePostes;
    }
    
    public void Eae.setEaeFichePostes(Set<EaeFichePoste> eaeFichePostes) {
        this.eaeFichePostes = eaeFichePostes;
    }
    
    public Set<EaeDiplome> Eae.getEaeDiplomes() {
        return this.eaeDiplomes;
    }
    
    public void Eae.setEaeDiplomes(Set<EaeDiplome> eaeDiplomes) {
        this.eaeDiplomes = eaeDiplomes;
    }
    
    public Set<EaeParcoursPro> Eae.getEaeParcoursPros() {
        return this.eaeParcoursPros;
    }
    
    public void Eae.setEaeParcoursPros(Set<EaeParcoursPro> eaeParcoursPros) {
        this.eaeParcoursPros = eaeParcoursPros;
    }
    
    public Set<EaeFormation> Eae.getEaeFormations() {
        return this.eaeFormations;
    }
    
    public void Eae.setEaeFormations(Set<EaeFormation> eaeFormations) {
        this.eaeFormations = eaeFormations;
    }
    
    public Set<EaeResultat> Eae.getEaeResultats() {
        return this.eaeResultats;
    }
    
    public void Eae.setEaeResultats(Set<EaeResultat> eaeResultats) {
        this.eaeResultats = eaeResultats;
    }
    
    public Set<EaePlanAction> Eae.getEaePlanActions() {
        return this.eaePlanActions;
    }
    
    public void Eae.setEaePlanActions(Set<EaePlanAction> eaePlanActions) {
        this.eaePlanActions = eaePlanActions;
    }
    
    public Set<EaeAppreciation> Eae.getEaeAppreciations() {
        return this.eaeAppreciations;
    }
    
    public void Eae.setEaeAppreciations(Set<EaeAppreciation> eaeAppreciations) {
        this.eaeAppreciations = eaeAppreciations;
    }
    
    public EaeAutoEvaluation Eae.getEaeAutoEvaluation() {
        return this.eaeAutoEvaluation;
    }
    
    public void Eae.setEaeAutoEvaluation(EaeAutoEvaluation eaeAutoEvaluation) {
        this.eaeAutoEvaluation = eaeAutoEvaluation;
    }
    
    public Agent Eae.getAgentDelegataire() {
        return this.agentDelegataire;
    }
    
    public void Eae.setAgentDelegataire(Agent agentDelegataire) {
        this.agentDelegataire = agentDelegataire;
    }
    
}
