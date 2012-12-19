-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en SYSTEM ou SYS
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

create role R_EAE_JOBS_USR;
grant connect, create session to R_EAE_JOBS_USR;

-- remplacer avec un vrai mot de passe
create user EAE_JOBS_USR identified by PASSWORD_SECRET_SIE;

grant R_EAE_JOBS_USR to EAE_JOBS_USR;

alter user EAE_JOBS_USR default tablespace TS_EAE_DEFAULT;