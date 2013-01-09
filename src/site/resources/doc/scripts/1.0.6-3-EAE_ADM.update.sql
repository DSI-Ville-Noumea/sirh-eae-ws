----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

-- SCRIPT POUR QUALIFICATION

UPDATE EAE_ADM.EAE_EVALUATION SET NOTE_ANNEE_N2 = 17.75 WHERE ID_EAE_EVALUATION = 505;
UPDATE EAE_ADM.eae SET cap = 0 WHERE id_eae in(select e.id_eae from eae e inner join eae_evalue ev on e.id_eae=ev.id_eae where ev.statut != 'F' and ev.statut is not null);
COMMIT;


-- SCRIPT POUR PRODUCTION

UPDATE EAE_ADM.EAE_EVALUATION SET NOTE_ANNEE_N3 = 17.75 WHERE ID_EAE_EVALUATION = 640;
UPDATE EAE_ADM.EAE_EVALUATION SET NOTE_ANNEE_N2 = 17.75 WHERE ID_EAE_EVALUATION = 483;
UPDATE EAE_ADM.eae SET cap = 0 WHERE id_eae in(select e.id_eae from eae e inner join eae_evalue ev on e.id_eae=ev.id_eae where ev.statut != 'F' and ev.statut is not null);
COMMIT;
