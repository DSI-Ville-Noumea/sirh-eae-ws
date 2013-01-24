----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

ALTER TABLE EAE_ADM.EAE_EVALUE ADD (AGENT_DETACHE NUMBER(1,0) DEFAULT 0 NOT NULL);
