package nc.noumea.mairie.sirh.eae.dto;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvaluation;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeAvancementEnum;
import nc.noumea.mairie.sirh.eae.domain.enums.EaeEtatEnum;

import org.junit.Test;

public class EaeDashboardItemDtoTest {

	@Test
	public void testIncrementCountersWithEae_EaeIsNAAvancementIsNonDefini() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.NA);

		// When
		EaeDashboardItemDto dto = new EaeDashboardItemDto();
		dto.incrementCountersWithEae(eae);

		// Then
		assertEquals(1, dto.getNonAffecte());
		assertEquals(0, dto.getNonDebute());
		assertEquals(0, dto.getCree());
		assertEquals(0, dto.getEnCours());
		assertEquals(0, dto.getFinalise());
		assertEquals(0, dto.getFige());

		assertEquals(1, dto.getNonDefini());
		assertEquals(0, dto.getMini());
		assertEquals(0, dto.getMaxi());
		assertEquals(0, dto.getMoy());
		assertEquals(0, dto.getChangClasse());
	}

	@Test
	public void testIncrementCountersWithEae_EaeIsNDAvancementIsMini() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.ND);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setPropositionAvancement(EaeAvancementEnum.MINI);
		eae.setEaeEvaluation(eval);

		// When
		EaeDashboardItemDto dto = new EaeDashboardItemDto();
		dto.incrementCountersWithEae(eae);

		// Then
		assertEquals(0, dto.getNonAffecte());
		assertEquals(1, dto.getNonDebute());
		assertEquals(0, dto.getCree());
		assertEquals(0, dto.getEnCours());
		assertEquals(0, dto.getFinalise());
		assertEquals(0, dto.getFige());

		assertEquals(0, dto.getNonDefini());
		assertEquals(1, dto.getMini());
		assertEquals(0, dto.getMaxi());
		assertEquals(0, dto.getMoy());
		assertEquals(0, dto.getChangClasse());
	}

	@Test
	public void testIncrementCountersWithEae_EaeIsCAvancementIsMaxiAvisDefavorable() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.C);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setPropositionAvancement(EaeAvancementEnum.MAXI);
		eval.setAvisChangementClasse(0);
		eae.setEaeEvaluation(eval);

		// When
		EaeDashboardItemDto dto = new EaeDashboardItemDto();
		dto.incrementCountersWithEae(eae);

		// Then
		assertEquals(0, dto.getNonAffecte());
		assertEquals(0, dto.getNonDebute());
		assertEquals(1, dto.getCree());
		assertEquals(0, dto.getEnCours());
		assertEquals(0, dto.getFinalise());
		assertEquals(0, dto.getFige());

		assertEquals(0, dto.getNonDefini());
		assertEquals(0, dto.getMini());
		assertEquals(1, dto.getMaxi());
		assertEquals(0, dto.getMoy());
		assertEquals(0, dto.getChangClasse());
	}

	@Test
	public void testIncrementCountersWithEae_EaeIsECAvancementIsMoyAvisFavorable() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.EC);
		EaeEvaluation eval = new EaeEvaluation();
		eval.setPropositionAvancement(EaeAvancementEnum.MOY);
		eval.setAvisChangementClasse(1);
		eae.setEaeEvaluation(eval);

		// When
		EaeDashboardItemDto dto = new EaeDashboardItemDto();
		dto.incrementCountersWithEae(eae);

		// Then
		assertEquals(0, dto.getNonAffecte());
		assertEquals(0, dto.getNonDebute());
		assertEquals(0, dto.getCree());
		assertEquals(1, dto.getEnCours());
		assertEquals(0, dto.getFinalise());
		assertEquals(0, dto.getFige());

		assertEquals(0, dto.getNonDefini());
		assertEquals(0, dto.getMini());
		assertEquals(0, dto.getMaxi());
		assertEquals(1, dto.getMoy());
		assertEquals(1, dto.getChangClasse());
	}

	@Test
	public void testIncrementCountersWithEae_EaeIsFAvancementIsNonDefini() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.F);

		// When
		EaeDashboardItemDto dto = new EaeDashboardItemDto();
		dto.incrementCountersWithEae(eae);

		// Then
		assertEquals(0, dto.getNonAffecte());
		assertEquals(0, dto.getNonDebute());
		assertEquals(0, dto.getCree());
		assertEquals(0, dto.getEnCours());
		assertEquals(1, dto.getFinalise());
		assertEquals(0, dto.getFige());

		assertEquals(1, dto.getNonDefini());
		assertEquals(0, dto.getMini());
		assertEquals(0, dto.getMaxi());
		assertEquals(0, dto.getMoy());
		assertEquals(0, dto.getChangClasse());
	}

	@Test
	public void testIncrementCountersWithEae_EaeIsCOAvancementIsNonDefini() {
		// Given
		Eae eae = new Eae();
		eae.setEtat(EaeEtatEnum.CO);

		// When
		EaeDashboardItemDto dto = new EaeDashboardItemDto();
		dto.incrementCountersWithEae(eae);

		// Then
		assertEquals(0, dto.getNonAffecte());
		assertEquals(0, dto.getNonDebute());
		assertEquals(0, dto.getCree());
		assertEquals(0, dto.getEnCours());
		assertEquals(0, dto.getFinalise());
		assertEquals(1, dto.getFige());

		assertEquals(1, dto.getNonDefini());
		assertEquals(0, dto.getMini());
		assertEquals(0, dto.getMaxi());
		assertEquals(0, dto.getMoy());
		assertEquals(0, dto.getChangClasse());
	}
}
