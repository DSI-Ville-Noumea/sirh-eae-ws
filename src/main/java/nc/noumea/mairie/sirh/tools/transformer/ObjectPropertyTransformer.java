package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Field;

import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Inline;

public class ObjectPropertyTransformer extends AbstractTransformer implements
		Inline {

	private String propertyToInclude;

	public ObjectPropertyTransformer() {
	}

	public ObjectPropertyTransformer(String propertyName) {
		setPropertyToInclude(propertyName);
	}

	@Override
	public Boolean isInline() {
		return Boolean.TRUE;
	}

	@Override
	public void transform(Object arg0) {

		getContext().writeName(getPropertyToInclude());

		if (arg0 == null) {
			getContext().write(null);
			return;
		}

		try {

			Field f = arg0.getClass().getDeclaredField(getPropertyToInclude());
			boolean isAccessible = f.isAccessible();

			if (!isAccessible)
				f.setAccessible(true);

			getContext().transform(f.get(arg0));

			if (!isAccessible)
				f.setAccessible(false);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getPropertyToInclude() {
		return propertyToInclude;
	}

	public void setPropertyToInclude(String propertyToInclude) {
		this.propertyToInclude = propertyToInclude;
	}

}
