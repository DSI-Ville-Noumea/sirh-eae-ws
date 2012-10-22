-- Connecte en EAE_ADM
alter table EAE drop column id_agent;
alter table EAE_EVALUE rename column ANCIENNETE_ECHELON to ANCIENNETE_ECHELON_JOURS;
alter table EAE_EVALUE add (STATUT_PRECISION VARCHAR2(50));
alter table EAE_EVALUE modify (STATUT VARCHAR2(2));
update EAE_EVALUE set POSITION = 'AC';
alter table EAE_EVALUE modify (POSITION VARCHAR2(2));
update EAE_EVALUE set VERSION = 0 where VERSION is null;
alter table EAE drop column STATUT;
alter table EAE_FICHE_POSTE add (PRIMAIRE number(1) default 0 not null check(PRIMAIRE in (0, 1)));
update EAE_FICHE_POSTE set PRIMAIRE = 1;
alter table EAE_FICHE_POSTE drop column TYPE_FDP;
update EAE_FDP_ACTIVITE set VERSION = 0 where VERSION is null;
alter table EAE_FDP_ACTIVITE modify (VERSION NUMBER DEFAULT 0 not null);
alter table EAE_FDP_ACTIVITE drop column TYPE_ACTIVITE;