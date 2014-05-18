package nc.noumea.mairie.mairie.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceUnit;
import javax.persistence.PostLoad;
import javax.persistence.Table;

@Entity
@Table(name = "SPBHOR")
@PersistenceUnit(unitName = "sirhPersistenceUnit")
@NamedQuery(name = "Spbhor.whereCdTauxNotZero", query = "SELECT sp from Spbhor sp WHERE (sp.taux <> 0 and sp.taux <> 1) order by sp.taux DESC")
public class Spbhor {

	@Id
	@Column(name = "CDTHOR", columnDefinition = "decimal")
	private Integer cdThor;

	@Column(name = "LIBHOR", columnDefinition = "char")
	private String label;

	@Column(name = "CDTAUX", columnDefinition = "decimal")
	private Double taux;

	@PostLoad
	protected void repair() {
		if (label != null)
			label = label.trim();
	}

	public Integer getCdThor() {
		return cdThor;
	}

	public void setCdThor(Integer cdThor) {
		this.cdThor = cdThor;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getTaux() {
		return taux;
	}

	public void setTaux(Double taux) {
		this.taux = taux;
	}

}
