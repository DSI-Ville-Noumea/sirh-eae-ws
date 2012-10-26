package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Type;
import java.util.HashMap;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

public class MinutesToHoursAndMinutesTransformer extends AbstractTransformer implements ObjectFactory {

	private static final String HOURS = "heures";
	private static final String MINUTES = "minutes";
	
	@Override
	public void transform(Object object) {

		Integer minutes = (Integer) object;
		Integer hours = (minutes/60);
		Integer minutesRemaining = minutes%60;
		
		getContext().writeOpenObject();
		
		getContext().writeName(HOURS);
		getContext().write(hours.toString());
		
		getContext().writeComma();
		getContext().writeName(MINUTES);
		getContext().write(minutesRemaining.toString());

		getContext().writeCloseObject();
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {

		if (value == null)
			return null;
		
		HashMap<String,Integer> hoursAndMinutes = (HashMap<String,Integer>) value;
		int hours = hoursAndMinutes.get(HOURS) != null ? hoursAndMinutes.get(HOURS) : 0;
		int minutes = hoursAndMinutes.get(MINUTES) != null ? hoursAndMinutes.get(MINUTES) : 0;
		
		return (hours*60) + minutes;
	}

}
