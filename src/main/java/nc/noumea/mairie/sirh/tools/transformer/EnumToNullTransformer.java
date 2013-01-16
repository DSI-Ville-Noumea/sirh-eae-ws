package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Type;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class EnumToNullTransformer implements ObjectFactory {

	@SuppressWarnings("rawtypes")
	@Override
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {
		return null;
	}
}
