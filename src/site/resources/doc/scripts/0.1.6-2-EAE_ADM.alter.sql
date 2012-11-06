-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
-- connecte en EAE_ADM
-- !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

alter table EAE_EVALUATION add (ID_EAE_COM_EVALUATEUR NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_EVALUATEUR FOREIGN KEY (ID_EAE_COM_EVALUATEUR) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION add (ID_EAE_COM_EVALUE NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_EVALUE FOREIGN KEY (ID_EAE_COM_EVALUE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION add (ID_EAE_COM_AVCT_EVALUATEUR NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_AVCT_EVALUATEUR FOREIGN KEY (ID_EAE_COM_AVCT_EVALUATEUR) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION add (ID_EAE_COM_AVCT_EVALUE NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_AVCT_EVALUE FOREIGN KEY (ID_EAE_COM_AVCT_EVALUE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION modify (AVANCEMENT_DIFF varchar2(4));
alter table EAE_EVALUATION modify (AVIS_SHD varchar2(12));
alter table EAE_EVALUATION rename column AVIS to AVIS_REVALORISATION;
alter table EAE_EVALUATION rename column CHANGEMENT_CLASSE to AVIS_CHANGEMENT_CLASSE;
alter table EAE_EVALUATION rename column AVANCEMENT_DIFF to PROPOSITION_AVANCEMENT;
alter table EAE_EVALUATION modify (AVIS_CHANGEMENT_CLASSE NUMBER(1,0));
alter table EAE_EVALUATION modify (AVIS_REVALORISATION NUMBER(1,0));

update EAE_EVALUE set TYPE_AVCT = null;
alter table EAE_EVALUE modify (TYPE_AVCT varchar2(5));

alter table EAE_AUTO_EVALUATION drop column OBJECTIF_ATTEINT;
alter table EAE_AUTO_EVALUATION drop column ELEMENTS;
alter table EAE_AUTO_EVALUATION drop column COMPETENCES_ACQUISES;
alter table EAE_AUTO_EVALUATION drop column SUCCES;
alter table EAE_AUTO_EVALUATION add (PARTICULARITES CLOB);
alter table EAE_AUTO_EVALUATION add (PARTICULARITES CLOB);
alter table EAE_AUTO_EVALUATION add (ACQUIS CLOB);
alter table EAE_AUTO_EVALUATION add (SUCCES_DIFFICULTES CLOB);
alter table EAE_AUTO_EVALUATION add (VERSION number default 0 not null);

drop index EAE_PLAN_ACTION_index;

-- 

alter table EAE_EVOL_SOUHAIT drop column LIB_SOUHAIT;
alter table EAE_EVOL_SOUHAIT drop column TYPE_SOUHAIT;
alter table EAE_EVOL_SOUHAIT drop column ID_EAE;
alter table EAE_EVOL_SOUHAIT add (ID_EAE_EVOLUTION number(*,0) not null);
alter table EAE_EVOL_SOUHAIT add CONSTRAINT FK_EAE_EVOL_SOUHAIT FOREIGN KEY (ID_EAE_EVOLUTION) REFERENCES EAE_EVOLUTION (ID_EAE_EVOLUTION);
alter table EAE_EVOL_SOUHAIT add (LIB_SOUHAIT CLOB);
alter table EAE_EVOL_SOUHAIT add (LIB_SUGGESTION CLOB);
alter table EAE_EVOL_SOUHAIT add (VERSION number default 0 not null);

alter table EAE_DEVELOPPEMENT rename column LIBELLE_DEVELOPPEMENT to LIBELLE;
alter table EAE_DEVELOPPEMENT rename column ECHEANCE_DEVELOPPEMENT to ECHEANCE;
alter table EAE_DEVELOPPEMENT rename column PRIORISATION_DEVELOPPEMENT to PRIORISATION;
alter table EAE_DEVELOPPEMENT drop column ID_EAE;
alter table EAE_DEVELOPPEMENT add (ID_EAE_EVOLUTION number(*,0) not null);
alter table EAE_DEVELOPPEMENT add CONSTRAINT FK_EAE_DEVELOP FOREIGN KEY (ID_EAE_EVOLUTION) REFERENCES EAE_EVOLUTION (ID_EAE_EVOLUTION);
alter table EAE_DEVELOPPEMENT add (TYPE_DEVELOPPEMENT varchar2(15) not null);
alter table EAE_DEVELOPPEMENT drop column ID_TYPE_DEVELOPPEMENT;
alter table EAE_DEVELOPPEMENT modify (ECHEANCE DATE);
alter table EAE_DEVELOPPEMENT add (VERSION number default 0 not null);

alter table EAE_EVOLUTION modify (MOBILIE_GEO NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (MOBILIE_FONCT NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (CHANGEMENT_METIER NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (MOBILITE_SERVICE NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (MOBILITE_DIRECTION NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (MOBILITE_COLLECTIVITE NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (MOBILITE_AUTRE NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (CONCOURS NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (DEMANDE_VAE NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (TEMPS_PARTIEL NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (RETRAITE NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (AUTRE_PERSPECTIVE NUMBER(1,0) default 0);
alter table EAE_EVOLUTION modify (DELAI_ENVISAGE varchar2(15));
alter table EAE_EVOLUTION modify (POURC_TEMPS_PARTIEL default 0 not null);
alter table EAE_EVOLUTION rename column DEMANDE_VAE to VAE;
alter table EAE_EVOLUTION rename column NOM_DIPLOME to NOM_VAE;
alter table EAE_EVOLUTION rename column NOM_ENTITE to NOM_COLLECTIVITE;
alter table EAE_EVOLUTION rename column PERSPECTIVE to LIB_AUTRE_PERSPECTIVE;
alter table EAE_EVOLUTION drop column COMMENTAIRE;
alter table EAE_EVOLUTION add (ID_EAE_COM_EVOLUTION number(*,0));
alter table EAE_EVOLUTION add CONSTRAINT FK_EAE_COM_EVOLUTION FOREIGN KEY (ID_EAE_COM_EVOLUTION) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVOLUTION add (ID_EAE_COM_EVALUATEUR number(*,0));
alter table EAE_EVOLUTION add CONSTRAINT FK_EAE_COM_EVOL_EVALUATEUR FOREIGN KEY (ID_EAE_COM_EVALUATEUR) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVOLUTION add (ID_EAE_COM_EVALUE number(*,0));
alter table EAE_EVOLUTION add CONSTRAINT FK_EAE_COM_EVOL_EVALUE FOREIGN KEY (ID_EAE_COM_EVALUE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVOLUTION add (VERSION number default 0 not null);
