package nc.noumea.mairie.sirh.tools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

@Service
public class Helper implements IHelper {
	
	@Override
	public Date getCurrentDate() {
		Calendar c = new GregorianCalendar();
		return c.getTime();
	}

	/**
	 * Converts a 900xxxx IdAgent into an employeeNumber readable one: 90xxxx
	 * 
	 * @param idAgent
	 *            900xxxx
	 * @return 90xxxx
	 */
	@Override
	public String getEmployeeNumber(Integer idAgent) {
		return "90" + String.valueOf(idAgent).substring(3, String.valueOf(idAgent).length());
	}

}
