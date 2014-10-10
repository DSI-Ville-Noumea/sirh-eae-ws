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

		if(null == object) {
			return;
		}
		
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

		Integer hours = convertJSonNumberToInteger(context, hoursAndMinutes.get(HOURS), targetType);
		Integer minutes = convertJSonNumberToInteger(context, hoursAndMinutes.get(MINUTES), targetType);
		
		return (hours*60) + minutes;
	}
	
	private Integer convertJSonNumberToInteger(ObjectBinder context, Object value, Type targetType) {
		
		if (value instanceof Number) {
	        return ((Number) value).intValue();
	    } else {
	        try {
	            return Integer.parseInt(value.toString());
	        } catch (Exception e) {
	            throw context.cannotConvertValueToTargetType(value, Integer.class);
	        }
	    }
	}

}
