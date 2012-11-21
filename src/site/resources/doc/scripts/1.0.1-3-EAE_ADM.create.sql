----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

----------------------------------------------------------------
-- EAE_FINALISATION
----------------------------------------------------------------
create sequence EAE_S_FINALISATION 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_S_FINALISATION for EAE_S_FINALISATION;
grant select on EAE_S_FINALISATION to R_EAE_USR;

create table EAE_FINALISATION
(
   ID_EAE_FINALISATION INTEGER not null,
   ID_EAE INTEGER not null,
   DATE_FINALISATION DATE not null,
   ID_AGENT INTEGER not null,
   ID_GED_DOCUMENT varchar2(50),
   VERSION_GED_DOCUMENT varchar2(50),
   ID_EAE_COMMENTAIRE INTEGER,
   VERSION NUMBER DEFAULT 0 not null,
   constraint PK_EAE_FINALISATION
		 primary key (ID_EAE_FINALISATION),
   constraint FK_EAE_FINALISATION
         foreign key (ID_EAE)
         references EAE(ID_EAE),
   constraint FK_COMMENTAIRE_FINALISATION
         foreign key (ID_EAE_COMMENTAIRE)
         references EAE_COMMENTAIRE(ID_EAE_COMMENTAIRE)
)
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_FINALISATION for EAE_FINALISATION;
grant select, insert, update, delete on EAE_FINALISATION to R_EAE_USR;
grant select on EAE_FINALISATION to R_EAE_READ;
