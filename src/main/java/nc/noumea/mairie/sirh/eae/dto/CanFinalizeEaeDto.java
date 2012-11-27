package nc.noumea.mairie.sirh.eae.dto;

import flexjson.JSONSerializer;

public class CanFinalizeEaeDto implements IJSONSerialize {

	private boolean canFinalize;
	private String message;
	
	public CanFinalizeEaeDto() {
		
	}
	
	@Override
	public String serializeInJSON() {
		return new JSONSerializer().exclude("*.class").serialize(this);
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
