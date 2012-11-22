package nc.noumea.mairie.sirh.domain;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaeParcoursPro;
import nc.noumea.mairie.sirh.eae.domain.comparator.EaeParcoursProByDateDebutInverseComparator;

import org.joda.time.DateTime;
import org.junit.Test;

public class EaeParcoursProByDateDebutInverseComparatorTest {

	@Test
	public void testComparatorLessThan() {
		// Given
		EaeParcoursPro p1 = new EaeParcoursPro();
		p1.setDateDebut(new DateTime(2010, 04, 15, 0, 0, 0, 0).toDate());
		
		EaeParcoursPro p2 = new EaeParcoursPro();
		p2.setDateDebut(new DateTime(2008, 07, 17, 0, 0, 0, 0).toDate());
		
		EaeParcoursProByDateDebutInverseComparator comparator = new EaeParcoursProByDateDebutInverseComparator();
		
		// When
		int result = comparator.compare(p1,  p2);
		
		// Then
		assertEquals(-1, result);
	}
	
	@Test
	public void testComparatorMoreThan() {
		// Given
		EaeParcoursPro p1 = new EaeParcoursPro();
		p1.setDateDebut(new DateTime(2010, 04, 15, 0, 0, 0, 0).toDate());
		
		EaeParcoursPro p2 = new EaeParcoursPro();
		p2.setDateDebut(new DateTime(2008, 07, 17, 0, 0, 0, 0).toDate());
		
		EaeParcoursProByDateDebutInverseComparator comparator = new EaeParcoursProByDateDebutInverseComparator();
		
		// When
		int result = comparator.compare(p2,  p1);
		
		// Then
		assertEquals(1, result);
	}
	
	@Test
	public void testComparatorEqual() {
		// Given
		EaeParcoursPro p1 = new EaeParcoursPro();
		p1.setDateDebut(new DateTime(2010, 04, 15, 0, 0, 0, 0).toDate());
		
		EaeParcoursProByDateDebutInverseComparator comparator = new EaeParcoursProByDateDebutInverseComparator();
		
		// When
		int result = comparator.compare(p1,  p1);
		
		// Then
		assertEquals(0, result);
	}
}
