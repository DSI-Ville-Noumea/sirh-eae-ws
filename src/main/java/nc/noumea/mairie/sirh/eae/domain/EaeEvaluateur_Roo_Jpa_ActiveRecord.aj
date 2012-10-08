// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluateur;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeEvaluateur_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeEvaluateur.entityManager;
    
    public static final EntityManager EaeEvaluateur.entityManager() {
        EntityManager em = new EaeEvaluateur().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeEvaluateur.countEaeEvaluateurs() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeEvaluateur o", Long.class).getSingleResult();
    }
    
    public static List<EaeEvaluateur> EaeEvaluateur.findAllEaeEvaluateurs() {
        return entityManager().createQuery("SELECT o FROM EaeEvaluateur o", EaeEvaluateur.class).getResultList();
    }
    
    public static EaeEvaluateur EaeEvaluateur.findEaeEvaluateur(Integer idEaeEvaluateur) {
        if (idEaeEvaluateur == null) return null;
        return entityManager().find(EaeEvaluateur.class, idEaeEvaluateur);
    }
    
    public static List<EaeEvaluateur> EaeEvaluateur.findEaeEvaluateurEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeEvaluateur o", EaeEvaluateur.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeEvaluateur.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeEvaluateur.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeEvaluateur attached = EaeEvaluateur.findEaeEvaluateur(this.idEaeEvaluateur);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeEvaluateur.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeEvaluateur.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeEvaluateur EaeEvaluateur.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeEvaluateur merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
