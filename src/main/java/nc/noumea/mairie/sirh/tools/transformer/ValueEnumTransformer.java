package nc.noumea.mairie.sirh.tools.transformer;

import flexjson.transformer.AbstractTransformer;

@SuppressWarnings("rawtypes")
public class ValueEnumTransformer extends AbstractTransformer {
	
	@Override
	public void transform(Object object) {
		getContext().writeQuoted(((Enum) object).toString());
		
	}

}
