package nc.noumea.mairie.sirh.eae.dto.planAction;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.EaePlanAction;

import org.junit.Test;

public class EaeItemPlanActionDtoTest {

	@Test
	public void testConstructor() {
		
		EaePlanAction planAction = new EaePlanAction();
		planAction.setObjectif("objectif");
		planAction.setIdEaePlanAction(1);
		
		EaeItemPlanActionDto dto = new EaeItemPlanActionDto(planAction);
		
		assertEquals(dto.getLibelle(), "objectif");
		assertEquals(dto.getIdItemPlanAction(), new Integer(1));
	}
}
