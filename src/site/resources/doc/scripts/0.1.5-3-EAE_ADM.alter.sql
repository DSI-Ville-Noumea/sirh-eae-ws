----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

alter table EAE_EVALUE rename column ANCIENNETE_ECHELON to ANCIENNETE_ECHELON_JOURS;
alter table EAE_EVALUE add (STATUT_PRECISION VARCHAR2(50));
alter table EAE_EVALUE modify (STATUT VARCHAR2(2));
update EAE_EVALUE set POSITION = 'AC';
alter table EAE_EVALUE modify (POSITION VARCHAR2(2));
alter table EAE drop column STATUT;

update EAE_FICHE_POSTE set VERSION = 0 where VERSION is null;
alter table EAE_FICHE_POSTE modify (VERSION NUMBER DEFAULT 0 not null);
alter table EAE_FICHE_POSTE add (PRIMAIRE number(1) default 0 not null check(PRIMAIRE in (0, 1)));
update EAE_FICHE_POSTE set PRIMAIRE = 1;
alter table EAE_FICHE_POSTE drop column TYPE_FDP;

alter table EAE_FDP_ACTIVITE drop column TYPE_ACTIVITE;

alter table EAE_RESULTAT drop column COMMENTAIRE;
alter table EAE_RESULTAT add (ID_EAE_COMMENTAIRE NUMBER(*,0));
alter table EAE_RESULTAT add CONSTRAINT FK_EAE_RESULT_EAE_COMMENT FOREIGN KEY (ID_EAE_COMMENTAIRE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);

alter table EAE drop column id_agent;
alter table EAE add (ID_EAE_COMMENTAIRE NUMBER(*,0));
alter table EAE add CONSTRAINT FK_EAE_EAE_COMMENTAIRE FOREIGN KEY (ID_EAE_COMMENTAIRE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);

update EAE_EVALUATION set VERSION = 0 where VERSION is null;
alter table EAE_EVALUATION modify (VERSION number default 0 not null);

alter table EAE_APPRECIATION add (TYPE_APPRECIATION varchar2(2) not null);
alter table EAE_APPRECIATION add (VERSION number default 0 not null);
alter table EAE_APPRECIATION modify (NUMERO number(2,0) not null);
alter table EAE_APPRECIATION add constraint EAE_UNIQUE_APPRECIATION unique(ID_EAE,NUMERO,TYPE_APPRECIATION) USING INDEX TABLESPACE TS_SIRHR_INDEX;
drop index EAE_APPRECIATION_index;