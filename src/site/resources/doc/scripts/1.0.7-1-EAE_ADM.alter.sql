----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

DROP INDEX EAE_RESULTAT_INDEX;

ALTER TABLE EAE_EVOLUTION ADD (TEMPS_PARTIEL_ID_SPBHOR NUMBER(5,0));