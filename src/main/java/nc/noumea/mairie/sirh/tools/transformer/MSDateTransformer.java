package nc.noumea.mairie.sirh.tools.transformer;

import java.util.Date;

import flexjson.transformer.AbstractTransformer;

public class MSDateTransformer extends AbstractTransformer {

	@Override
	public void transform(Object arg0) {
		Date theDate = (Date) arg0;
		String theDateInString;
		
		if (theDate == null)
			getContext().write(null);
		else {
			theDateInString = String.format("/DATE(%s)/", theDate.getTime());
			getContext().writeQuoted(theDateInString);
		}
	}

}
