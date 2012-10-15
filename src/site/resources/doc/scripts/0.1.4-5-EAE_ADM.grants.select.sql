-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en EAE_ADM
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
sho user
grant select on EAE to R_SIRH_WS_RO;

grant select on EAE_S_EAE to R_SIRH_WS_RO;
grant select on EAE_S_CAMPAGNE_EAE to R_SIRH_WS_RO;
grant select on EAE_S_EVALUATEUR to R_SIRH_WS_RO;
grant select on EAE_S_EVALUATION to R_SIRH_WS_RO;
grant select on EAE_S_FICHE_POSTE to R_SIRH_WS_RO;
