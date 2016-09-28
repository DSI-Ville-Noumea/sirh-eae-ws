package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.Table;

import nc.noumea.mairie.sirh.eae.dto.EaeEvolutionSouhaitDto;

@Entity
@Table(name = "EAE_EVOL_SOUHAIT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeEvolutionSouhait {

	@Id
	@Column(name = "ID_EAE_EVOL_SOUHAIT")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idEaeEvolutionSouhait;

	@Column(name = "LIB_SOUHAIT")
	private String souhait;

	@Column(name = "LIB_SUGGESTION")
	private String suggestion;

	@OneToOne
	@JoinColumn(name = "ID_EAE_EVOLUTION")
	private EaeEvolution eaeEvolution;

	public EaeEvolutionSouhait() {
		
	}
	
	public EaeEvolutionSouhait(EaeEvolutionSouhaitDto dto) {
		this.idEaeEvolutionSouhait = dto.getIdEaeEvolutionSouhait();
		this.souhait = dto.getSouhait();
		this.suggestion = dto.getSuggestion();
	}
	
	public Integer getIdEaeEvolutionSouhait() {
		return idEaeEvolutionSouhait;
	}

	public void setIdEaeEvolutionSouhait(Integer idEaeEvolutionSouhait) {
		this.idEaeEvolutionSouhait = idEaeEvolutionSouhait;
	}

	public String getSouhait() {
		return souhait;
	}

	public void setSouhait(String souhait) {
		this.souhait = souhait;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public EaeEvolution getEaeEvolution() {
		return eaeEvolution;
	}

	public void setEaeEvolution(EaeEvolution eaeEvolution) {
		this.eaeEvolution = eaeEvolution;
	}
}
