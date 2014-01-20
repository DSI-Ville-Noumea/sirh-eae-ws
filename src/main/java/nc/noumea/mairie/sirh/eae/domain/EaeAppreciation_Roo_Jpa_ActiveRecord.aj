// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeAppreciation;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeAppreciation_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeAppreciation.entityManager;
    
    public static final List<String> EaeAppreciation.fieldNames4OrderClauseFilter = java.util.Arrays.asList("typeAppreciation", "numero", "noteEvalue", "noteEvaluateur", "eae");
    
    public static final EntityManager EaeAppreciation.entityManager() {
        EntityManager em = new EaeAppreciation().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeAppreciation.countEaeAppreciations() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeAppreciation o", Long.class).getSingleResult();
    }
    
    public static List<EaeAppreciation> EaeAppreciation.findAllEaeAppreciations() {
        return entityManager().createQuery("SELECT o FROM EaeAppreciation o", EaeAppreciation.class).getResultList();
    }
    
    public static List<EaeAppreciation> EaeAppreciation.findAllEaeAppreciations(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM EaeAppreciation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, EaeAppreciation.class).getResultList();
    }
    
    public static EaeAppreciation EaeAppreciation.findEaeAppreciation(Integer idEaeAppreciation) {
        if (idEaeAppreciation == null) return null;
        return entityManager().find(EaeAppreciation.class, idEaeAppreciation);
    }
    
    public static List<EaeAppreciation> EaeAppreciation.findEaeAppreciationEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeAppreciation o", EaeAppreciation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<EaeAppreciation> EaeAppreciation.findEaeAppreciationEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM EaeAppreciation o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, EaeAppreciation.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeAppreciation.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeAppreciation.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeAppreciation attached = EaeAppreciation.findEaeAppreciation(this.idEaeAppreciation);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeAppreciation.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeAppreciation.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeAppreciation EaeAppreciation.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeAppreciation merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
