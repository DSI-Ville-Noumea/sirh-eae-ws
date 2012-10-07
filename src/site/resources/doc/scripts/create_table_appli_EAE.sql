--==============================================================
-- Table: EAE_CAMPAGNE_EAE
--==============================================================
create sequence ID_CAMPAGNE_EAE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_CAMPAGNE_EAE
(
   ID_CAMPAGNE_EAE INTEGER not null,
   ANNEE INTEGER not null,
   DATE_DEBUT DATE,
   DATE_FIN DATE,
   DATE_OUVERTURE_KIOSQUE DATE,
   DATE_FERMETURE_KIOSQUE DATE,
   COMMENTAIRE VARCHAR2(255),
   constraint PK_CAMPAGNE_EAE
   primary key (ID_CAMPAGNE_EAE)
);

--==============================================================
-- Table: EAE_CAMPAGNE_ACTION
--==============================================================
create sequence ID_CAMPAGNE_ACTION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_CAMPAGNE_ACTION
(
   ID_CAMPAGNE_ACTION INTEGER ,
   ID_CAMPAGNE_EAE INTEGER not null,
   NOM_ACTION VARCHAR2(50) not null,
   MESSAGE VARCHAR2(100) not null,
   DATE_TRANSMISSION DATE not null,
   DIFFUSE INTEGER not null,
   DATE_A_FAIRE_LE DATE,
   DATE_FAIT_LE DATE ,
   COMMENTAIRE VARCHAR2(255),
   ID_AGENT_REALISATION INTEGER not null,
   constraint PK_CAMPAGNE_ACTION
   primary key (ID_CAMPAGNE_ACTION),
   constraint FK_CAMPAGNE_EAE
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
);
--==============================================================
-- Table: EAE_CAMPAGNE_ACTEURS
--==============================================================
create sequence EAE_ID_CAMPAGNE_ACTEURS_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_CAMPAGNE_ACTEURS
(
   ID_CAMPAGNE_ACTEURS INTEGER not null ,
   ID_CAMPAGNE_ACTION INTEGER not null,
   ID_AGENT INTEGER not null,
   constraint PK_CAMPAGNE_ACTEURS
   primary key (ID_CAMPAGNE_ACTEURS),
   constraint FK_CAMPAGNE_ACTION
         foreign key (ID_CAMPAGNE_ACTION)
         references EAE_CAMPAGNE_ACTION(ID_CAMPAGNE_ACTION)
);
--==============================================================
-- Table: EAE
--==============================================================
create sequence EAE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE
(
   ID_EAE INTEGER not null,
   ID_CAMPAGNE_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   ID_SHD INTEGER,
   DIRECTION_SERVICE VARCHAR2(10),
   SECTION_SERVICE VARCHAR2(10),
   SERVICE VARCHAR2(10),
   STATUT VARCHAR2(10),
   ETAT VARCHAR2(2) not null,
   CAP INTEGER not null,
   DOC_ATTACHE INTEGER not null,
   DATE_CREATION DATE ,
   DATE_FIN DATE,   
   DATE_ENTRETIEN DATE,
   DUREE_ENTRETIEN INTEGER ,
   DATE_FINALISE DATE,
   DATE_CONTROLE DATE ,
   HEURE_CONTROLE VARCHAR2(5) ,
   USER_CONTROLE VARCHAR2(7),
   ID_DELEGATAIRE INTEGER ,
   constraint PK_EAE
   primary key (ID_EAE),
   constraint FK_EAE_CAMPAGNE_EAE
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
);
CREATE UNIQUE INDEX EAE_index
   ON EAE (ID_CAMPAGNE_EAE,ID_AGENT);
--==============================================================
-- Table: EAE_EVALUATEUR
--==============================================================
create sequence EAE_EVALUATEUR_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_EVALUATEUR
(
   ID_EAE_EVALUATEUR INTEGER not null,
   ID_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   FONCTION VARCHAR2(100),
   DATE_ENTREE_SERVICE DATE,
   DATE_ENTREE_COLLECTIVITE DATE,
   DATE_ENTREE_FONCTION DATE,
   VERSION NUMBER,
   constraint PK_EAE_EVALUATEUR
   primary key (ID_EAE_EVALUATEUR),
   constraint FK_EAE_EVALUATEUR
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX EAE_EVALUATEUR_index
   ON EAE_EVALUATEUR(ID_EAE,ID_AGENT);
--==============================================================
-- Table: EAE_EVALUE
--==============================================================
create sequence EAE_EVALUE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_EVALUE
(
   ID_EAE_EVALUE INTEGER not null,
   ID_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   DATE_ENTREE_SERVICE DATE,
   DATE_ENTREE_COLLECTIVITE DATE,
   DATE_ENTREE_FONCTIONNAIRE DATE,
   DATE_ENTREE_ADMINISTRATION DATE,
   STATUT VARCHAR2(10),
   ANCIENNETE_ECHELON INTEGER,
   CADRE VARCHAR2(30),
   CATEGORIE VARCHAR2(1),
   CLASSIFICATION VARCHAR2(50),
   GRADE VARCHAR2(50),
   ECHELON VARCHAR2(60),
   DATE_EFFET_AVCT DATE,
   NOUV_GRADE VARCHAR2(50),
   NOUV_ECHELON VARCHAR2(60),
   POSITION VARCHAR2(50),
   TYPE_AVCT INTEGER ,
   constraint PK_EAE_EVALUE
   primary key (ID_EAE_EVALUE),
   constraint FK_EAE_EVALUE
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX EAE_EVALUE_index
   ON EAE_EVALUE (ID_EAE,ID_AGENT);
--==============================================================
-- Table: EAE_FICHE_POSTE
--==============================================================
create sequence EAE_FICHE_POSTE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_FICHE_POSTE
(
   ID_EAE_FICHE_POSTE INTEGER not null,
   ID_EAE INTEGER not null,
   ID_AGENT INTEGER not null,
   TYPE_FDP VARCHAR2(60),
   DIRECTION_SERV VARCHAR2(60),
   SERVICE_SERV VARCHAR2(60),
   SECTION_SERV VARCHAR2(60),
   EMPLOI VARCHAR2(100),
   FONCTION VARCHAR2(100),
   DATE_ENTREE_FONCTION DATE,
   GRADE_POSTE VARCHAR2(50),
   LOCALISATION VARCHAR2(60),
   MISSIONS CLOB,
   FONCTION_RESP VARCHAR2(100),
   NOM_RESP VARCHAR2(100),
   PRENOM_RESP VARCHAR2(100),
   constraint PK_EAE_FICHE_POSTE
   primary key (ID_EAE_FICHE_POSTE),
   constraint FK_EAE_FICHE_POSTE
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX EAE_FICHE_POSTE_index
   ON EAE_FICHE_POSTE (ID_EAE,ID_AGENT,TYPE_FDP);
--==============================================================
-- Table: EAE_FDP_ACTIVITES
--==============================================================
create sequence EAE_FDP_ACTIVITES_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_FDP_ACTIVITES
(
   ID_EAE_FDP_ACTIVITES INTEGER not null,
   ID_EAE_FICHE_POSTE INTEGER not null,
   TYPE_ACTIVITE VARCHAR2(100),
   LIBELLE_ACTIVITE VARCHAR2(255),
   constraint PK_EAE_FDP_ACTIVITES
   primary key (ID_EAE_FDP_ACTIVITES),
   constraint FK_EAE_FDP_ACTIVITES
         foreign key (ID_EAE_FICHE_POSTE)
         references EAE_FICHE_POSTE(ID_EAE_FICHE_POSTE)
);
CREATE UNIQUE INDEX EAE_FDP_ACTIVITES_index
   ON EAE_FDP_ACTIVITES (ID_EAE_FICHE_POSTE,TYPE_ACTIVITE,LIBELLE_ACTIVITE);
--==============================================================
-- Table: EAE_DIPLOME
--==============================================================
create sequence EAE_DIPLOME_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_DIPLOME
(
   ID_EAE_DIPLOME INTEGER not null,
   ID_EAE INTEGER not null,
   LIBELLE_DIPLOME VARCHAR2(255),
   constraint PK_EAE_DIPLOME
   primary key (ID_EAE_DIPLOME),
   constraint FK_EAE_DIPLOME
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX EAE_DIPLOME_index
   ON EAE_DIPLOME (id_eae,LIBELLE_DIPLOME);
--==============================================================
-- Table: EAE_PARCOURS_PRO
--==============================================================
create sequence EAE_PARCOURS_PRO_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_PARCOURS_PRO
(
   ID_EAE_PARCOURS_PRO INTEGER not null,
   ID_EAE INTEGER not null,
   DATE_DEBUT DATE,
   DATE_FIN DATE,
   LIBELLE_PARCOURS_PRO VARCHAR2(255),
   constraint PK_EAE_PARCOURS_PRO
   primary key (ID_EAE_PARCOURS_PRO),
   constraint FK_EAE_PARCOURS_PRO
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX eae_parcours_pro_index
   ON eae_parcours_pro (id_eae,date_debut);

--==============================================================
-- Table: EAE_FORMATION
--==============================================================
create sequence EAE_FORMATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_FORMATION
(
   ID_EAE_FORMATION INTEGER not null,
   ID_EAE INTEGER not null,
   ANNEE_FORMATION Integer not null,
   DUREE_FORMATION Integer not null,
   LIBELLE_FORMATION VARCHAR2(255),
   constraint PK_EAE_FORMATION
   primary key (ID_EAE_FORMATION),
   constraint FK_EAE_FORMATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX eae_FORMATION_index
   ON eae_FORMATION (id_eae,LIBELLE_FORMATION);

--==============================================================
-- Table: EAE_TYPE_RESULTAT
--==============================================================
create sequence EAE_TYPE_RESULTAT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_TYPE_RESULTAT
(
   ID_EAE_TYPE_RESULTAT INTEGER not null,
   LIBELLE_TYPE_RESULTAT VARCHAR2(50),
   constraint PK_EAE_TYPE_RESULTAT
   primary key (ID_EAE_TYPE_RESULTAT)
);

--==============================================================
-- Table: EAE_RESULTAT
--==============================================================
create sequence EAE_RESULTAT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_RESULTAT
(
   ID_EAE_RESULTAT INTEGER not null,
   ID_EAE INTEGER not null,
   ID_TYPE_RESULTAT INTEGER not null,
   OBJECTIF VARCHAR2(255),
   DETAIL_OBJECTIF VARCHAR2(255),
   RESULTAT VARCHAR2(255),
   COMMENTAIRE VARCHAR2(255),
   constraint PK_EAE_RESULTAT
   primary key (ID_EAE_RESULTAT),
   constraint FK_EAE_TYPE_RESULTAT
         foreign key (ID_TYPE_RESULTAT)
         references EAE_TYPE_RESULTAT(ID_EAE_TYPE_RESULTAT),
   constraint FK_EAE_RESULTAT
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX eae_RESULTAT_index
   ON eae_RESULTAT (id_eae,ID_TYPE_RESULTAT,OBJECTIF);

--==============================================================
-- Table: EAE_PLAN_ACTION
--==============================================================
create sequence EAE_PLAN_ACTION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_PLAN_ACTION
(
   ID_EAE_PLAN_ACTION INTEGER not null,
   ID_EAE INTEGER not null,
   ID_TYPE_RESULTAT INTEGER not null,
   OBJECTIF VARCHAR2(255),
   MESURE VARCHAR2(255),
   constraint PK_EAE_PLAN_ACTION
   primary key (ID_EAE_PLAN_ACTION),
   constraint FK_EAE_TYPE_RES_PLAN_ACTION
         foreign key (ID_TYPE_RESULTAT)
         references EAE_TYPE_RESULTAT(ID_EAE_TYPE_RESULTAT),
   constraint FK_EAE_PLAN_ACTION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX EAE_PLAN_ACTION_index
   ON EAE_PLAN_ACTION (id_eae,ID_TYPE_RESULTAT,OBJECTIF);

--==============================================================
-- Table: EAE_NIVEAU_EAE
--==============================================================
create sequence EAE_NIVEAU_EAE_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_NIVEAU_EAE
(
   ID_EAE_NIVEAU_EAE INTEGER not null,
   LIBELLE_NIVEAU_EAE VARCHAR2(50),
   constraint PK_EAE_NIVEAU_EAE
   primary key (ID_EAE_NIVEAU_EAE)
);

--==============================================================
-- Table: EAE_EVALUATION
--==============================================================
create sequence EAE_EVALUATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_EVALUATION
(
   ID_EAE_EVALUATION INTEGER not null,
   ID_EAE INTEGER not null,
   ID_EAE_NIVEAU INTEGER,
   NOTE_ANNEE DECIMAL(4,2),
   NOTE_ANNEE_N1 DECIMAL(4,2),
   NOTE_ANNEE_N2 DECIMAL(4,2),
   NOTE_ANNEE_N3 DECIMAL(4,2),
   AVIS VARCHAR2(50),
   AVANCEMENT_DIFF VARCHAR2(50),
   CHANGEMENT_CLASSE VARCHAR2(50),
   AVIS_SHD VARCHAR2(50),
   VERSION NUMBER,
   constraint PK_EAE_EVALUATION
   primary key (ID_EAE_EVALUATION),
   constraint FK_EAE_NIVEAU_EAE
         foreign key (ID_EAE_NIVEAU)
         references EAE_NIVEAU_EAE(ID_EAE_NIVEAU_EAE),
   constraint FK_EAE_EVALUATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);

--==============================================================
-- Table: EAE_DOCUMENT
--==============================================================
create sequence EAE_DOCUMENT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_DOCUMENT
(
   ID_EAE_DOCUMENT INTEGER not null,
   ID_CAMPAGNE_EAE INTEGER not null,
   ID_CAMPAGNE_ACTION INTEGER,
   ID_DOCUMENT INTEGER not null,
   TYPE_DOCUMENT VARCHAR2(30),
   constraint PK_EAE_DOCUMENT
   primary key (ID_EAE_DOCUMENT),
   constraint FK_EAE_DOCUMENT
         foreign key (ID_CAMPAGNE_EAE)
         references EAE_CAMPAGNE_EAE(ID_CAMPAGNE_EAE)
);

--==============================================================
-- Table: EAE_APPRECIATION
--==============================================================
create sequence EAE_APPRECIATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_APPRECIATION
(
   ID_EAE_APPRECIATION INTEGER not null,
   ID_EAE INTEGER not null,
   NUMERO VARCHAR2(50),
   NOTE_EVALUE VARCHAR2(1),
   NOTE_EVALUATEUR VARCHAR2(1),
   constraint PK_EAE_APPRECIATION
   primary key (ID_EAE_APPRECIATION),
   constraint FK_EAE_APPRECIATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);
CREATE UNIQUE INDEX EAE_APPRECIATION_index
   ON EAE_APPRECIATION (id_eae,NUMERO);

--==============================================================
-- Table: EAE_AUTO_EVALUATION
--==============================================================
create sequence EAE_AUTO_EVALUATION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_AUTO_EVALUATION
(
   ID_EAE_AUTO_EVALUATION INTEGER not null,
   ID_EAE INTEGER not null,
   ELEMENT VARCHAR2(255),
   OBJECTIF_ATTEINT VARCHAR2(255),
   COMPETENCES_ACQUISES VARCHAR2(255),
   SUCCES VARCHAR2(255),
   constraint PK_EAE_AUTO_EVALUATION
   primary key (ID_EAE_AUTO_EVALUATION),
   constraint FK_EAE_AUTO_EVALUATION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);

--==============================================================
-- Table: EAE_EVOLUTION
--==============================================================
create sequence EAE_EVOLUTION_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_EVOLUTION
(
   ID_EAE_EVOLUTION INTEGER not null,
   ID_EAE INTEGER not null,
   MOBILIE_GEO INTEGER not null,
   MOBILIE_FONCT INTEGER not null,
   CHANGEMENT_METIER INTEGER not null,
   DELAI_ENVISAGE VARCHAR2(50),
   MOBILITE_SERVICE INTEGER not null,
   MOBILITE_DIRECTION INTEGER not null,
   MOBILITE_COLLECTIVITE INTEGER not null,
   MOBILITE_AUTRE INTEGER not null,
   NOM_ENTITE VARCHAR2(100),
   CONCOURS INTEGER not null,
   NOM_CONCOURS VARCHAR2(100),
   DEMANDE_VAE INTEGER not null,
   NOM_DIPLOME VARCHAR2(100),
   TEMPS_PARTIEL INTEGER not null,
   POURC_TEMPS_PARTIEL INTEGER,
   RETRAITE INTEGER not null,
   DATE_RETRAITE DATE,
   AUTRE_PERSPECTIVE INTEGER not null,
   PERSPECTIVE VARCHAR2(255),
   COMMENTAIRE VARCHAR2(255),
   constraint PK_EAE_EVOLUTION
   primary key (ID_EAE_EVOLUTION),
   constraint FK_EAE_EVOLUTION
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);

--==============================================================
-- Table: EAE_EVOL_SOUHAIT
--==============================================================
create sequence EAE_EVOL_SOUHAIT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_EVOL_SOUHAIT
(
   ID_EAE_EVOL_SOUHAIT INTEGER not null,
   ID_EAE INTEGER not null,
   TYPE_SOUHAIT VARCHAR2(50),
   LIB_SOUHAIT VARCHAR2(255),
   constraint PK_EAE_EVOL_SOUHAIT
   primary key (ID_EAE_EVOL_SOUHAIT),
   constraint FK_EAE_EVOL_SOUHAIT
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);

--==============================================================
-- Table: EAE_TYPE_DEVELOPPEMENT
--==============================================================
create sequence EAE_TYPE_DEVELOPPEMENT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_TYPE_DEVELOPPEMENT
(
   ID_EAE_TYPE_DEVELOPPEMENT INTEGER not null,
   LIBELLE_TYPE_DEVELOPPEMENT VARCHAR2(50),
   constraint PK_EAE_TYPE_DEVELOPPEMENT
   primary key (ID_EAE_TYPE_DEVELOPPEMENT)
);

--==============================================================
-- Table: EAE_DEVELOPPEMENT
--==============================================================
create sequence EAE_DEVELOPPEMENT_SEQ 
start with 1 
increment by 1 
nomaxvalue; 
create table EAE_DEVELOPPEMENT
(
   ID_EAE_DEVELOPPEMENT INTEGER not null,
   ID_EAE INTEGER not null,
   ID_TYPE_DEVELOPPEMENT INTEGER not null,
   LIBELLE_DEVELOPPEMENT VARCHAR2(255),
   ECHEANCE_DEVELOPPEMENT VARCHAR2(50),
   PRIORISATION_DEVELOPPEMENT INTEGER ,
   constraint PK_EAE_DEVELOPPEMENT
   primary key (ID_EAE_DEVELOPPEMENT),
   constraint FK_EAE_TYPE_DEVELOPPEMENT
         foreign key (ID_TYPE_DEVELOPPEMENT)
         references EAE_TYPE_DEVELOPPEMENT(ID_EAE_TYPE_DEVELOPPEMENT),
   constraint FK_EAE_DEVELOPPEMENT
         foreign key (ID_EAE)
         references EAE(ID_EAE)
);