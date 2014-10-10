package nc.noumea.mairie.sirh.tools.transformer;

import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import flexjson.BasicType;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Inline;

public class EaeEvalueToAgentTransformer extends AbstractTransformer implements Inline {

	private boolean inline = Boolean.FALSE;
	
	@Override
    public Boolean isInline() {
        return inline;
    }
	
	public EaeEvalueToAgentTransformer() {
		
	}
	
	public EaeEvalueToAgentTransformer(boolean _inline) {
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
	    
	    EaeEvalue eaeEvalue = (EaeEvalue) arg0;
	    Agent agent = eaeEvalue.getAgent();
	    
	    if (agent == null) {
	    	getContext().write(null);
	    	getContext().writeCloseObject();
	    	return;
	    }
	    
	    if (!typeContext.isFirst()) 
	    	getContext().writeComma();
	    
	    typeContext.setFirst(false);
		
		getContext().writeName("idAgent");
	    getContext().transform(agent.getIdAgent());

	    getContext().writeComma();
	    getContext().writeName("nom");
	    getContext().transform(agent.getDisplayNom());

	    getContext().writeComma();
	    getContext().writeName("nomJeuneFille");
	    getContext().transform(agent.getNomPatronymique());
	    
	    getContext().writeComma();
	    getContext().writeName("prenom");
	    getContext().transform(agent.getDisplayPrenom());
	    
	    getContext().writeComma();
	    getContext().writeName("dateNaissance");
	    getContext().transform(agent.getDateNaissance());
	    
	    if (isObjectNotInline) {
            getContext().writeCloseObject();
        }
	}

}
