-- Connecte en EAE_ADM
alter table EAE_EVALUE add (VERSION number default 0 not null);
alter table EAE add (VERSION number default 0 not null);
alter table EAE drop column DIRECTION_SERVICE;
alter table EAE drop column SERVICE;
alter table EAE drop column SECTION_SERVICE;
alter table EAE drop column ID_SHD;
alter table EAE_DIPLOME add (VERSION number default 0 not null);
alter table EAE_FICHE_POSTE drop column ID_AGENT;
alter table EAE_FICHE_POSTE drop column NOM_RESP;
alter table EAE_FICHE_POSTE drop column PRENOM_RESP;
alter table EAE_FICHE_POSTE add (ID_SHD number);
alter table EAE_FICHE_POSTE add (VERSION number default 0 not null);
alter table EAE_FICHE_POSTE rename column DIRECTION_SERV to DIRECTION_SERVICE;
alter table EAE_FICHE_POSTE rename column SERVICE_SERV to SERVICE;
alter table EAE_FICHE_POSTE rename column SECTION_SERV to SECTION_SERVICE;
alter table EAE_FDP_ACTIVITE rename column ID_EAE_FDP_ACTIVITES to ID_EAE_FDP_ACTIVITE;
alter table EAE_FDP_ACTIVITE add (VERSION number default 0 not null);
alter table EAE_PARCOURS_PRO add (VERSION number default 0 not null);
alter table EAE_FORMATION add (VERSION number default 0 not null);
alter table EAE_TYPE_OBJECTIF rename column ID_EAE_TYPE_RESULTAT to ID_EAE_TYPE_OBJECTIF;
alter table EAE_TYPE_OBJECTIF rename column LIBELLE_TYPE_RESULTAT to LIBELLE_TYPE_OBJECTIF;
alter table EAE_RESULTAT add (VERSION number default 0 not null);
alter table EAE_RESULTAT rename column ID_TYPE_RESULTAT to ID_EAE_TYPE_OBJECTIF;
alter table EAE_NIVEAU rename column ID_EAE_NIVEAU_EAE to ID_EAE_NIVEAU;
alter table EAE_PLAN_ACTION rename column ID_TYPE_RESULTAT to ID_EAE_TYPE_OBJECTIF;

-- Verifier les objets invalides