package nc.noumea.mairie.sirh.tools.transformer;

import nc.noumea.mairie.sirh.domain.Agent;
import flexjson.BasicType;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Inline;

public class SimpleAgentTransformer extends AbstractTransformer implements Inline {

	private boolean inline = Boolean.FALSE;
	
	@Override
    public Boolean isInline() {
        return inline;
    }
	
	public SimpleAgentTransformer() {
		
	}
	
	public SimpleAgentTransformer(boolean _inline) {
		this.inline = _inline;
	}
	
	@Override
	public void transform(Object arg0) {
		
		TypeContext typeContext = getContext().peekTypeContext();
	    
	    boolean isObjectNotInline = false;
	    
	    if (typeContext == null || typeContext.getBasicType() != BasicType.OBJECT || !inline) {
	        typeContext = getContext().writeOpenObject();
	        isObjectNotInline = true;
	    }
	    
	    Agent agent = (Agent) arg0;
	    
	    if (!typeContext.isFirst()) 
	    	getContext().writeComma();
	    
//	    typeContext.setFirst(false);
		
		getContext().writeName("idAgent");
	    getContext().transform(agent.getIdAgent());

	    getContext().writeComma();
	    getContext().writeName("nom");
	    getContext().transform(agent.getDisplayNom());

	    getContext().writeComma();
	    getContext().writeName("prenom");
	    getContext().transform(agent.getDisplayPrenom());
	    
	    if (isObjectNotInline) {
            getContext().writeCloseObject();
        }
	}

}
