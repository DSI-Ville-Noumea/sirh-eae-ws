package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import flexjson.JSONException;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.transformer.AbstractTransformer;

@SuppressWarnings(value = { "rawtypes", "unchecked" })
public class ObjectToPropertyTransformer extends AbstractTransformer implements
		ObjectFactory {

	private String property;
	private Class objectClass;

	public ObjectToPropertyTransformer() {

	}

	public ObjectToPropertyTransformer(String _property, Class _objectClass) {
		this.property = _property;
		this.objectClass = _objectClass;
	}

	@Override
	public void transform(Object object) {

		if (object == null)
			return;

		Object value = null;

		try {
			Field f = objectClass.getDeclaredField(property);
			f.setAccessible(true);
			value = f.get(object);
		} catch (NoSuchFieldException ex) {
			// do nothing, just try again with a method name
		} catch (IllegalArgumentException e) {
			// do nothing, just try again with a method name
		} catch (IllegalAccessException e) {
			// do nothing, just try again with a method name
		}

		if (value == null) {

			try {
				String potentialMethodName = String.format("%s%s%s", "get",
						property.substring(0, 1).toUpperCase(),
						property.substring(1, property.length()));
				Method m = objectClass.getDeclaredMethod(potentialMethodName,
						(Class[]) null);
				value = m.invoke(object, (Object[]) null);
			} catch (Exception e) {
				throw new RuntimeException(String.format(
						"Could not get field or getter for property '%s'",
						property));
			}
		}

		if (value == null)
			getContext().write(null);
		else
			getContext().writeQuoted(value.toString());
	}

	@Override
	public Object instantiate(ObjectBinder context, Object value,
			Type targetType, Class targetClass) {

		Object obj = null;

		try {
			Constructor c = objectClass.getDeclaredConstructors()[0];
			obj = c.newInstance((Object[]) null);

			Field f = objectClass.getDeclaredField(property);
			f.setAccessible(true);
			f.set(obj, value);

		} catch (Exception ex) {
			throw new JSONException(
					String.format(
							"Unable to parse '%s' as a valid property value for the given object. Target class is '%s', target property is '%s'",
							value.toString(), objectClass.toString(), property),
					ex);
		}

		return obj;
	}

}
