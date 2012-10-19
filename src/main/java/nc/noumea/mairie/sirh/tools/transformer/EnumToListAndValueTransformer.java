package nc.noumea.mairie.sirh.tools.transformer;

import flexjson.JSONException;
import flexjson.transformer.AbstractTransformer;

public class EnumToListAndValueTransformer extends AbstractTransformer {

	private Class enumClass;
	
	public EnumToListAndValueTransformer(Class _enumClass) {
		this.enumClass = _enumClass;
	}
	
	@Override
	public void transform(Object object) {

		if (!enumClass.isEnum())
			throw new JSONException("Invalid type: expected Enum.");
		
		getContext().writeOpenObject();
		
		Enum value = (Enum) object;
		getContext().writeName("courant");
		getContext().writeQuoted(value.name());
		
		getContext().writeComma();
		getContext().writeName("liste");

		getContext().writeOpenArray();
		
		boolean isFirst = true;
		
		for (Object o : enumClass.getEnumConstants()) {
			Enum e = (Enum) o;
			
			if (!isFirst)
				getContext().writeComma();
			
			getContext().writeOpenObject();
			
			getContext().writeName("code");
			getContext().writeQuoted(e.name());
			
			getContext().writeComma();
			getContext().writeName("valeur");
			getContext().writeQuoted(e.toString());
			
			getContext().writeCloseObject();
			
			isFirst = false;
		}
		
		getContext().writeCloseArray();
		
		getContext().writeCloseObject();
	}

}
