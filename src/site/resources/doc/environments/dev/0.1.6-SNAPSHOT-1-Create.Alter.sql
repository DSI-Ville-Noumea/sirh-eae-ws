create sequence EAE_S_FDP_COMPETENCE 
start with 1 
increment by 1 
nomaxvalue;

create table EAE_FDP_COMPETENCE
(
   ID_EAE_FDP_COMPETENCE INTEGER not null,
   ID_EAE_FICHE_POSTE INTEGER not null,
   TYPE_COMPETENCE VARCHAR2(2),
   LIBELLE_COMPETENCE VARCHAR2(255),
   VERSION NUMBER DEFAULT 0 not null,
   constraint PK_EAE_FDP_COMPETENCE
   primary key (ID_EAE_FDP_COMPETENCE),
   constraint FK_EAE_FDP_COMPETENCE
         foreign key (ID_EAE_FICHE_POSTE)
         references EAE_FICHE_POSTE(ID_EAE_FICHE_POSTE)
);


----------------------------------------------------------------
-- EAE_COMMENTAIRE
----------------------------------------------------------------
create sequence EAE_S_COMMENTAIRE 
start with 1 
increment by 1 
nomaxvalue;

create table EAE_COMMENTAIRE
(
   ID_EAE_COMMENTAIRE INTEGER not null,
   TEXT CLOB,
   VERSION NUMBER DEFAULT 0 not null,
   constraint PK_EAE_COMMENTAIRE
   primary key (ID_EAE_COMMENTAIRE)
);


----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

alter table EAE_EVALUE rename column ANCIENNETE_ECHELON to ANCIENNETE_ECHELON_JOURS;
alter table EAE_EVALUE add (STATUT_PRECISION VARCHAR2(50));
alter table EAE_EVALUE modify (STATUT VARCHAR2(2));
update EAE_EVALUE set POSITION = 'AC';
alter table EAE_EVALUE modify (POSITION VARCHAR2(2));
alter table EAE drop column STATUT;

alter table EAE_FICHE_POSTE add (PRIMAIRE number(1) default 0 not null check(PRIMAIRE in (0, 1)));
update EAE_FICHE_POSTE set PRIMAIRE = 1;
alter table EAE_FICHE_POSTE drop column TYPE_FDP;

alter table EAE_FDP_ACTIVITE drop column TYPE_ACTIVITE;

alter table EAE_RESULTAT drop column COMMENTAIRE;
alter table EAE_RESULTAT add (ID_EAE_COMMENTAIRE NUMBER(*,0));
alter table EAE_RESULTAT add CONSTRAINT FK_EAE_RESULT_EAE_COMMENT FOREIGN KEY (ID_EAE_COMMENTAIRE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);

alter table EAE drop column id_agent;
alter table EAE add (ID_EAE_COMMENTAIRE NUMBER(*,0));
alter table EAE add CONSTRAINT FK_EAE_EAE_COMMENTAIRE FOREIGN KEY (ID_EAE_COMMENTAIRE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);

update EAE_EVALUATION set VERSION = 0 where VERSION is null;
alter table EAE_EVALUATION modify (VERSION number default 0 not null);

alter table EAE_APPRECIATION add (TYPE_APPRECIATION varchar2(2) not null);
alter table EAE_APPRECIATION add (VERSION number default 0 not null);
alter table EAE_APPRECIATION modify (NUMERO number(2,0) not null);
alter table EAE_APPRECIATION add constraint EAE_UNIQUE_APPRECIATION unique(ID_EAE,NUMERO,TYPE_APPRECIATION) USING INDEX TABLESPACE TS_SIRHR_INDEX;
drop index EAE_APPRECIATION_index;


----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------
comment on table EAE_FDP_COMPETENCE is 'La liste des comp�tences requises pour une fiche de poste';
comment on table EAE_COMMENTAIRE is 'Commentaires sur les diff�rentes parties d''un EAE';
comment on table EAE_NIVEAU is 'Le r�f�rentiel des diff�rents niveaux de r�sultats';
comment on table EAE_FDP_ACTIVITE is 'La liste des activit�s principales li�es � une fiche de pose';
comment on table EAE_CAMPAGNE_EAE is 'SIRH: Campagne regroupant tous les EAEs pour une ann�e donn�e';
comment on table EAE_CAMPAGNE_ACTION is 'SIRH: Liste d''actions � entreprendre durant une campagne EAE';
comment on table EAE_CAMPAGNE_ACTEURS is 'SIRH: Liste des acteurs de la campagne EAE';
comment on table EAE is 'Informations g�n�rales sur l''EAE';
comment on table EAE_EVALUATEUR is 'Une personne ayant droit d''�valuer un agent';
comment on table EAE_EVALUE is 'L''agent �valu� dans le cadre de son EAE';
comment on table EAE_FICHE_POSTE is 'Fiche de poste d�crivant le travail et les comp�tences requises de l''agent actuellement';
comment on table EAE_DIPLOME is 'La liste des d�plomes de l''agent �valu�';
comment on table EAE_PARCOURS_PRO is 'La liste des exp�riences et parcours professionnel de l''agent �valu�';
comment on table EAE_FORMATION is 'La liste des formations auxquelles l''agent �valu� a particip�';
comment on table EAE_RESULTAT is 'Le r�sultat de l''�valuation des diff�rents objectifs de l''agent �valu�';
comment on table EAE_PLAN_ACTION is 'La liste des objectifs � r�aliser pour l''ann�e suivante. (Deviendront le r�sultat du prochain EAE)';
comment on table EAE_EVALUATION is 'L''�valuation de l''agent en regard de ses objectifs et performance';
comment on table EAE_DOCUMENT is 'SIRH: Liste des documents li�s � une campagne EAE';
comment on table EAE_APPRECIATION is 'La liste des appr�ciations d''un agent par son �valuateur et par lui m�me';
comment on table EAE_AUTO_EVALUATION is 'L''auto �valuation d''un agent';
comment on table EAE_EVOLUTION is 'L''�volution d''un agent dans son travail vu par l''agent';
comment on table EAE_EVOL_SOUHAIT is 'Les souhaits d''�volution d''un agent dans ses comp�tences et ses t�ches';
comment on table EAE_TYPE_DEVELOPPEMENT is 'Le r�f�rentiel des diff�rents type de d�veloppements de l''agent';
comment on table EAE_DEVELOPPEMENT is 'La liste des d�veloppements de l''agent planifi�s pour l''ann�e suivante';
comment on table EAE_TYPE_OBJECTIF is 'Le r�f�rentiel des types d''objectifs d''un agent';


----------------------------------------------------------------
-- Connecte en EAE_ADM
----------------------------------------------------------------

alter table EAE_EVALUATION add (ID_EAE_COM_EVALUATEUR NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_EVALUATEUR FOREIGN KEY (ID_EAE_COM_EVALUATEUR) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION add (ID_EAE_COM_EVALUE NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_EVALUE FOREIGN KEY (ID_EAE_COM_EVALUE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION add (ID_EAE_COM_AVCT_EVALUATEUR NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_AVCT_EVALUATEUR FOREIGN KEY (ID_EAE_COM_AVCT_EVALUATEUR) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION add (ID_EAE_COM_AVCT_EVALUE NUMBER(*,0));
alter table EAE_EVALUATION add CONSTRAINT FK_EAE_COM_AVCT_EVALUE FOREIGN KEY (ID_EAE_COM_AVCT_EVALUE) REFERENCES EAE_COMMENTAIRE (ID_EAE_COMMENTAIRE);
alter table EAE_EVALUATION modify (AVIS varchar2(12));
alter table EAE_EVALUATION modify (CHANGEMENT_CLASSE varchar2(12));
alter table EAE_EVALUATION modify (AVANCEMENT_DIFF varchar2(4));
alter table EAE_EVALUATION modify (AVIS_SHD varchar2(12));
alter table EAE_EVALUATION rename column AVIS to AVIS_REVALORISATION;
alter table EAE_EVALUATION rename column CHANGEMENT_CLASSE to AVIS_CHANGEMENT_CLASSE;
alter table EAE_EVALUATION rename column AVANCEMENT_DIFF to PROPOSITION_AVANCEMENT;

