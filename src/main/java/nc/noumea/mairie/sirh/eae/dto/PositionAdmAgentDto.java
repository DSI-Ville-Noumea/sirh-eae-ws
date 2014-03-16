package nc.noumea.mairie.sirh.eae.dto;

public class PositionAdmAgentDto {

	private Integer nomatr;
	private Integer datdeb;
	private Integer datfin;
	private String cdpadm;
	
	public Integer getNomatr() {
		return nomatr;
	}
	public void setNomatr(Integer nomatr) {
		this.nomatr = nomatr;
	}
	public Integer getDatdeb() {
		return datdeb;
	}
	public void setDatdeb(Integer datdeb) {
		this.datdeb = datdeb;
	}
	public Integer getDatfin() {
		return datfin;
	}
	public void setDatfin(Integer datfin) {
		this.datfin = datfin;
	}
	public String getCdpadm() {
		return cdpadm;
	}
	public void setCdpadm(String cdpadm) {
		this.cdpadm = cdpadm;
	}
	
}
