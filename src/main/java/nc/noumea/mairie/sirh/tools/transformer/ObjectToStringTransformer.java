package nc.noumea.mairie.sirh.tools.transformer;

import java.lang.reflect.Field;

import flexjson.transformer.AbstractTransformer;

public class ObjectToStringTransformer extends AbstractTransformer {

	private String property;
	private Class objectClass;
	
	public ObjectToStringTransformer() {
		
	}
	
	public ObjectToStringTransformer(String _property, Class _objectClass) {
		this.property = _property;
		this.objectClass = _objectClass;
	}
	
	@Override
	public void transform(Object object) {
		
		if (object == null)
			return;
		
		try {
			Field f = objectClass.getDeclaredField(property);
			f.setAccessible(true);
			Object value = f.get(object);
			
			if (value == null)
				getContext().write(null);
			else
				getContext().writeQuoted(value.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
