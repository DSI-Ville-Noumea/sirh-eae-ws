-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en SYSTEM ou SYS
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

create role R_SIRH_USR;
grant connect, create session to R_SIRH_USR;

-- remplacer avec un vrai mot de passe
create user SIRH_USR identified by PASSWORD_SECRET_SIE;
alter user SIRH_USR default tablespace TS_DEFAULT;




