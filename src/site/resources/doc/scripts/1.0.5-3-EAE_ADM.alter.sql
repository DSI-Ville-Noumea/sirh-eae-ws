----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

ALTER TABLE EAE_CAMPAGNE_ACTION add (DATE_MAIL_ENVOYE DATE);

ALTER TABLE EAE_EVALUATION modify (AVIS_SHD varchar2(20));
