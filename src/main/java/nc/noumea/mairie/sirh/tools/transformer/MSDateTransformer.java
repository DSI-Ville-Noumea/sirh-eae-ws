package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class MSDateTransformer extends AbstractTransformer implements ObjectFactory {

	private static final String msDateFormat = "/[Dd][Aa][Tt][Ee]\\(([0-9]+)\\)/";
	private static final Pattern msDateFormatPattern = Pattern.compile(msDateFormat);
	
	@Override
	public void transform(Object arg0) {
		Date theDate = (Date) arg0;
		String theDateInString;

		if (theDate == null)
			getContext().write(null);
		else {
			theDateInString = String.format("/Date(%s)/", theDate.getTime());
			getContext().writeQuoted(theDateInString);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
		
		Matcher matcher = msDateFormatPattern.matcher(value.toString());
		Calendar c = new GregorianCalendar();
		
		try {
			matcher.find();	
			String timestamp = matcher.group(1);
			c.setTimeInMillis(Long.parseLong(timestamp));
			return c.getTime();
		}
		catch(Exception ex) {
			throw new JSONException(String.format("Unable to parse '%s' as a valid date time. Expected format is '%s'", value.toString(), msDateFormat), ex);
		}
	}

}
