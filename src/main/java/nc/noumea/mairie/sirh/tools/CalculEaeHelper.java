package nc.noumea.mairie.sirh.tools;

import java.util.Date;

public class CalculEaeHelper {

	public static final String CHAINE_VIDE = "";
	final static long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24; 
	
	public static String getStatutCarriereEAE(String codeCategorie) {
		if (codeCategorie.equals("1") || codeCategorie.equals("2") || codeCategorie.equals("6")
				|| codeCategorie.equals("16") || codeCategorie.equals("17") || codeCategorie.equals("18")
				|| codeCategorie.equals("19") || codeCategorie.equals("20")) {
			return "F";
		} else if (codeCategorie.equals("7")) {
			return "CC";
		} else if (codeCategorie.equals("4")) {
			return "C";
		} else if (codeCategorie.equals("3")) {
			return "AL";
		} else if (codeCategorie.equals("5") || codeCategorie.equals("8") || codeCategorie.equals("9")
				|| codeCategorie.equals("10") || codeCategorie.equals("15") || codeCategorie.equals("11")) {
			return "A";
		} else {
			return CHAINE_VIDE;
		}
	}
	
	public static int compteJoursEntreDates(Date dateBefore, Date dateAfter) {
		
		if (dateBefore == null) return 0;
		if (dateAfter == null) return 0;
		
		
		long delta = dateAfter.getTime() - dateBefore.getTime();
		return Long.valueOf(delta / MILLISECONDS_PER_DAY).intValue();
	}
	
	public String getPositionAdmEAE(String codePA) {
		if (!codePA.equals("54") && !codePA.equals("56") && !codePA.equals("57")) {
			// activite
			return "AC";
		} else if (codePA.equals("56") || codePA.equals("57")) {
			// Mise à disposition
			return "MD";
		} else if (codePA.equals("54")) {
			// Détachement
			return "D";
		} else {
			// Autre
			return "A";
		}
	}
	
	public Date getDateAnterieure(Date date1, Date date2) {
		
		if (date1 == null && date2 != null) {
			return date2;
		} else if (date1 != null && date2 == null) {
			return date1;
		} else if (date1 != null && date2 != null) {
			if (date1.before(date2)) {
				return date1;
			} else {
				return date2;
			}
		}
		return null;
	}
	
}
