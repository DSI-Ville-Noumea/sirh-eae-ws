----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

ALTER TABLE EAE_CAMPAGNE_ACTION add (DATE_MAIL_ENVOYE DATE);

ALTER TABLE EAE_EVALUATION modify (AVIS_SHD varchar2(20));

ALTER TABLE EAE_EVALUE ADD (AVCT_DUR_MIN NUMBER(4,0) default null);
ALTER TABLE EAE_EVALUE ADD (AVCT_DUR_MOY NUMBER(4,0) default null);
ALTER TABLE EAE_EVALUE ADD (AVCT_DUR_MAX NUMBER(4,0) default null);