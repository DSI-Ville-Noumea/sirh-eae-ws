-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en EAE_ADM
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

ALTER TABLE EAE_EVALUATION drop column ID_EAE_NIVEAU;
ALTER TABLE EAE_EVALUATION add (NIVEAU varchar2(25));

