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

}
