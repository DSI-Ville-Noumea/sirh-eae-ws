----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

drop index EAE_FORMATION_INDEX ;

alter table EAE_FORMATION add DUREE_FORMATION_NEW varchar2(50);
update EAE_FORMATION set DUREE_FORMATION_NEW = DUREE_FORMATION;
alter table EAE_FORMATION drop column DUREE_FORMATION;
alter table EAE_FORMATION rename column DUREE_FORMATION_NEW to DUREE_FORMATION;

alter table EAE_CAMPAGNE_ACTION add message_new clob;
update EAE_CAMPAGNE_ACTION set message_new = message;
alter table EAE_CAMPAGNE_ACTION drop column message;
alter table EAE_CAMPAGNE_ACTION rename column message_new to message;
alter table EAE_CAMPAGNE_ACTION drop column DIFFUSE;
