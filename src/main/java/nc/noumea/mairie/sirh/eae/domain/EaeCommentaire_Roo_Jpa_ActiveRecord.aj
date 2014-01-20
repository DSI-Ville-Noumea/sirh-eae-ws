// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeCommentaire;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeCommentaire_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeCommentaire.entityManager;
    
    public static final List<String> EaeCommentaire.fieldNames4OrderClauseFilter = java.util.Arrays.asList("text");
    
    public static final EntityManager EaeCommentaire.entityManager() {
        EntityManager em = new EaeCommentaire().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeCommentaire.countEaeCommentaires() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeCommentaire o", Long.class).getSingleResult();
    }
    
    public static List<EaeCommentaire> EaeCommentaire.findAllEaeCommentaires() {
        return entityManager().createQuery("SELECT o FROM EaeCommentaire o", EaeCommentaire.class).getResultList();
    }
    
    public static List<EaeCommentaire> EaeCommentaire.findAllEaeCommentaires(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM EaeCommentaire o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, EaeCommentaire.class).getResultList();
    }
    
    public static EaeCommentaire EaeCommentaire.findEaeCommentaire(Integer idEaeCommentaire) {
        if (idEaeCommentaire == null) return null;
        return entityManager().find(EaeCommentaire.class, idEaeCommentaire);
    }
    
    public static List<EaeCommentaire> EaeCommentaire.findEaeCommentaireEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeCommentaire o", EaeCommentaire.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<EaeCommentaire> EaeCommentaire.findEaeCommentaireEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM EaeCommentaire o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, EaeCommentaire.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeCommentaire.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeCommentaire.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeCommentaire attached = EaeCommentaire.findEaeCommentaire(this.idEaeCommentaire);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeCommentaire.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeCommentaire.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeCommentaire EaeCommentaire.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeCommentaire merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
