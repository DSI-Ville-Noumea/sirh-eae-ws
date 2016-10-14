package nc.noumea.mairie.alfresco.cmis;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import nc.noumea.mairie.alfresco.dto.GlobalPermissionDto;
import nc.noumea.mairie.alfresco.dto.PermissionDto;
import nc.noumea.mairie.alfresco.ws.AlfrescoWsConsumer;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

@Service
public class AlfrescoCMISService implements IAlfrescoCMISService {

	private Logger				logger	= LoggerFactory.getLogger(AlfrescoCMISService.class);

	@Autowired
	@Qualifier("alfrescoUrl")
	private String				alfrescoUrl;

	@Autowired
	@Qualifier("alfrescoLogin")
	private String				alfrescoLogin;

	@Autowired
	@Qualifier("alfrescoPassword")
	private String				alfrescoPassword;

	private static String		staticAlfrescoUrl;

	@Autowired
	private ISirhWsConsumer		sirhWsConsumer;

	@Autowired
	private CreateSession		createSession;

	@Autowired
	private AlfrescoWsConsumer	alfrescoWsConsumer;

	@PostConstruct
	public void init() {
		AlfrescoCMISService.staticAlfrescoUrl = alfrescoUrl;
	}

	@Override
	public ReturnMessageDto uploadDocument(Integer idAgent, EaeFinalizationDto eaeFinalizationDto, EaeFinalisation eaeFinalisation, Eae eae,
			ReturnMessageDto returnDto) {

		Session session = null;
		try {
			session = createSession.getSession(alfrescoUrl, alfrescoLogin, alfrescoPassword);
		} catch (CmisConnectionException e) {
			logger.error("Erreur de connexion a Alfresco CMIS : " + e.getMessage());
			returnDto.getErrors().add("Erreur de connexion à Alfresco CMIS");
			return returnDto;
		}

		Agent agentDto;
		try {
			agentDto = sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent());
		} catch (SirhWSConsumerException e1) {
			logger.error("L'application SIRH-WS ne répond pas.", e1.getMessage());
			returnDto.getErrors().add("L'application SIRH-WS ne répond pas.");
			return returnDto;
		}

		if (null == agentDto) {
			logger.debug("Alfresco - Agent non trouvé : " + eae.getEaeEvalue().getIdAgent());
			returnDto.getErrors().add("Alfresco - Agent non trouvé.");
			return returnDto;
		}

		// on cherche le repertoire distant

		String pathAgentEae = CmisUtils.getPathEAE(eae.getEaeEvalue().getIdAgent(), agentDto.getDisplayNom(), agentDto.getDisplayPrenom());
		CmisObject object = null;
		try {
			object = session.getObjectByPath(pathAgentEae);
		} catch (CmisUnauthorizedException e) {
			logger.error("Probleme d autorisation Alfresco CMIS : " + e.getMessage());
			returnDto.getErrors().add("Erreur Alfresco CMIS : non autorisé");
			return returnDto;
		} catch (CmisObjectNotFoundException e) {
			logger.debug("Le dossier agent n'existe pas sous Alfresco : " + e.getMessage());
			returnDto.getErrors().add("Impossible d'ajouter un document : répertoire distant non trouvé.");
			return returnDto;
		}

		if (null == object) {
			returnDto.getErrors().add(CmisUtils.ERROR_PATH);
			return returnDto;
		}

		Folder folder = (Folder) object;
		int maxItemsPerPage = 5;
		OperationContext operationContext = session.createOperationContext();
		operationContext.setMaxItemsPerPage(maxItemsPerPage);

		Document doc = null;
		boolean isCreated = false;
		int i = 0;
		while (!isCreated) {

			String name = CmisUtils.getPatternEAE(agentDto.getIdAgent(), eaeFinalizationDto.getAnnee(), i);

			// properties
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put(PropertyIds.NAME, name);
			properties.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
			properties.put(PropertyIds.DESCRIPTION, getDescriptionEae(eaeFinalizationDto.getAnnee(), agentDto));

			ByteArrayInputStream stream = new ByteArrayInputStream(eaeFinalizationDto.getbFile());

			ContentStream contentStream = new ContentStreamImpl(name, BigInteger.valueOf(eaeFinalizationDto.getbFile().length),
					eaeFinalizationDto.getTypeFile(), stream);

			// create a major version
			try {
				doc = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
				isCreated = true;
			} catch (CmisContentAlreadyExistsException e) {
				logger.debug(e.getMessage());
				i++;
			}
		}

		if (null == doc) {
			returnDto.getErrors().add(CmisUtils.ERROR_UPLOAD);
			return returnDto;
		}

		if (null != doc.getProperty("cmis:secondaryObjectTypeIds")) {
			List<Object> aspects = doc.getProperty("cmis:secondaryObjectTypeIds").getValues();
			if (!aspects.contains("P:mairie:customDocumentAspect")) {
				aspects.add("P:mairie:customDocumentAspect");
				HashMap<String, Object> props = new HashMap<String, Object>();
				props.put("cmis:secondaryObjectTypeIds", aspects);
				doc.updateProperties(props);
				logger.debug("Added aspect");
			} else {
				logger.debug("Doc already had aspect");
			}
		}

		HashMap<String, Object> props = new HashMap<String, Object>();
		props.put("mairie:idAgentOwner", eae.getEaeEvalue().getIdAgent());
		props.put("mairie:idAgentCreateur", idAgent);
		// props.put("mairie:commentaire", "");
		doc.updateProperties(props);

		eaeFinalisation.setNodeRefAlfresco(doc.getProperty("alfcmis:nodeRef").getFirstValue().toString());

		// on met les bons droits
		// car l agent n a pas le droit de voir son EAE tant qu il n est pas
		// controle
		setPermissionsEaeNonControle(doc.getProperty("alfcmis:nodeRef").getFirstValue().toString(), agentDto.getIdAgent(), agentDto.getDisplayNom(),
				agentDto.getDisplayPrenom());

		return returnDto;
	}

	private String getDescriptionEae(String annee, Agent agent) {
		String description = "";
		if (null != agent && null != annee) {
			description = "EAE de l'agent " + agent.getDisplayPrenom() + " " + agent.getDisplayNom() + " pour l'année " + annee;
		}

		return description;
	}

	/**
	 * exemple de nodeRef :
	 * "workspace://SpacesStore/1a344bd7-6422-45c6-94f7-5640048b20ab" exemple d
	 * URL a retourner :
	 * http://localhost:8080/alfresco/service/api/node/workspace
	 * /SpacesStore/418c511a-7c0a-4bb1-95a2-37e5946be726/content
	 * 
	 * @param nodeRef
	 *            String
	 * @return String l URL pour acceder au document directement a alfresco
	 */
	public static String getUrlOfDocument(String nodeRef) {

		return CmisUtils.getUrlOfDocument(staticAlfrescoUrl, nodeRef);
	}

	/**
	 * Pour un EAE Non Controle, seuls le SHD et la DRH ont le droit de le
	 * consulter : - au final on casse l heritage sur le document - et on met
	 * les droits au groupe - SITE_SIRH_ADRIEN_SALES_9005131_SHD - SITE_SIRH_DRH
	 * 
	 * @param nodeRef
	 *            NodeRef du document EAE
	 * @param idAgent
	 *            Id de l agent
	 * @param nom
	 *            Nom de l agent
	 * @param prenom
	 *            Prenom de l agent
	 */
	public void setPermissionsEaeNonControle(String nodeRef, Integer idAgent, String nom, String prenom) {

		// on ajoute les bons groupes
		// SITE_SIRH_DRH
		PermissionDto permissionGroupeDRH = new PermissionDto(CmisUtils.GROUPE_DRH, CmisUtils.ROLE_CONSUMER, false);
		// SHD
		PermissionDto permissionGroupeSHD = new PermissionDto(CmisUtils.getGroupeSHDOfAgent(idAgent, nom, prenom), CmisUtils.ROLE_CONSUMER, false);

		List<PermissionDto> permissions = new ArrayList<PermissionDto>();
		permissions.add(permissionGroupeDRH);
		permissions.add(permissionGroupeSHD);

		GlobalPermissionDto dto = new GlobalPermissionDto();
		// on casse l heritage
		dto.setIsInherited(false);
		dto.setPermissions(permissions);

		setPermissionsNode(nodeRef, dto);
	}

	/**
	 * Pour un EAE Controle, on remet l heritage sur le document - pour que les
	 * groupes suivants aient les bons droits : -
	 * SITE_SIRH_ADRIEN_SALES_9005131_SHD - SITE_SIRH_DRH -
	 * SITE_SIRH_ADRIEN_SALES_9005131
	 * 
	 * @param nodeRef
	 *            NodeRef du document EAE
	 */
	public void setPermissionsEaeControle(String nodeRef) {
		GlobalPermissionDto dto = new GlobalPermissionDto();
		// on remet l heritage
		dto.setIsInherited(true);

		setPermissionsNode(nodeRef, dto);
	}

	private void setPermissionsNode(String nodeRef, GlobalPermissionDto dto) {

		alfrescoWsConsumer.setAlfrescoUrl(alfrescoUrl);
		alfrescoWsConsumer.setAlfrescoLogin(alfrescoLogin);
		alfrescoWsConsumer.setAlfrescoPassword(alfrescoPassword);

		alfrescoWsConsumer.setPermissionsNode(nodeRef, dto);
	}

}
