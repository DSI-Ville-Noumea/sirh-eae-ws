----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

DROP INDEX EAE_RESULTAT_INDEX;

ALTER TABLE EAE_EVOLUTION ADD (TEMPS_PARTIEL_ID_SPBHOR NUMBER(5,0));
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 11 WHERE POURC_TEMPS_PARTIEL <= 100 AND POURC_TEMPS_PARTIEL > 80;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 7 WHERE POURC_TEMPS_PARTIEL <= 80 AND POURC_TEMPS_PARTIEL > 75;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 5 WHERE POURC_TEMPS_PARTIEL <= 75 AND POURC_TEMPS_PARTIEL > 70;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 12 WHERE POURC_TEMPS_PARTIEL <= 70 AND POURC_TEMPS_PARTIEL > 67;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 8 WHERE POURC_TEMPS_PARTIEL <= 67 AND POURC_TEMPS_PARTIEL > 60;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 6 WHERE POURC_TEMPS_PARTIEL <= 60 AND POURC_TEMPS_PARTIEL > 50;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 2 WHERE POURC_TEMPS_PARTIEL <= 50 AND POURC_TEMPS_PARTIEL > 40;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 10 WHERE POURC_TEMPS_PARTIEL <= 40 AND POURC_TEMPS_PARTIEL > 33;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 3 WHERE POURC_TEMPS_PARTIEL <= 33 AND POURC_TEMPS_PARTIEL > 25;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 4 WHERE POURC_TEMPS_PARTIEL <= 25 AND POURC_TEMPS_PARTIEL > 20;
UPDATE EAE_EVOLUTION SET TEMPS_PARTIEL_ID_SPBHOR = 9 WHERE POURC_TEMPS_PARTIEL <= 20 AND POURC_TEMPS_PARTIEL >= 0;
ALTER TABLE EAE_EVOLUTION DROP COLUMN POURC_TEMPS_PARTIEL;
commit;

ALTER TABLE EAE_EVALUE ADD (EST_ENCADRANT NUMBER(1,0) DEFAULT 0 NOT NULL); 
