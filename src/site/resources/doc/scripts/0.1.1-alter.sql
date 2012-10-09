-- Connecte en EAE_ADM
alter table EAE_EVALUE add (VERSION number);
alter table EAE add (VERSION number);
alter table EAE drop column DIRECTION_SERVICE;
alter table EAE drop column SERVICE;
alter table EAE drop column SECTION_SERVICE;
alter table EAE drop column ID_SHD;
alter table EAE_DIPLOME add (VERSION number);
alter table EAE_FICHE_POSTE drop column ID_AGENT;
alter table EAE_FICHE_POSTE drop column NOM_RESP;
alter table EAE_FICHE_POSTE drop column PRENOM_RESP;
alter table EAE_FICHE_POSTE add (ID_SHD number);
alter table EAE_FICHE_POSTE add (VERSION number);
alter table EAE_FICHE_POSTE rename column DIRECTION_SERV to DIRECTION_SERVICE;
alter table EAE_FICHE_POSTE rename column SERVICE_SERV to SERVICE;
alter table EAE_FICHE_POSTE rename column SECTION_SERV to SECTION_SERVICE;
alter table EAE_FDP_ACTIVITE rename column ID_EAE_FDP_ACTIVITES to ID_EAE_FDP_ACTIVITE;
alter table EAE_FDP_ACTIVITE add (VERSION number);
alter table EAE_PARCOURS_PRO add (VERSION number);
alter table EAE_FORMATION add (VERSION number);
alter table EAE_RESULTAT add (VERSION number);
alter table EAE_RESULTAT rename column ID_TYPE_RESULTAT to ID_EAE_TYPE_RESULTAT;
alter table EAE_NIVEAU rename column ID_EAE_NIVEAU_EAE to ID_EAE_NIVEAU;

-- Verifier les objets invalides