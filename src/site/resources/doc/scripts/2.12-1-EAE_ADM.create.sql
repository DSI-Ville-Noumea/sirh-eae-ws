--------------------------------------------------------
--  Fichier créé - 20/03/2014
--------------------------------------------------------

----------------------------------------------------------------
-- EAE_CAMPAGNE_TASK
----------------------------------------------------------------
create sequence EAE_S_CAMPAGNE_TASK 
start with 1 
increment by 1 
nomaxvalue;

create public synonym EAE_S_CAMPAGNE_TASK for EAE_S_CAMPAGNE_TASK;
grant select on EAE_S_CAMPAGNE_TASK to R_EAE_USR;

CREATE TABLE EAE_CAMPAGNE_TASK
(	"ID_CAMPAGNE_TASK" INTEGER not null,
	"ID_CAMPAGNE_EAE" INTEGER not null, 
	"ANNEE" INTEGER not null, 
	"ID_AGENT" INTEGER not null, 
	"DATE_CALCUL_EAE" DATE not null, 
	"TASK_STATUS" varchar2(255),
   constraint PK_CAMPAGNE_TASK
		 primary key (ID_CAMPAGNE_TASK),
   constraint FK_CAMPAGNE_TASK
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
) 
TABLESPACE TS_SIRHR_DATA;

create public synonym EAE_CAMPAGNE_TASK for EAE_CAMPAGNE_TASK;
grant select, insert, update, delete on EAE_CAMPAGNE_TASK to R_EAE_USR;
grant select on EAE_CAMPAGNE_TASK to R_EAE_READ;
