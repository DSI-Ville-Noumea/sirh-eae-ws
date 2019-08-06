package nc.noumea.mairie.sirh.eae.service;

import java.io.IOException;

import nc.noumea.mairie.sirh.ws.SirhWSConsumerException;

public interface IMigrationEaeService {
	void exportEAEForMigration() throws IOException, SirhWSConsumerException;
}
