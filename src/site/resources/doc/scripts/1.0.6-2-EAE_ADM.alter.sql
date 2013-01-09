----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

ALTER TABLE EAE_RESULTAT modify (OBJECTIF varchar2(1000 CHAR));
ALTER TABLE EAE_RESULTAT modify (RESULTAT varchar2(1000 CHAR));

ALTER TABLE EAE_PLAN_ACTION modify (OBJECTIF varchar2(1000 CHAR));
ALTER TABLE EAE_PLAN_ACTION modify (MESURE varchar2(1000 CHAR));

ALTER TABLE EAE_DEVELOPPEMENT modify (LIBELLE varchar2(300 CHAR));
