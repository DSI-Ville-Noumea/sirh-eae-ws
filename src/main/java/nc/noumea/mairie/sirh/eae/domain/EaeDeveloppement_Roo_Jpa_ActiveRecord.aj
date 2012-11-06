// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeDeveloppement_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeDeveloppement.entityManager;
    
    public static final EntityManager EaeDeveloppement.entityManager() {
        EntityManager em = new EaeDeveloppement().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeDeveloppement.countEaeDeveloppements() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeDeveloppement o", Long.class).getSingleResult();
    }
    
    public static List<EaeDeveloppement> EaeDeveloppement.findAllEaeDeveloppements() {
        return entityManager().createQuery("SELECT o FROM EaeDeveloppement o", EaeDeveloppement.class).getResultList();
    }
    
    public static EaeDeveloppement EaeDeveloppement.findEaeDeveloppement(Integer idEaeDeveloppement) {
        if (idEaeDeveloppement == null) return null;
        return entityManager().find(EaeDeveloppement.class, idEaeDeveloppement);
    }
    
    public static List<EaeDeveloppement> EaeDeveloppement.findEaeDeveloppementEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeDeveloppement o", EaeDeveloppement.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeDeveloppement.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeDeveloppement.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeDeveloppement attached = EaeDeveloppement.findEaeDeveloppement(this.idEaeDeveloppement);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeDeveloppement.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeDeveloppement.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeDeveloppement EaeDeveloppement.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeDeveloppement merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}