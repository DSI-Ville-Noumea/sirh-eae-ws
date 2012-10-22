package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import flexjson.transformer.AbstractTransformer;

public class ObjectToPropertyTransformer extends AbstractTransformer {

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
				Method m = objectClass.getDeclaredMethod(potentialMethodName, null);
				value = m.invoke(object, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
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

}
