
-- connecte en SYSTEM

----------------------------------------------------------------
-- creation des roles et users
create role R_EAE_ADM;
create role R_EAE_USR;
create role R_EAE_READ;


grant connect, create session, create table, create sequence, create public synonym to R_EAE_ADM;
grant unlimited tablespace to R_EAE_ADM;
grant connect, create session to R_EAE_USR;
grant connect, create session to R_EAE_READ;

create user EAE_ADM identified by PASSWORD_SECRET_SIE;
create user EAE_USR identified by PASSWORD_SECRET_SIE_2;
create user EAE_READ identified by PASSWORD_DONNER_AU_SED;



grant R_EAE_ADM to EAE_ADM;
grant R_EAE_USR to EAE_USR;
grant R_EAE_READ to EAE_READ;



----------------------------------------------------------------
-- Creation des tablespaces : finaliser les nomns de fichiers par le SIE

-- petit, prevoir des extends de 20 Mo, initial 20 Mo
CREATE TABLESPACE TS_SIRHR_PARAM DATAFILE
'E:\oradata\ORADEV\dbfusers\ORADEV_ts_dev.dbf'
SIZE 20M AUTOEXTEND ON NEXT 20M MAXSIZE 100M
LOGGING
ONLINE
PERMANENT
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 512K
BLOCKSIZE 8K
SEGMENT SPACE MANAGEMENT AUTO
FLASHBACK OFF;


-- prevoir des extends de 100 Mo, initial 50 Mo
CREATE TABLESPACE TS_SIRHR_DATA DATAFILE
'E:\oradata\ORADEV\dbfusers\ORADEV_ts_dev.dbf'
SIZE 50M AUTOEXTEND ON NEXT 100M MAXSIZE 2000M
LOGGING
ONLINE
PERMANENT
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 512K
BLOCKSIZE 8K
SEGMENT SPACE MANAGEMENT AUTO
FLASHBACK OFF;


-- moyen, prevoir des extends de 100 Mo, initial 20 Mo
CREATE TABLESPACE TS_SIRHR_INDEX DATAFILE
'E:\oradata\ORADEV\dbfusers\ORADEV_ts_dev.dbf'
SIZE 20M AUTOEXTEND ON NEXT 100M MAXSIZE 2000M
LOGGING
ONLINE
PERMANENT
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 512K
BLOCKSIZE 8K
SEGMENT SPACE MANAGEMENT AUTO
FLASHBACK OFF;


-- le plus petit possible, pas d'extend, bloque
CREATE TABLESPACE TS_SIRHR_DEFAULT DATAFILE
'E:\oradata\ORADEV\dbfusers\ORADEV_ts_dev.dbf'
SIZE 10M AUTOEXTEND OFF MAXSIZE 2000M
LOGGING
ONLINE
PERMANENT
EXTENT MANAGEMENT LOCAL UNIFORM SIZE 512K
BLOCKSIZE 8K
SEGMENT SPACE MANAGEMENT AUTO
FLASHBACK OFF;

alter tablespace TS_DEFAULT read only;


-- on redirige par defaut sur le tablespace USERS pour flagger les mises en recette sauvages...
alter user EAE_ADM default tablespace TS_DEFAULT;

-- fin de la section admin bdd

----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

--==============================================================
-- Table: EAE_CAMPAGNE_EAE
--==============================================================
create sequence ID_CAMPAGNE_EAE_SEQ 
start with 1 
increment by 1 
nomaxvalue;

create public synonym ID_CAMPAGNE_EAE_SEQ for ID_CAMPAGNE_EAE_SEQ;
grant select on ID_CAMPAGNE_EAE_SEQ to R_EAE_USR;


 
create table EAE_CAMPAGNE_EAE
(
   ID_CAMPAGNE_EAE INTEGER not null,
   ANNEE INTEGER not null,
   DATE_DEBUT DATE,
   DATE_FIN DATE,
   DATE_OUVERTURE_KIOSQUE DATE,
   DATE_FERMETURE_KIOSQUE DATE,
   COMMENTAIRE VARCHAR2(255),
   constraint PK_CAMPAGNE_EAE
   primary key (ID_CAMPAGNE_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_CAMPAGNE_EAE for EAE_CAMPAGNE_EAE;
grant select, insert, update, delete on EAE_CAMPAGNE_EAE to R_EAE_USR;
grant select on EAE_CAMPAGNE_EAE to R_EAE_READ;


--==============================================================
-- Table: EAE_CAMPAGNE_ACTION
--==============================================================
create sequence ID_CAMPAGNE_ACTION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym ID_CAMPAGNE_ACTION_SEQ for ID_CAMPAGNE_ACTION_SEQ;
grant select on EAE_CAMPAGNE_EAE to R_EAE_USR;



create table EAE_CAMPAGNE_ACTION
(
   ID_CAMPAGNE_ACTION INTEGER ,
   ID_CAMPAGNE_EAE INTEGER not null,
   NOM_ACTION VARCHAR2(50) not null,
   MESSAGE VARCHAR2(100) not null,
   DATE_TRANSMISSION DATE not null,
   DIFFUSE INTEGER not null,
   DATE_A_FAIRE_LE DATE,
   DATE_FAIT_LE DATE ,
   COMMENTAIRE VARCHAR2(255),
   ID_AGENT_REALISATION INTEGER not null,
   constraint PK_CAMPAGNE_ACTION
   primary key (ID_CAMPAGNE_ACTION),
   constraint FK_CAMPAGNE_EAE
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_CAMPAGNE_ACTION for EAE_CAMPAGNE_ACTION;
grant select, insert, update, delete on EAE_CAMPAGNE_ACTION to R_EAE_USR;
grant select on EAE_CAMPAGNE_ACTION to R_EAE_READ;


--==============================================================
-- Table: EAE_CAMPAGNE_ACTEURS
--==============================================================
create sequence EAE_ID_CAMPAGNE_ACTEURS_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_ID_CAMPAGNE_ACTEURS_SEQ for EAE_ID_CAMPAGNE_ACTEURS_SEQ;
grant select on EAE_ID_CAMPAGNE_ACTEURS_SEQ to R_EAE_USR;


create table EAE_CAMPAGNE_ACTEURS
(
   ID_CAMPAGNE_ACTEURS INTEGER not null ,
   ID_CAMPAGNE_ACTION INTEGER not null,
   ID_AGENT INTEGER not null,
   constraint PK_CAMPAGNE_ACTEURS
   primary key (ID_CAMPAGNE_ACTEURS),
   constraint FK_CAMPAGNE_ACTION
         foreign key (ID_CAMPAGNE_ACTION)
         references EAE_CAMPAGNE_ACTION(ID_CAMPAGNE_ACTION)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_CAMPAGNE_ACTEURS for EAE_CAMPAGNE_ACTEURS;
grant select, insert, update, delete on EAE_CAMPAGNE_ACTEURS to R_EAE_USR;
grant select on EAE_CAMPAGNE_ACTEURS to R_EAE_READ;


--==============================================================
-- Table: EAE
--==============================================================
create sequence EAE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_SEQ for EAE_SEQ;
grant select on EAE_SEQ to R_EAE_USR;


create table EAE
(
   ID_EAE INTEGER not null,
   ID_CAMPAGNE_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   ID_SHD INTEGER,
   DIRECTION_SERVICE VARCHAR2(10),
   SECTION_SERVICE VARCHAR2(10),
   SERVICE VARCHAR2(10),
   STATUT VARCHAR2(10),
   ETAT VARCHAR2(2) not null,
   CAP INTEGER not null,
   DOC_ATTACHE INTEGER not null,
   DATE_CREATION DATE ,
   DATE_FIN DATE,   
   DATE_ENTRETIEN DATE,
   DUREE_ENTRETIEN INTEGER ,
   DATE_FINALISE DATE,
   DATE_CONTROLE DATE ,
   HEURE_CONTROLE VARCHAR2(5) ,
   USER_CONTROLE VARCHAR2(7),
   ID_DELEGATAIRE INTEGER ,
   constraint PK_EAE
   primary key (ID_EAE),
   constraint FK_EAE_CAMPAGNE_EAE
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE for EAE;
grant select, insert, update, delete on EAE to R_EAE_USR;
grant select on EAE to R_EAE_READ;


CREATE UNIQUE INDEX EAE_index
   ON EAE (ID_CAMPAGNE_EAE,ID_AGENT);
--==============================================================
-- Table: EAE_EVALUATEUR
--==============================================================
create sequence EAE_EVALUATEUR_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_EVALUATEUR_SEQ for EAE_EVALUATEUR_SEQ;
grant select on EAE_EVALUATEUR_SEQ to R_EAE_USR;


create table EAE_EVALUATEUR
(
   ID_EAE_EVALUATEUR INTEGER not null,
   ID_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   FONCTION VARCHAR2(100),
   DATE_ENTREE_SERVICE DATE,
   DATE_ENTREE_COLLECTIVITE DATE,
   DATE_ENTREE_FONCTION DATE,
   VERSION NUMBER,
   constraint PK_EAE_EVALUATEUR
   primary key (ID_EAE_EVALUATEUR),
   constraint FK_EAE_EVALUATEUR
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_EVALUATEUR for EAE_EVALUATEUR;
grant select, insert, update, delete on EAE_EVALUATEUR to R_EAE_USR;
grant select on EAE_EVALUATEUR to R_EAE_READ;


CREATE UNIQUE INDEX EAE_EVALUATEUR_index
   ON EAE_EVALUATEUR(ID_EAE,ID_AGENT);
--==============================================================
-- Table: EAE_EVALUE
--==============================================================
create sequence EAE_EVALUE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_EVALUE_SEQ for EAE_EVALUE_SEQ;
grant select on EAE_EVALUE_SEQ to R_EAE_USR;


create table EAE_EVALUE
(
   ID_EAE_EVALUE INTEGER not null,
   ID_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   DATE_ENTREE_SERVICE DATE,
   DATE_ENTREE_COLLECTIVITE DATE,
   DATE_ENTREE_FONCTIONNAIRE DATE,
   DATE_ENTREE_ADMINISTRATION DATE,
   STATUT VARCHAR2(10),
   ANCIENNETE_ECHELON INTEGER,
   CADRE VARCHAR2(30),
   CATEGORIE VARCHAR2(1),
   CLASSIFICATION VARCHAR2(50),
   GRADE VARCHAR2(50),
   ECHELON VARCHAR2(60),
   DATE_EFFET_AVCT DATE,
   NOUV_GRADE VARCHAR2(50),
   NOUV_ECHELON VARCHAR2(60),
   POSITION VARCHAR2(50),
   TYPE_AVCT INTEGER ,
   constraint PK_EAE_EVALUE
   primary key (ID_EAE_EVALUE),
   constraint FK_EAE_EVALUE
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_EVALUE for EAE_EVALUE;
grant select, insert, update, delete on EAE_EVALUE to R_EAE_USR;
grant select on EAE_EVALUE to R_EAE_READ;


CREATE UNIQUE INDEX EAE_EVALUE_index
   ON EAE_EVALUE (ID_EAE,ID_AGENT);
--==============================================================
-- Table: EAE_FICHE_POSTE
--==============================================================
create sequence EAE_FICHE_POSTE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_FICHE_POSTE_SEQ for EAE_FICHE_POSTE_SEQ;
grant select on EAE_FICHE_POSTE_SEQ to R_EAE_USR;


create table EAE_FICHE_POSTE
(
   ID_EAE_FICHE_POSTE INTEGER not null,
   ID_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   TYPE_FDP VARCHAR2(60),
   DIRECTION_SERV VARCHAR2(60),
   SERVICE_SERV VARCHAR2(60),
   SECTION_SERV VARCHAR2(60),
   EMPLOI VARCHAR2(100),
   FONCTION VARCHAR2(100),
   DATE_ENTREE_FONCTION DATE,
   GRADE_POSTE VARCHAR2(50),
   LOCALISATION VARCHAR2(60),
   MISSIONS CLOB,
   FONCTION_RESP VARCHAR2(100),
   NOM_RESP VARCHAR2(100),
   PRENOM_RESP VARCHAR2(100),
   constraint PK_EAE_FICHE_POSTE
   primary key (ID_EAE_FICHE_POSTE),
   constraint FK_EAE_FICHE_POSTE
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_FICHE_POSTE for EAE_FICHE_POSTE;
grant select, insert, update, delete on EAE_FICHE_POSTE to R_EAE_USR;
grant select on EAE_FICHE_POSTE to R_EAE_READ;



CREATE UNIQUE INDEX EAE_FICHE_POSTE_index
   ON EAE_FICHE_POSTE (ID_EAE,ID_AGENT,TYPE_FDP);
--==============================================================
-- Table: EAE_FDP_ACTIVITES
--==============================================================
create sequence EAE_FDP_ACTIVITES_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_FDP_ACTIVITES_SEQ for EAE_FDP_ACTIVITES_SEQ;
grant select on EAE_FDP_ACTIVITES_SEQ to R_EAE_USR;


create table EAE_FDP_ACTIVITES
(
   ID_EAE_FDP_ACTIVITES INTEGER not null,
   ID_EAE_FICHE_POSTE INTEGER not null,
   TYPE_ACTIVITE VARCHAR2(100),
   LIBELLE_ACTIVITE VARCHAR2(255),
   constraint PK_EAE_FDP_ACTIVITES
   primary key (ID_EAE_FDP_ACTIVITES),
   constraint FK_EAE_FDP_ACTIVITES
         foreign key (ID_EAE_FICHE_POSTE)
         references EAE_FICHE_POSTE(ID_EAE_FICHE_POSTE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_FDP_ACTIVITES for EAE_FDP_ACTIVITES;
grant select, insert, update, delete on EAE_FDP_ACTIVITES to R_EAE_USR;
grant select on EAE_FDP_ACTIVITES to R_EAE_READ;


CREATE UNIQUE INDEX EAE_FDP_ACTIVITES_index
   ON EAE_FDP_ACTIVITES (ID_EAE_FICHE_POSTE,TYPE_ACTIVITE,LIBELLE_ACTIVITE);
--==============================================================
-- Table: EAE_DIPLOME
--==============================================================
create sequence EAE_DIPLOME_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_DIPLOME_SEQ for EAE_DIPLOME_SEQ;
grant select on EAE_DIPLOME_SEQ to R_EAE_USR;


create table EAE_DIPLOME
(
   ID_EAE_DIPLOME INTEGER not null,
   ID_EAE INTEGER not null,
   LIBELLE_DIPLOME VARCHAR2(255),
   constraint PK_EAE_DIPLOME
   primary key (ID_EAE_DIPLOME),
   constraint FK_EAE_DIPLOME
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_DIPLOME for EAE_DIPLOME;
grant select, insert, update, delete on EAE_DIPLOME to R_EAE_USR;
grant select on EAE_DIPLOME to R_EAE_READ;


CREATE UNIQUE INDEX EAE_DIPLOME_index
   ON EAE_DIPLOME (id_eae,LIBELLE_DIPLOME);
--==============================================================
-- Table: EAE_PARCOURS_PRO
--==============================================================
create sequence EAE_PARCOURS_PRO_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_PARCOURS_PRO_SEQ for EAE_PARCOURS_PRO_SEQ;
grant select on EAE_PARCOURS_PRO_SEQ to R_EAE_USR;



create table EAE_PARCOURS_PRO
(
   ID_EAE_PARCOURS_PRO INTEGER not null,
   ID_EAE INTEGER not null,
   DATE_DEBUT DATE,
   DATE_FIN DATE,
   LIBELLE_PARCOURS_PRO VARCHAR2(255),
   constraint PK_EAE_PARCOURS_PRO
   primary key (ID_EAE_PARCOURS_PRO),
   constraint FK_EAE_PARCOURS_PRO
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_PARCOURS_PRO for EAE_PARCOURS_PRO;
grant select, insert, update, delete on EAE_PARCOURS_PRO to R_EAE_USR;
grant select on EAE_PARCOURS_PRO to R_EAE_READ;



CREATE UNIQUE INDEX eae_parcours_pro_index
   ON eae_parcours_pro (id_eae,date_debut);

--==============================================================
-- Table: EAE_FORMATION
--==============================================================
create sequence EAE_FORMATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_FORMATION_SEQ for EAE_FORMATION_SEQ;
grant select on EAE_FORMATION_SEQ to R_EAE_USR;


create table EAE_FORMATION
(
   ID_EAE_FORMATION INTEGER not null,
   ID_EAE INTEGER not null,
   ANNEE_FORMATION Integer not null,
   DUREE_FORMATION Integer not null,
   LIBELLE_FORMATION VARCHAR2(255),
   constraint PK_EAE_FORMATION
   primary key (ID_EAE_FORMATION),
   constraint FK_EAE_FORMATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_FORMATION for EAE_FORMATION;
grant select, insert, update, delete on EAE_FORMATION to R_EAE_USR;
grant select on EAE_FORMATION to R_EAE_READ;


CREATE UNIQUE INDEX eae_FORMATION_index
   ON eae_FORMATION (id_eae,LIBELLE_FORMATION);

--==============================================================
-- Table: EAE_TYPE_RESULTAT
--==============================================================
create sequence EAE_TYPE_RESULTAT_SEQ 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_TYPE_RESULTAT_SEQ for EAE_TYPE_RESULTAT_SEQ;
grant select on EAE_TYPE_RESULTAT_SEQ to R_EAE_USR;



create table EAE_TYPE_RESULTAT
(
   ID_EAE_TYPE_RESULTAT INTEGER not null,
   LIBELLE_TYPE_RESULTAT VARCHAR2(50),
   constraint PK_EAE_TYPE_RESULTAT
   primary key (ID_EAE_TYPE_RESULTAT)
)
TABLESPACE TS_SIRHR_PARAM;

create public synonym EAE_TYPE_RESULTAT for EAE_TYPE_RESULTAT;
grant select, insert, update, delete on EAE_TYPE_RESULTAT to R_EAE_USR;
grant select on EAE_TYPE_RESULTAT to R_EAE_READ;


--==============================================================
-- Table: EAE_RESULTAT
--==============================================================
create sequence EAE_RESULTAT_SEQ 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_RESULTAT_SEQ for EAE_RESULTAT_SEQ;
grant select on EAE_RESULTAT_SEQ to R_EAE_USR;


create table EAE_RESULTAT
(
   ID_EAE_RESULTAT INTEGER not null,
   ID_EAE INTEGER not null,
   ID_TYPE_RESULTAT INTEGER not null,
   OBJECTIF VARCHAR2(255),
   DETAIL_OBJECTIF VARCHAR2(255),
   RESULTAT VARCHAR2(255),
   COMMENTAIRE VARCHAR2(255),
   constraint PK_EAE_RESULTAT
   primary key (ID_EAE_RESULTAT),
   constraint FK_EAE_TYPE_RESULTAT
         foreign key (ID_TYPE_RESULTAT)
         references EAE_TYPE_RESULTAT(ID_EAE_TYPE_RESULTAT),
   constraint FK_EAE_RESULTAT
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_RESULTAT for EAE_RESULTAT;
grant select, insert, update, delete on EAE_RESULTAT to R_EAE_USR;
grant select on EAE_RESULTAT to R_EAE_READ;


CREATE UNIQUE INDEX eae_RESULTAT_index
   ON eae_RESULTAT (id_eae,ID_TYPE_RESULTAT,OBJECTIF);

--==============================================================
-- Table: EAE_PLAN_ACTION
--==============================================================
create sequence EAE_PLAN_ACTION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_PLAN_ACTION_SEQ for EAE_PLAN_ACTION_SEQ;
grant select on EAE_PLAN_ACTION_SEQ to R_EAE_USR;




create table EAE_PLAN_ACTION
(
   ID_EAE_PLAN_ACTION INTEGER not null,
   ID_EAE INTEGER not null,
   ID_TYPE_RESULTAT INTEGER not null,
   OBJECTIF VARCHAR2(255),
   MESURE VARCHAR2(255),
   constraint PK_EAE_PLAN_ACTION
   primary key (ID_EAE_PLAN_ACTION),
   constraint FK_EAE_TYPE_RES_PLAN_ACTION
         foreign key (ID_TYPE_RESULTAT)
         references EAE_TYPE_RESULTAT(ID_EAE_TYPE_RESULTAT),
   constraint FK_EAE_PLAN_ACTION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_PLAN_ACTION for EAE_PLAN_ACTION;
grant select, insert, update, delete on EAE_PLAN_ACTION to R_EAE_USR;
grant select on EAE_PLAN_ACTION to R_EAE_READ;


CREATE UNIQUE INDEX EAE_PLAN_ACTION_index
   ON EAE_PLAN_ACTION (id_eae,ID_TYPE_RESULTAT,OBJECTIF);

--==============================================================
-- Table: EAE_NIVEAU_EAE
--==============================================================
create sequence EAE_NIVEAU_EAE_SEQ 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_NIVEAU_EAE_SEQ for EAE_NIVEAU_EAE_SEQ;
grant select on EAE_NIVEAU_EAE_SEQ to R_EAE_USR;




create table EAE_NIVEAU_EAE
(
   ID_EAE_NIVEAU_EAE INTEGER not null,
   LIBELLE_NIVEAU_EAE VARCHAR2(50),
   constraint PK_EAE_NIVEAU_EAE
   primary key (ID_EAE_NIVEAU_EAE)
)
TABLESPACE TS_SIRHR_PARAM;

create public synonym EAE_NIVEAU_EAE for EAE_NIVEAU_EAE;
grant select, insert, update, delete on EAE_NIVEAU_EAE to R_EAE_USR;
grant select on EAE_NIVEAU_EAE to R_EAE_READ;



--==============================================================
-- Table: EAE_EVALUATION
--==============================================================
create sequence EAE_EVALUATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_EVALUATION_SEQ for EAE_EVALUATION_SEQ;
grant select on EAE_EVALUATION_SEQ to R_EAE_USR;


create table EAE_EVALUATION
(
   ID_EAE_EVALUATION INTEGER not null,
   ID_EAE INTEGER not null,
   ID_EAE_NIVEAU INTEGER,
   NOTE_ANNEE DECIMAL(4,2),
   NOTE_ANNEE_N1 DECIMAL(4,2),
   NOTE_ANNEE_N2 DECIMAL(4,2),
   NOTE_ANNEE_N3 DECIMAL(4,2),
   AVIS VARCHAR2(50),
   AVANCEMENT_DIFF VARCHAR2(50),
   CHANGEMENT_CLASSE VARCHAR2(50),
   AVIS_SHD VARCHAR2(50),
   VERSION NUMBER,
   constraint PK_EAE_EVALUATION
   primary key (ID_EAE_EVALUATION),
   constraint FK_EAE_NIVEAU_EAE
         foreign key (ID_EAE_NIVEAU)
         references EAE_NIVEAU_EAE(ID_EAE_NIVEAU_EAE),
   constraint FK_EAE_EVALUATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_EVALUATION for EAE_EVALUATION;
grant select, insert, update, delete on EAE_EVALUATION to R_EAE_USR;
grant select on EAE_EVALUATION to R_EAE_READ;

--==============================================================
-- Table: EAE_DOCUMENT
--==============================================================
create sequence EAE_DOCUMENT_SEQ 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_DOCUMENT_SEQ for EAE_DOCUMENT_SEQ;
grant select on EAE_DOCUMENT_SEQ to R_EAE_USR;


create table EAE_DOCUMENT
(
   ID_EAE_DOCUMENT INTEGER not null,
   ID_CAMPAGNE_EAE INTEGER not null,
   ID_CAMPAGNE_ACTION INTEGER,
   ID_DOCUMENT INTEGER not null,
   TYPE_DOCUMENT VARCHAR2(30),
   constraint PK_EAE_DOCUMENT
   primary key (ID_EAE_DOCUMENT),
   constraint FK_EAE_DOCUMENT
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_DOCUMENT for EAE_DOCUMENT;
grant select, insert, update, delete on EAE_DOCUMENT to R_EAE_USR;
grant select on EAE_DOCUMENT to R_EAE_READ;



--==============================================================
-- Table: EAE_APPRECIATION
--==============================================================
create sequence EAE_APPRECIATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_APPRECIATION_SEQ for EAE_APPRECIATION_SEQ;
grant select on EAE_APPRECIATION_SEQ to R_EAE_USR;


create table EAE_APPRECIATION
(
   ID_EAE_APPRECIATION INTEGER not null,
   ID_EAE INTEGER not null,
   NUMERO VARCHAR2(50),
   NOTE_EVALUE VARCHAR2(1),
   NOTE_EVALUATEUR VARCHAR2(1),
   constraint PK_EAE_APPRECIATION
   primary key (ID_EAE_APPRECIATION),
   constraint FK_EAE_APPRECIATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_APPRECIATION for EAE_APPRECIATION;
grant select, insert, update, delete on EAE_APPRECIATION to R_EAE_USR;
grant select on EAE_APPRECIATION to R_EAE_READ;



CREATE UNIQUE INDEX EAE_APPRECIATION_index
   ON EAE_APPRECIATION (id_eae,NUMERO);

--==============================================================
-- Table: EAE_AUTO_EVALUATION
--==============================================================
create sequence EAE_AUTO_EVALUATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_AUTO_EVALUATION_SEQ for EAE_AUTO_EVALUATION_SEQ;
grant select on EAE_AUTO_EVALUATION_SEQ to R_EAE_USR;


create table EAE_AUTO_EVALUATION
(
   ID_EAE_AUTO_EVALUATION INTEGER not null,
   ID_EAE INTEGER not null,
   ELEMENT VARCHAR2(255),
   OBJECTIF_ATTEINT VARCHAR2(255),
   COMPETENCES_ACQUISES VARCHAR2(255),
   SUCCES VARCHAR2(255),
   constraint PK_EAE_AUTO_EVALUATION
   primary key (ID_EAE_AUTO_EVALUATION),
   constraint FK_EAE_AUTO_EVALUATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_AUTO_EVALUATION for EAE_AUTO_EVALUATION;
grant select, insert, update, delete on EAE_AUTO_EVALUATION to R_EAE_USR;
grant select on EAE_AUTO_EVALUATION to R_EAE_READ;



--==============================================================
-- Table: EAE_EVOLUTION
--==============================================================
create sequence EAE_EVOLUTION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_EVOLUTION_SEQ for EAE_EVOLUTION_SEQ;
grant select on EAE_EVOLUTION_SEQ to R_EAE_USR;


create table EAE_EVOLUTION
(
   ID_EAE_EVOLUTION INTEGER not null,
   ID_EAE INTEGER not null,
   MOBILIE_GEO INTEGER not null,
   MOBILIE_FONCT INTEGER not null,
   CHANGEMENT_METIER INTEGER not null,
   DELAI_ENVISAGE VARCHAR2(50),
   MOBILITE_SERVICE INTEGER not null,
   MOBILITE_DIRECTION INTEGER not null,
   MOBILITE_COLLECTIVITE INTEGER not null,
   MOBILITE_AUTRE INTEGER not null,
   NOM_ENTITE VARCHAR2(100),
   CONCOURS INTEGER not null,
   NOM_CONCOURS VARCHAR2(100),
   DEMANDE_VAE INTEGER not null,
   NOM_DIPLOME VARCHAR2(100),
   TEMPS_PARTIEL INTEGER not null,
   POURC_TEMPS_PARTIEL INTEGER,
   RETRAITE INTEGER not null,
   DATE_RETRAITE DATE,
   AUTRE_PERSPECTIVE INTEGER not null,
   PERSPECTIVE VARCHAR2(255),
   COMMENTAIRE VARCHAR2(255),
   constraint PK_EAE_EVOLUTION
   primary key (ID_EAE_EVOLUTION),
   constraint FK_EAE_EVOLUTION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_EVOLUTION for EAE_EVOLUTION;
grant select, insert, update, delete on EAE_EVOLUTION to R_EAE_USR;
grant select on EAE_EVOLUTION to R_EAE_READ;



--==============================================================
-- Table: EAE_EVOL_SOUHAIT
--==============================================================
create sequence EAE_EVOL_SOUHAIT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_EVOL_SOUHAIT_SEQ for EAE_EVOL_SOUHAIT_SEQ;
grant select on EAE_EVOL_SOUHAIT_SEQ to R_EAE_USR;



create table EAE_EVOL_SOUHAIT
(
   ID_EAE_EVOL_SOUHAIT INTEGER not null,
   ID_EAE INTEGER not null,
   TYPE_SOUHAIT VARCHAR2(50),
   LIB_SOUHAIT VARCHAR2(255),
   constraint PK_EAE_EVOL_SOUHAIT
   primary key (ID_EAE_EVOL_SOUHAIT),
   constraint FK_EAE_EVOL_SOUHAIT
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_EVOL_SOUHAIT for EAE_EVOL_SOUHAIT;
grant select, insert, update, delete on EAE_EVOL_SOUHAIT to R_EAE_USR;
grant select on EAE_EVOL_SOUHAIT to R_EAE_READ;



--==============================================================
-- Table: EAE_TYPE_DEVELOPPEMENT
--==============================================================
create sequence EAE_TYPE_DEVELOPPEMENT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_TYPE_DEVELOPPEMENT_SEQ for EAE_TYPE_DEVELOPPEMENT_SEQ;
grant select on EAE_TYPE_DEVELOPPEMENT_SEQ to R_EAE_USR;


create table EAE_TYPE_DEVELOPPEMENT
(
   ID_EAE_TYPE_DEVELOPPEMENT INTEGER not null,
   LIBELLE_TYPE_DEVELOPPEMENT VARCHAR2(50),
   constraint PK_EAE_TYPE_DEVELOPPEMENT
   primary key (ID_EAE_TYPE_DEVELOPPEMENT)
)
TABLESPACE TS_SIRHR_PARAM;

create public synonym EAE_TYPE_DEVELOPPEMENT for EAE_TYPE_DEVELOPPEMENT;
grant select, insert, update, delete on EAE_TYPE_DEVELOPPEMENT to R_EAE_USR;
grant select on EAE_TYPE_DEVELOPPEMENT to R_EAE_READ;



--==============================================================
-- Table: EAE_DEVELOPPEMENT
--==============================================================
create sequence EAE_DEVELOPPEMENT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 

create public synonym EAE_DEVELOPPEMENT_SEQ for EAE_DEVELOPPEMENT_SEQ;
grant select on EAE_DEVELOPPEMENT_SEQ to R_EAE_USR;


create table EAE_DEVELOPPEMENT
(
   ID_EAE_DEVELOPPEMENT INTEGER not null,
   ID_EAE INTEGER not null,
   ID_TYPE_DEVELOPPEMENT INTEGER not null,
   LIBELLE_DEVELOPPEMENT VARCHAR2(255),
   ECHEANCE_DEVELOPPEMENT VARCHAR2(50),
   PRIORISATION_DEVELOPPEMENT INTEGER ,
   constraint PK_EAE_DEVELOPPEMENT
   primary key (ID_EAE_DEVELOPPEMENT),
   constraint FK_EAE_TYPE_DEVELOPPEMENT
         foreign key (ID_TYPE_DEVELOPPEMENT)
         references EAE_TYPE_DEVELOPPEMENT(ID_EAE_TYPE_DEVELOPPEMENT),
   constraint FK_EAE_DEVELOPPEMENT
         foreign key (ID_EAE)
         references EAE(ID_EAE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_DEVELOPPEMENT for EAE_DEVELOPPEMENT;
grant select, insert, update, delete on EAE_DEVELOPPEMENT to R_EAE_USR;
grant select on EAE_DEVELOPPEMENT to R_EAE_READ;

