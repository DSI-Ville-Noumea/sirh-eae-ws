package nc.noumea.mairie.sirh.eae.domain.comparator;

import java.util.Comparator;

import nc.noumea.mairie.sirh.eae.domain.EaeDeveloppement;

public class EaeDeveloppementComparator implements Comparator<EaeDeveloppement> {

	@Override
	public int compare(EaeDeveloppement o1, EaeDeveloppement o2) {
		if (o1.getPriorisation() > o2.getPriorisation())
			return 1;
		if (o1.getPriorisation() < o2.getPriorisation())
			return -1;
		return 0;
	}
}
