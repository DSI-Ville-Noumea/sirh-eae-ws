// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeTypeDeveloppement;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeTypeDeveloppement_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeTypeDeveloppement.entityManager;
    
    public static final List<String> EaeTypeDeveloppement.fieldNames4OrderClauseFilter = java.util.Arrays.asList("libelle");
    
    public static final EntityManager EaeTypeDeveloppement.entityManager() {
        EntityManager em = new EaeTypeDeveloppement().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeTypeDeveloppement.countEaeTypeDeveloppements() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeTypeDeveloppement o", Long.class).getSingleResult();
    }
    
    public static List<EaeTypeDeveloppement> EaeTypeDeveloppement.findAllEaeTypeDeveloppements() {
        return entityManager().createQuery("SELECT o FROM EaeTypeDeveloppement o", EaeTypeDeveloppement.class).getResultList();
    }
    
    public static List<EaeTypeDeveloppement> EaeTypeDeveloppement.findAllEaeTypeDeveloppements(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM EaeTypeDeveloppement o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, EaeTypeDeveloppement.class).getResultList();
    }
    
    public static EaeTypeDeveloppement EaeTypeDeveloppement.findEaeTypeDeveloppement(Integer idEaeTypeDeveloppement) {
        if (idEaeTypeDeveloppement == null) return null;
        return entityManager().find(EaeTypeDeveloppement.class, idEaeTypeDeveloppement);
    }
    
    public static List<EaeTypeDeveloppement> EaeTypeDeveloppement.findEaeTypeDeveloppementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeTypeDeveloppement o", EaeTypeDeveloppement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<EaeTypeDeveloppement> EaeTypeDeveloppement.findEaeTypeDeveloppementEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM EaeTypeDeveloppement o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, EaeTypeDeveloppement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeTypeDeveloppement.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeTypeDeveloppement.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeTypeDeveloppement attached = EaeTypeDeveloppement.findEaeTypeDeveloppement(this.idEaeTypeDeveloppement);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeTypeDeveloppement.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeTypeDeveloppement.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeTypeDeveloppement EaeTypeDeveloppement.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeTypeDeveloppement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
