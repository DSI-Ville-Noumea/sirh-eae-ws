package nc.noumea.mairie.sirh.tools.transformer;

import nc.noumea.mairie.sirh.eae.domain.EaeFichePoste;
import flexjson.BasicType;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import flexjson.transformer.Inline;

public class EaeFichePosteToEaeListTransformer extends AbstractTransformer implements Inline {

	@Override
    public Boolean isInline() {
        return Boolean.TRUE;
    }
	
	@Override
	public void transform(Object object) {

		TypeContext typeContext = getContext().peekTypeContext();
	    
	    boolean isObjectNotInline = false;
	    
	    if (typeContext == null || typeContext.getBasicType() != BasicType.OBJECT) {
	        typeContext = getContext().writeOpenObject();
	        isObjectNotInline = true;
	    }
	    
	    EaeFichePoste fdp = (EaeFichePoste) object;
	    
	    if (!typeContext.isFirst()) 
	    	getContext().writeComma();
	    
//	    typeContext.setFirst(false);
	    
	    getContext().writeName("directionService");
	    getContext().transform(fdp.getDirectionService());

	    getContext().writeComma();
	    getContext().writeName("sectionService");
	    getContext().transform(fdp.getSectionService());

	    getContext().writeComma();
	    getContext().writeName("service");
	    getContext().transform(fdp.getService());
	    
	    getContext().writeComma();
	    getContext().writeName("agentShd");
	    getContext().transform(fdp.getAgentShd());
	    
	    if (isObjectNotInline) {
            getContext().writeCloseObject();
        }
	}

}
