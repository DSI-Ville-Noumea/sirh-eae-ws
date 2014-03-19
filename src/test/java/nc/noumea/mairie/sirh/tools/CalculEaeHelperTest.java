package nc.noumea.mairie.sirh.tools;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class CalculEaeHelperTest {

	@Test
	public void getStatutCarriereEAE() {
		
		List<String> listCodeCategorieF = Arrays.asList("1", "2", "6", "16", "17", "18", "19", "20");
		
		String result = null;
		for(String codeCategorie : listCodeCategorieF) {
			result = CalculEaeHelper.getStatutCarriereEAE(codeCategorie);
			assertEquals("F", result);
		}
		result = CalculEaeHelper.getStatutCarriereEAE("7");
		assertEquals("CC", result);
		result = CalculEaeHelper.getStatutCarriereEAE("4");
		assertEquals("C", result);
		result = CalculEaeHelper.getStatutCarriereEAE("3");
		assertEquals("AL", result);
		
		List<String> listCodeCategorieA = Arrays.asList("5", "8", "9", "10", "15", "11");
		for(String codeCategorie : listCodeCategorieA) {
			result = CalculEaeHelper.getStatutCarriereEAE(codeCategorie);
			assertEquals("A", result);
		}
	}
	
	@Test
	public void compteJoursEntreDates_dateBeforeNull() {
		
		int result = CalculEaeHelper.compteJoursEntreDates(null, new Date());
		assertEquals(0, result);
	}
	
	@Test
	public void compteJoursEntreDates_dateAfterNull() {
		
		int result = CalculEaeHelper.compteJoursEntreDates(new Date(), null);
		assertEquals(0, result);
	}
	
	@Test
	public void compteJoursEntreDates() {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		
		int result = CalculEaeHelper.compteJoursEntreDates(cal.getTime(), new Date());
		assertEquals(7, result);
	}
	
	@Test
	public void getPositionAdmEAE() {

		String result = null;
		
		List<String> listCodePA_MD = Arrays.asList("57", "56");
		for(String codePA : listCodePA_MD) {
			result = CalculEaeHelper.getPositionAdmEAE(codePA);
			assertEquals("MD", result);
		}
		result = CalculEaeHelper.getPositionAdmEAE("54");
		assertEquals("D", result);
		result = CalculEaeHelper.getPositionAdmEAE("5");
		assertEquals("AC", result);
	}
	
	@Test
	public void getDateAnterieure_date1Null() {
		
		Date date2 = new Date();
		Date result = CalculEaeHelper.getDateAnterieure(null, date2);
		assertEquals(date2, result);
	}
	
	@Test
	public void getDateAnterieure_date2Null() {
		
		Date date1 = new Date();
		Date result = CalculEaeHelper.getDateAnterieure(date1, null);
		assertEquals(date1, result);
	}
	
	@Test
	public void getDateAnterieure_date2Anterieure() {
		
		Date date1 = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		
		Date result = CalculEaeHelper.getDateAnterieure(date1, cal.getTime());
		assertEquals(cal.getTime(), result);
	}
	
	@Test
	public void getDateAnterieure_date1Anterieure() {
		
		Date date1 = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 7);
		
		Date result = CalculEaeHelper.getDateAnterieure(date1, cal.getTime());
		assertEquals(date1, result);
	}
}
