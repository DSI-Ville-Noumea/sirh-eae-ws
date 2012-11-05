// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.noumea.mairie.sirh.eae.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import nc.noumea.mairie.sirh.eae.domain.EaeEvolutionSouhait;
import org.springframework.transaction.annotation.Transactional;

privileged aspect EaeEvolutionSouhait_Roo_Jpa_ActiveRecord {
    
    @PersistenceContext(unitName = "eaePersistenceUnit")
    transient EntityManager EaeEvolutionSouhait.entityManager;
    
    public static final EntityManager EaeEvolutionSouhait.entityManager() {
        EntityManager em = new EaeEvolutionSouhait().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long EaeEvolutionSouhait.countEaeEvolutionSouhaits() {
        return entityManager().createQuery("SELECT COUNT(o) FROM EaeEvolutionSouhait o", Long.class).getSingleResult();
    }
    
    public static List<EaeEvolutionSouhait> EaeEvolutionSouhait.findAllEaeEvolutionSouhaits() {
        return entityManager().createQuery("SELECT o FROM EaeEvolutionSouhait o", EaeEvolutionSouhait.class).getResultList();
    }
    
    public static EaeEvolutionSouhait EaeEvolutionSouhait.findEaeEvolutionSouhait(Integer idEaeEvolutionSouhait) {
        if (idEaeEvolutionSouhait == null) return null;
        return entityManager().find(EaeEvolutionSouhait.class, idEaeEvolutionSouhait);
    }
    
    public static List<EaeEvolutionSouhait> EaeEvolutionSouhait.findEaeEvolutionSouhaitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM EaeEvolutionSouhait o", EaeEvolutionSouhait.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void EaeEvolutionSouhait.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void EaeEvolutionSouhait.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            EaeEvolutionSouhait attached = EaeEvolutionSouhait.findEaeEvolutionSouhait(this.idEaeEvolutionSouhait);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void EaeEvolutionSouhait.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void EaeEvolutionSouhait.clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public EaeEvolutionSouhait EaeEvolutionSouhait.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        EaeEvolutionSouhait merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
}
