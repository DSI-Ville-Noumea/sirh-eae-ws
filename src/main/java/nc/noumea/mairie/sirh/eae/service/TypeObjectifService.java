package nc.noumea.mairie.sirh.eae.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import nc.noumea.mairie.sirh.eae.domain.EaeTypeObjectif;

import org.springframework.stereotype.Service;

@Service
public class TypeObjectifService implements ITypeObjectifService {

	@PersistenceContext(unitName = "eaePersistenceUnit")
	private EntityManager eaeEntityManager;
	
	@Override
	public EaeTypeObjectif getTypeObjectifForLibelle(String libelle) {

		CriteriaBuilder builder = eaeEntityManager.getCriteriaBuilder();
		CriteriaQuery<EaeTypeObjectif> criteriaQuery = builder.createQuery(EaeTypeObjectif.class);
		Root<EaeTypeObjectif> eaeTypeObjectif = criteriaQuery.from(EaeTypeObjectif.class);
		criteriaQuery.where(builder.equal(eaeTypeObjectif.get("libelle"), libelle));
		
		return eaeEntityManager.createQuery(criteriaQuery).getSingleResult();
	}

}
