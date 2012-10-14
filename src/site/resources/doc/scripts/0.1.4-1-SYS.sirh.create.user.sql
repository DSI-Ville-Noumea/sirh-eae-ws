-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en SYSTEM ou SYS
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

create role R_SIRH_WS_RO;
grant connect, create session to R_SIRH_WS_RO;

-- remplacer avec un vrai mot de passe
create user SIRH_WS_RO identified by &PASSWORD_SIRH_WS_RO;

grant R_SIRH_WS_RO to SIRH_WS_RO;
grant R_SIRH_USR to SIRH_USR;


alter user SIRH_WS_RO default tablespace TS_DEFAULT;

