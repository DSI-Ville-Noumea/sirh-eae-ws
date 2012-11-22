package nc.noumea.mairie.sirh.eae.domain.enums;

public enum EaeReportFormatEnum {

	DOC("DOC"),
	PDF("PDF");
	
	private String format;
	
	private EaeReportFormatEnum(String _format) {
		this.format = _format;
	}
	
	@Override
	public String toString() {
		return format;
	}
}
