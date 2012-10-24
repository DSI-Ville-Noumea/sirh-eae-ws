----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

----------------------------------------------------------------
-- EAE_FDP_COMPETENCE
----------------------------------------------------------------
create sequence EAE_S_FDP_COMPETENCE 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_S_FDP_COMPETENCE for EAE_S_FDP_COMPETENCE;
grant select on EAE_S_FDP_COMPETENCE to R_EAE_USR;

create table EAE_FDP_COMPETENCE
(
   ID_EAE_FDP_COMPETENCE INTEGER not null,
   ID_EAE_FICHE_POSTE INTEGER not null,
   TYPE_COMPETENCE VARCHAR2(2),
   LIBELLE_COMPETENCE VARCHAR2(255),
   VERSION NUMBER DEFAULT 0 not null,
   constraint PK_EAE_FDP_COMPETENCE
   primary key (ID_EAE_FDP_COMPETENCE),
   constraint FK_EAE_FDP_COMPETENCE
         foreign key (ID_EAE_FICHE_POSTE)
         references EAE_FICHE_POSTE(ID_EAE_FICHE_POSTE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_FDP_COMPETENCE for EAE_FDP_COMPETENCE;
grant select, insert, update, delete on EAE_FDP_COMPETENCE to R_EAE_USR;
grant select on EAE_FDP_COMPETENCE to R_EAE_READ;

----------------------------------------------------------------
-- EAE_COMMENTAIRE
----------------------------------------------------------------
create sequence EAE_S_COMMENTAIRE 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_S_COMMENTAIRE for EAE_S_COMMENTAIRE;
grant select on EAE_S_COMMENTAIRE to R_EAE_USR;

create table EAE_COMMENTAIRE
(
   ID_EAE_COMMENTAIRE INTEGER not null,
   TEXT CLOB,
   VERSION NUMBER DEFAULT 0 not null,
   constraint PK_EAE_COMMENTAIRE
   primary key (ID_EAE_COMMENTAIRE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_COMMENTAIRE for EAE_COMMENTAIRE;
grant select, insert, update, delete on EAE_COMMENTAIRE to R_EAE_USR;
grant select on EAE_COMMENTAIRE to R_EAE_READ;
