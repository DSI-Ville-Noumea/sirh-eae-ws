package nc.noumea.mairie.sirh.eae.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceUnit;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EAE_EVOL_SOUHAIT")
@PersistenceUnit(unitName = "eaePersistenceUnit")
public class EaeEvolutionSouhait {

	@Id
	@SequenceGenerator(name = "eaeEvolutionSouhaitGen", sequenceName = "EAE_S_EVOL_SOUHAIT")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "eaeEvolutionSouhaitGen")
	@Column(name = "ID_EAE_EVOL_SOUHAIT")
	private Integer idEaeEvolutionSouhait;

	@Column(name = "LIB_SOUHAIT")
	@Lob
	private String souhait;
	
	@Column(name = "LIB_SUGGESTION")
	@Lob
	private String suggestion;
	
	@OneToOne
    @JoinColumn(name = "ID_EAE_EVOLUTION")
	private EaeEvolution eaeEvolution;

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
