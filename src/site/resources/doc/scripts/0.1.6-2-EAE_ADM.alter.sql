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
alter table EAE_EVALUATION modify (AVANCEMENT_DIFF varchar2(4));
alter table EAE_EVALUATION modify (AVIS_SHD varchar2(12));
alter table EAE_EVALUATION rename column AVIS to AVIS_REVALORISATION;
alter table EAE_EVALUATION rename column CHANGEMENT_CLASSE to AVIS_CHANGEMENT_CLASSE;
alter table EAE_EVALUATION rename column AVANCEMENT_DIFF to PROPOSITION_AVANCEMENT;
alter table EAE_EVALUATION modify (AVIS_CHANGEMENT_CLASSE NUMBER(1,0));
alter table EAE_EVALUATION modify (AVIS_REVALORISATION NUMBER(1,0));

update EAE_EVALUE set TYPE_AVCT = null;
alter table EAE_EVALUE modify (TYPE_AVCT varchar2(5));

alter table EAE_AUTO_EVALUATION add (VERSION number default 0 not null);