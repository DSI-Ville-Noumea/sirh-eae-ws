package nc.noumea.mairie.sirh.eae.domain.comparator;

import java.util.Comparator;

import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;

public class EaeParcoursProByDateDebutInverseComparator implements Comparator<EaeParcoursPro> {

	@Override
	public int compare(EaeParcoursPro o1, EaeParcoursPro o2) {
		return o2.getDateDebut().compareTo(o1.getDateDebut());
	}
}