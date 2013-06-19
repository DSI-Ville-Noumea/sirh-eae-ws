package nc.noumea.mairie.mairie.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;

import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(persistenceUnit = "sirhPersistenceUnit", table = "SPBHOR", versionField = "")
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

}
