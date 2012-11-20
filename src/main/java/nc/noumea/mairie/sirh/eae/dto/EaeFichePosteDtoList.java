package nc.noumea.mairie.sirh.eae.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EaeFichePosteDtoList {

	private List<EaeFichePosteDto> eaeFichePostes;

	public List<EaeFichePosteDto> getEaeFichePostes() {
		return eaeFichePostes;
	}

	public void setEaeFichePostes(List<EaeFichePosteDto> eaeFichePostes) {
		this.eaeFichePostes = eaeFichePostes;
	}
}
