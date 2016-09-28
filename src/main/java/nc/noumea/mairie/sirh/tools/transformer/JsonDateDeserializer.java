package nc.noumea.mairie.sirh.tools.transformer;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import flexjson.JSONException;

@Component
public class JsonDateDeserializer extends JsonDeserializer<Date>{

	private static final String msDateFormat = "/[Dd][Aa][Tt][Ee]\\((\\-?[0-9]+)([\\+\\-]{1}[0-9]{4})*\\)/";
	private static final Pattern msDateFormatPattern = Pattern.compile(msDateFormat);
	
	@Override
	public Date deserialize(JsonParser arg0, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		
		if (arg0 == null) {
			return null;
		}
		
		Matcher matcher = msDateFormatPattern.matcher(arg0.getText());

		try {
			matcher.find();	
			
			String timestamp = matcher.group(1);
			String timeZone = matcher.group(2);
			
			DateTime dt;
			
			if (timeZone != null)
				dt = new DateTime(Long.parseLong(timestamp), DateTimeZone.forID(timeZone));
			else
				dt = new DateTime(Long.parseLong(timestamp), DateTimeZone.UTC);
			
			return dt.toDate();
		}
		catch(Exception ex) {
			throw new JSONException(String.format("Unable to parse '%s' as a valid date time. Expected format is '%s'", arg0.toString(), msDateFormat), ex);
		}
	}

}
