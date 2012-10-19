-- Connecte en EAE_ADM
alter table EAE drop column id_agent;
alter table EAE_EVALUE rename column ANCIENNETE_ECHELON to ANCIENNETE_ECHELON_JOURS;
alter table EAE_EVALUE add (STATUT_PRECISION VARCHAR2(50));
alter table EAE_EVALUE modify (STATUT VARCHAR2(2));
update EAE_EVALUE set POSITION = 'AC';
alter table EAE_EVALUE modify (POSITION VARCHAR2(2));
update EAE_EVALUE set VERSION = 0 where VERSION is null;
alter table EAE drop column STATUT;