package nc.noumea.mairie.alfresco.cmis;

import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;

public interface IAlfrescoCMISService {

	/**
	 * Upload de l EAE 
	 * @param idAgent Id de l agent
	 * @param eaeFinalizationDto L entite EaeFinalizationDto
	 * @param eaeFinalisation L entite EaeFinalization
	 * @param eae L entite EAE
	 * @param returnDto Le message de retour
	 * @return ReturnMessageDto Le message de retour
	 */
	ReturnMessageDto uploadDocument(Integer idAgent, EaeFinalizationDto eaeFinalizationDto,
			EaeFinalisation eaeFinalisation, Eae eae, ReturnMessageDto returnDto);
	
	/**
	 * Pour un EAE Non Controle, seuls le SHD et la DRH ont le droit de le
	 * consulter : - au final on casse l heritage sur le document - et on met
	 * les droits au groupe - SITE_SIRH_ADRIEN_SALES_9005131_SHD - SITE_SIRH_DRH
	 * 
	 * @param nodeRef
	 *            NodeRef du document EAE
	 * @param idAgent Id de l agent
	 * @param nom Nom de l agent
	 * @param prenom Prenom de l agent
	 */
	public void setPermissionsEaeNonControle(String nodeRef, Integer idAgent, String nom, String prenom);

	/**
	 * Pour un EAE Controle, on remet l heritage sur le document - pour que les
	 * groupes suivants aient les bons droits : -
	 * SITE_SIRH_ADRIEN_SALES_9005131_SHD - SITE_SIRH_DRH -
	 * SITE_SIRH_ADRIEN_SALES_9005131
	 * 
	 * @param nodeRef
	 *            NodeRef du document EAE
	 */
	public void setPermissionsEaeControle(String nodeRef);

}
