-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en SYSTEM ou SYS
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

create role R_EAE_QRTZ_USR;
grant connect, create session to R_EAE_QRTZ_USR;
create user EAE_QRTZ_USR identified by PASSWORD_SECRET_SIE;
grant R_EAE_QRTZ_USR to EAE_QRTZ_USR;

alter user EAE_QRTZ_USR default tablespace TS_QRTZ_DEFAULT;