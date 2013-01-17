// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentPositionAdministrativeEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAgentStatutEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeTypeAvctEnum;

privileged aspect EaeEvalue_Roo_JavaBean {
    
    public int EaeEvalue.getIdAgent() {
        return this.idAgent;
    }
    
    public void EaeEvalue.setIdAgent(int idAgent) {
        this.idAgent = idAgent;
    }
    
    public Date EaeEvalue.getDateEntreeService() {
        return this.dateEntreeService;
    }
    
    public void EaeEvalue.setDateEntreeService(Date dateEntreeService) {
        this.dateEntreeService = dateEntreeService;
    }
    
    public Date EaeEvalue.getDateEntreeCollectivite() {
        return this.dateEntreeCollectivite;
    }
    
    public void EaeEvalue.setDateEntreeCollectivite(Date dateEntreeCollectivite) {
        this.dateEntreeCollectivite = dateEntreeCollectivite;
    }
    
    public Date EaeEvalue.getDateEntreeFonctionnaire() {
        return this.dateEntreeFonctionnaire;
    }
    
    public void EaeEvalue.setDateEntreeFonctionnaire(Date dateEntreeFonctionnaire) {
        this.dateEntreeFonctionnaire = dateEntreeFonctionnaire;
    }
    
    public Date EaeEvalue.getDateEntreeAdministration() {
        return this.dateEntreeAdministration;
    }
    
    public void EaeEvalue.setDateEntreeAdministration(Date dateEntreeAdministration) {
        this.dateEntreeAdministration = dateEntreeAdministration;
    }
    
    public EaeAgentStatutEnum EaeEvalue.getStatut() {
        return this.statut;
    }
    
    public void EaeEvalue.setStatut(EaeAgentStatutEnum statut) {
        this.statut = statut;
    }
    
    public String EaeEvalue.getStatutPrecision() {
        return this.statutPrecision;
    }
    
    public void EaeEvalue.setStatutPrecision(String statutPrecision) {
        this.statutPrecision = statutPrecision;
    }
    
    public Integer EaeEvalue.getAncienneteEchelonJours() {
        return this.ancienneteEchelonJours;
    }
    
    public void EaeEvalue.setAncienneteEchelonJours(Integer ancienneteEchelonJours) {
        this.ancienneteEchelonJours = ancienneteEchelonJours;
    }
    
    public String EaeEvalue.getCadre() {
        return this.cadre;
    }
    
    public void EaeEvalue.setCadre(String cadre) {
        this.cadre = cadre;
    }
    
    public String EaeEvalue.getCategorie() {
        return this.categorie;
    }
    
    public void EaeEvalue.setCategorie(String categorie) {
        this.categorie = categorie;
    }
    
    public String EaeEvalue.getClassification() {
        return this.classification;
    }
    
    public void EaeEvalue.setClassification(String classification) {
        this.classification = classification;
    }
    
    public String EaeEvalue.getGrade() {
        return this.grade;
    }
    
    public void EaeEvalue.setGrade(String grade) {
        this.grade = grade;
    }
    
    public String EaeEvalue.getEchelon() {
        return this.echelon;
    }
    
    public void EaeEvalue.setEchelon(String echelon) {
        this.echelon = echelon;
    }
    
    public Date EaeEvalue.getDateEffetAvancement() {
        return this.dateEffetAvancement;
    }
    
    public void EaeEvalue.setDateEffetAvancement(Date dateEffetAvancement) {
        this.dateEffetAvancement = dateEffetAvancement;
    }
    
    public String EaeEvalue.getNouvGrade() {
        return this.nouvGrade;
    }
    
    public void EaeEvalue.setNouvGrade(String nouvGrade) {
        this.nouvGrade = nouvGrade;
    }
    
    public String EaeEvalue.getNouvEchelon() {
        return this.nouvEchelon;
    }
    
    public void EaeEvalue.setNouvEchelon(String nouvEchelon) {
        this.nouvEchelon = nouvEchelon;
    }
    
    public boolean EaeEvalue.isEstEncadrant() {
        return this.estEncadrant;
    }
    
    public void EaeEvalue.setEstEncadrant(boolean estEncadrant) {
        this.estEncadrant = estEncadrant;
    }
    
    public EaeTypeAvctEnum EaeEvalue.getTypeAvancement() {
        return this.typeAvancement;
    }
    
    public void EaeEvalue.setTypeAvancement(EaeTypeAvctEnum typeAvancement) {
        this.typeAvancement = typeAvancement;
    }
    
    public EaeAgentPositionAdministrativeEnum EaeEvalue.getPosition() {
        return this.position;
    }
    
    public void EaeEvalue.setPosition(EaeAgentPositionAdministrativeEnum position) {
        this.position = position;
    }
    
    public Integer EaeEvalue.getAvctDureeMin() {
        return this.avctDureeMin;
    }
    
    public void EaeEvalue.setAvctDureeMin(Integer avctDureeMin) {
        this.avctDureeMin = avctDureeMin;
    }
    
    public Integer EaeEvalue.getAvctDureeMoy() {
        return this.avctDureeMoy;
    }
    
    public void EaeEvalue.setAvctDureeMoy(Integer avctDureeMoy) {
        this.avctDureeMoy = avctDureeMoy;
    }
    
    public Integer EaeEvalue.getAvctDureeMax() {
        return this.avctDureeMax;
    }
    
    public void EaeEvalue.setAvctDureeMax(Integer avctDureeMax) {
        this.avctDureeMax = avctDureeMax;
    }
    
    public Eae EaeEvalue.getEae() {
        return this.eae;
    }
    
    public void EaeEvalue.setEae(Eae eae) {
        this.eae = eae;
    }
    
    public Agent EaeEvalue.getAgent() {
        return this.agent;
    }
    
    public void EaeEvalue.setAgent(Agent agent) {
        this.agent = agent;
    }
    
}
