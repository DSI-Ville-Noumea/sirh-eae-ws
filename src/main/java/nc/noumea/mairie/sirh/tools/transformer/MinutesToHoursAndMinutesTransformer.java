package nc.noumea.mairie.sirh.tools.transformer;

import flexjson.transformer.AbstractTransformer;

public class MinutesToHoursAndMinutesTransformer extends AbstractTransformer {

	@Override
	public void transform(Object object) {

		Integer minutes = (Integer) object;
		Integer hours = (minutes/60);
		Integer minutesRemaining = minutes%60;
		
		getContext().writeOpenObject();
		
		getContext().writeName("heures");
		getContext().write(hours.toString());
		
		getContext().writeComma();
		getContext().writeName("minutes");
		getContext().write(minutesRemaining.toString());

		getContext().writeCloseObject();
	}

}
