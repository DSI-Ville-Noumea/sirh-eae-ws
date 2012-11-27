----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

alter table EAE_FINALISATION add (COMMENTAIRE clob);
alter table EAE_FINALISATION drop column ID_EAE_COMMENTAIRE;
