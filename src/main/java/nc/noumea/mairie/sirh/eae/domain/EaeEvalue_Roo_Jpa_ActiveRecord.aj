// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeEvalue_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeEvalue.entityManager;
    
    public static final EntityManager EaeEvalue.entityManager() {
        EntityManager em = new EaeEvalue().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeEvalue.countEaeEvalues() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeEvalue o", Long.class).getSingleResult();
    }
    
    public static List<EaeEvalue> EaeEvalue.findAllEaeEvalues() {
        return entityManager().createQuery("SELECT o FROM EaeEvalue o", EaeEvalue.class).getResultList();
    }
    
    public static EaeEvalue EaeEvalue.findEaeEvalue(Integer idEaeEvalue) {
        if (idEaeEvalue == null) return null;
        return entityManager().find(EaeEvalue.class, idEaeEvalue);
    }
    
    public static List<EaeEvalue> EaeEvalue.findEaeEvalueEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeEvalue o", EaeEvalue.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeEvalue.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeEvalue.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeEvalue attached = EaeEvalue.findEaeEvalue(this.idEaeEvalue);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeEvalue.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeEvalue.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeEvalue EaeEvalue.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeEvalue merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
