package nc.noumea.mairie.alfresco.cmis;

import static org.junit.Assert.assertEquals;
import nc.noumea.mairie.sirh.domain.Agent;
import nc.noumea.mairie.sirh.eae.domain.Eae;
import nc.noumea.mairie.sirh.eae.domain.EaeEvalue;
import nc.noumea.mairie.sirh.eae.domain.EaeFinalisation;
import nc.noumea.mairie.sirh.eae.dto.EaeFinalizationDto;
import nc.noumea.mairie.sirh.eae.dto.ReturnMessageDto;
import nc.noumea.mairie.sirh.ws.ISirhWsConsumer;
import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisUnauthorizedException;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class AlfrescoCMISServiceTest {

	private AlfrescoCMISService	alfrescoCMISService	= new AlfrescoCMISService();

	@Test
	public void uploadDocument_CmisConnectionException() {

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		ReturnMessageDto returnDto = new ReturnMessageDto();

		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
				.thenThrow(new CmisConnectionException());

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		assertEquals("Erreur de connexion à Alfresco CMIS", returnDto.getErrors().get(0));
	}

	@Test
	public void uploadDocument_SirhWSConsumerException() throws SirhWSConsumerException {

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005140);

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		eae.setEaeEvalue(eaeEvalue);
		ReturnMessageDto returnDto = new ReturnMessageDto();

		Session session = Mockito.mock(Session.class);
		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(session);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent())).thenThrow(new SirhWSConsumerException());

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);
		ReflectionTestUtils.setField(alfrescoCMISService, "sirhWsConsumer", sirhWsConsumer);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		assertEquals("L'application SIRH-WS ne répond pas.", returnDto.getErrors().get(0));
	}

	@Test
	public void uploadDocument_AgentNotFound() throws SirhWSConsumerException {

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005140);

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		eae.setEaeEvalue(eaeEvalue);
		ReturnMessageDto returnDto = new ReturnMessageDto();

		Session session = Mockito.mock(Session.class);
		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(session);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent())).thenReturn(null);

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);
		ReflectionTestUtils.setField(alfrescoCMISService, "sirhWsConsumer", sirhWsConsumer);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		assertEquals("Alfresco - Agent non trouvé.", returnDto.getErrors().get(0));
	}

	@Test
	public void uploadDocument_CmisUnauthorizedException() throws SirhWSConsumerException {

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005140);

		Agent agent = new Agent();
		agent.setIdAgent(eaeEvalue.getIdAgent());
		agent.setNomMarital("nomMarital");
		agent.setPrenom("prenom");

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		eae.setEaeEvalue(eaeEvalue);
		ReturnMessageDto returnDto = new ReturnMessageDto();

		Session session = Mockito.mock(Session.class);
		Mockito.when(session.getObjectByPath(CmisUtils.getPathEAE(agent.getIdAgent(), agent.getDisplayNom(), agent.getDisplayPrenom()))).thenThrow(
				new CmisUnauthorizedException());

		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(session);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent())).thenReturn(agent);

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);
		ReflectionTestUtils.setField(alfrescoCMISService, "sirhWsConsumer", sirhWsConsumer);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		assertEquals("Erreur Alfresco CMIS : non autorisé", returnDto.getErrors().get(0));
	}

	@Test
	public void uploadDocument_CmisObjectNotFoundException() throws SirhWSConsumerException {

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005140);

		Agent agent = new Agent();
		agent.setIdAgent(eaeEvalue.getIdAgent());
		agent.setNomMarital("nomMarital");
		agent.setPrenom("prenom");

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		eae.setEaeEvalue(eaeEvalue);
		ReturnMessageDto returnDto = new ReturnMessageDto();

		Session session = Mockito.mock(Session.class);
		Mockito.when(session.getObjectByPath(CmisUtils.getPathEAE(agent.getIdAgent(), agent.getDisplayNom(), agent.getDisplayPrenom()))).thenThrow(
				new CmisObjectNotFoundException());

		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(session);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent())).thenReturn(agent);

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);
		ReflectionTestUtils.setField(alfrescoCMISService, "sirhWsConsumer", sirhWsConsumer);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		assertEquals("Impossible d'ajouter un document : répertoire distant non trouvé.", returnDto.getErrors().get(0));
	}

	@Test
	public void uploadDocument_folderNull() throws SirhWSConsumerException {

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005140);

		Agent agent = new Agent();
		agent.setIdAgent(eaeEvalue.getIdAgent());
		agent.setNomMarital("nomMarital");
		agent.setPrenom("prenom");

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		eae.setEaeEvalue(eaeEvalue);
		ReturnMessageDto returnDto = new ReturnMessageDto();

		Session session = Mockito.mock(Session.class);
		Mockito.when(session.getObjectByPath(CmisUtils.getPathEAE(agent.getIdAgent(), agent.getDisplayNom(), agent.getDisplayPrenom()))).thenReturn(
				null);

		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(session);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent())).thenReturn(agent);

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);
		ReflectionTestUtils.setField(alfrescoCMISService, "sirhWsConsumer", sirhWsConsumer);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		assertEquals(CmisUtils.ERROR_PATH, returnDto.getErrors().get(0));
	}

	@Test
	public void uploadDocument_ok() throws SirhWSConsumerException {

		EaeEvalue eaeEvalue = new EaeEvalue();
		eaeEvalue.setIdAgent(9005140);

		Agent agent = new Agent();
		agent.setIdAgent(eaeEvalue.getIdAgent());
		agent.setNomMarital("nomMarital");
		agent.setPrenom("prenom");

		Integer idAgent = 9005138;
		EaeFinalizationDto eaeFinalizationDto = new EaeFinalizationDto();
		eaeFinalizationDto.setbFile(new byte[1]);
		EaeFinalisation eaeFinalisation = new EaeFinalisation();
		Eae eae = new Eae();
		eae.setEaeEvalue(eaeEvalue);
		ReturnMessageDto returnDto = new ReturnMessageDto();

		OperationContext operationContext = Mockito.mock(OperationContext.class);

		Folder object = Mockito.mock(Folder.class);

		Session session = Mockito.mock(Session.class);
		Mockito.when(session.getObjectByPath(CmisUtils.getPathEAE(agent.getIdAgent(), agent.getDisplayNom(), agent.getDisplayPrenom()))).thenReturn(
				object);
		Mockito.when(session.createOperationContext()).thenReturn(operationContext);

		CreateSession createSession = Mockito.mock(CreateSession.class);
		Mockito.when(createSession.getSession(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(session);

		ISirhWsConsumer sirhWsConsumer = Mockito.mock(ISirhWsConsumer.class);
		Mockito.when(sirhWsConsumer.getAgent(eae.getEaeEvalue().getIdAgent())).thenReturn(agent);

		ReflectionTestUtils.setField(alfrescoCMISService, "createSession", createSession);
		ReflectionTestUtils.setField(alfrescoCMISService, "sirhWsConsumer", sirhWsConsumer);

		returnDto = alfrescoCMISService.uploadDocument(idAgent, eaeFinalizationDto, eaeFinalisation, eae, returnDto);

		Mockito.verify(object, Mockito.times(1)).createDocument(Mockito.anyMapOf(String.class, Object.class), Mockito.any(ContentStream.class),
				Mockito.any(VersioningState.class));
	}
}
