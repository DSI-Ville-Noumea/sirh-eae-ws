package nc.noumea.mairie.sirh.eae.dto;

public class CanFinalizeEaeDto {

	private boolean canFinalize;
	private String message;
	
	public CanFinalizeEaeDto() {
		
	}

	public boolean isCanFinalize() {
		return canFinalize;
	}

	public void setCanFinalize(boolean canFinalize) {
		this.canFinalize = canFinalize;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
