// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.Date;
import java.util.Set;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolution;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeDelaiEnum;

privileged aspect EaeEvolution_Roo_JavaBean {
    
    public boolean EaeEvolution.isMobiliteGeo() {
        return this.mobiliteGeo;
    }
    
    public void EaeEvolution.setMobiliteGeo(boolean mobiliteGeo) {
        this.mobiliteGeo = mobiliteGeo;
    }
    
    public boolean EaeEvolution.isMobiliteFonctionnelle() {
        return this.mobiliteFonctionnelle;
    }
    
    public void EaeEvolution.setMobiliteFonctionnelle(boolean mobiliteFonctionnelle) {
        this.mobiliteFonctionnelle = mobiliteFonctionnelle;
    }
    
    public boolean EaeEvolution.isChangementMetier() {
        return this.changementMetier;
    }
    
    public void EaeEvolution.setChangementMetier(boolean changementMetier) {
        this.changementMetier = changementMetier;
    }
    
    public EaeDelaiEnum EaeEvolution.getDelaiEnvisage() {
        return this.delaiEnvisage;
    }
    
    public void EaeEvolution.setDelaiEnvisage(EaeDelaiEnum delaiEnvisage) {
        this.delaiEnvisage = delaiEnvisage;
    }
    
    public boolean EaeEvolution.isMobiliteService() {
        return this.mobiliteService;
    }
    
    public void EaeEvolution.setMobiliteService(boolean mobiliteService) {
        this.mobiliteService = mobiliteService;
    }
    
    public boolean EaeEvolution.isMobiliteDirection() {
        return this.mobiliteDirection;
    }
    
    public void EaeEvolution.setMobiliteDirection(boolean mobiliteDirection) {
        this.mobiliteDirection = mobiliteDirection;
    }
    
    public boolean EaeEvolution.isMobiliteCollectivite() {
        return this.mobiliteCollectivite;
    }
    
    public void EaeEvolution.setMobiliteCollectivite(boolean mobiliteCollectivite) {
        this.mobiliteCollectivite = mobiliteCollectivite;
    }
    
    public String EaeEvolution.getNomCollectivite() {
        return this.nomCollectivite;
    }
    
    public void EaeEvolution.setNomCollectivite(String nomCollectivite) {
        this.nomCollectivite = nomCollectivite;
    }
    
    public boolean EaeEvolution.isMobiliteAutre() {
        return this.mobiliteAutre;
    }
    
    public void EaeEvolution.setMobiliteAutre(boolean mobiliteAutre) {
        this.mobiliteAutre = mobiliteAutre;
    }
    
    public boolean EaeEvolution.isConcours() {
        return this.concours;
    }
    
    public void EaeEvolution.setConcours(boolean concours) {
        this.concours = concours;
    }
    
    public String EaeEvolution.getNomConcours() {
        return this.nomConcours;
    }
    
    public void EaeEvolution.setNomConcours(String nomConcours) {
        this.nomConcours = nomConcours;
    }
    
    public boolean EaeEvolution.isVae() {
        return this.vae;
    }
    
    public void EaeEvolution.setVae(boolean vae) {
        this.vae = vae;
    }
    
    public String EaeEvolution.getNomVae() {
        return this.nomVae;
    }
    
    public void EaeEvolution.setNomVae(String nomVae) {
        this.nomVae = nomVae;
    }
    
    public boolean EaeEvolution.isTempsPartiel() {
        return this.tempsPartiel;
    }
    
    public void EaeEvolution.setTempsPartiel(boolean tempsPartiel) {
        this.tempsPartiel = tempsPartiel;
    }
    
    public int EaeEvolution.getPourcentageTempsParciel() {
        return this.pourcentageTempsParciel;
    }
    
    public void EaeEvolution.setPourcentageTempsParciel(int pourcentageTempsParciel) {
        this.pourcentageTempsParciel = pourcentageTempsParciel;
    }
    
    public boolean EaeEvolution.isRetraite() {
        return this.retraite;
    }
    
    public void EaeEvolution.setRetraite(boolean retraite) {
        this.retraite = retraite;
    }
    
    public Date EaeEvolution.getDateRetraite() {
        return this.dateRetraite;
    }
    
    public void EaeEvolution.setDateRetraite(Date dateRetraite) {
        this.dateRetraite = dateRetraite;
    }
    
    public boolean EaeEvolution.isAutrePerspective() {
        return this.autrePerspective;
    }
    
    public void EaeEvolution.setAutrePerspective(boolean autrePerspective) {
        this.autrePerspective = autrePerspective;
    }
    
    public String EaeEvolution.getLibelleAutrePerspective() {
        return this.libelleAutrePerspective;
    }
    
    public void EaeEvolution.setLibelleAutrePerspective(String libelleAutrePerspective) {
        this.libelleAutrePerspective = libelleAutrePerspective;
    }
    
    public EaeCommentaire EaeEvolution.getCommentaireEvolution() {
        return this.commentaireEvolution;
    }
    
    public void EaeEvolution.setCommentaireEvolution(EaeCommentaire commentaireEvolution) {
        this.commentaireEvolution = commentaireEvolution;
    }
    
    public EaeCommentaire EaeEvolution.getCommentaireEvaluateur() {
        return this.commentaireEvaluateur;
    }
    
    public void EaeEvolution.setCommentaireEvaluateur(EaeCommentaire commentaireEvaluateur) {
        this.commentaireEvaluateur = commentaireEvaluateur;
    }
    
    public EaeCommentaire EaeEvolution.getCommentaireEvalue() {
        return this.commentaireEvalue;
    }
    
    public void EaeEvolution.setCommentaireEvalue(EaeCommentaire commentaireEvalue) {
        this.commentaireEvalue = commentaireEvalue;
    }
    
    public Set<EaeEvolutionSouhait> EaeEvolution.getEaeEvolutionSouhaits() {
        return this.eaeEvolutionSouhaits;
    }
    
    public void EaeEvolution.setEaeEvolutionSouhaits(Set<EaeEvolutionSouhait> eaeEvolutionSouhaits) {
        this.eaeEvolutionSouhaits = eaeEvolutionSouhaits;
    }
    
    public Set<EaeDeveloppement> EaeEvolution.getEaeDeveloppements() {
        return this.eaeDeveloppements;
    }
    
    public void EaeEvolution.setEaeDeveloppements(Set<EaeDeveloppement> eaeDeveloppements) {
        this.eaeDeveloppements = eaeDeveloppements;
    }
    
    public Eae EaeEvolution.getEae() {
        return this.eae;
    }
    
    public void EaeEvolution.setEae(Eae eae) {
        this.eae = eae;
    }
    
}
