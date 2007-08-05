DROP TABLE IF EXISTS `activites`, `chargesParComposant`, `composants`, `cycleDeVie`, `definitions`, `demandesModifications`, `elementsPresentation`, `etats`, `evaluations`, `executionProcessus`, `guides`, `historiqueModifications`, `indicateurs`, `interfaces`, `lienCycleActivite`, `lienCycleProduit`, `lienCycleRole`, `liensProdActEntree`, `liensProdActSortie`, `liensProduitEtat`, `liensProduitInterface`, `listeDescriptionMaturite`, `mesureAcces`, `mesureAmelioration`, `mesureFormation`, `mesureRepresentation`, `mesureUtilisation`, `niveauMaturite`, `ordreactivite`, `paquetagesPresentation`, `processus`, `produits`, `revuesProcessus`, `roles`, `rolesevaluations`, `rolesutilisateurs`, `serveurs`, `seuils`, `typesGuide`, `typesProduit`, `utilisateurs`, `versionprocessus`;

CREATE TABLE `rolesutilisateurs`
(
`numero` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
`nom` TEXT NOT NULL
) TYPE = INNODB;

CREATE TABLE `utilisateurs`
(
`numero` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
`login` VARCHAR(200) NOT NULL, 
`motdepasse` TEXT,
`nom` TEXT NOT NULL,
`prenom` TEXT NOT NULL,
`langue` TEXT NOT NULL, 
`aspectGraphe` TEXT NOT NULL,
`idrole` INT NOT NULL REFERENCES rolesutilisateurs(numero),
UNIQUE (`login`,`idrole`),
INDEX (`idrole`)
) TYPE = INNODB;

CREATE TABLE `serveurs`
(
`numero` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
`type` INT NOT NULL, 
`adresse` TEXT NOT NULL, 
`login` TEXT, 
`motdepasse` TEXT, 
`selection` CHAR NOT NULL,
`idutil` INT NOT NULL REFERENCES utilisateurs(numero),
INDEX (`idutil`)
) TYPE = INNODB;

INSERT INTO `rolesutilisateurs` VALUES ('1', 'Responsable'); 
INSERT INTO `rolesutilisateurs` VALUES ('2', 'Directeur'); 
INSERT INTO `rolesutilisateurs` VALUES ('3', 'Ingenieur'); 

INSERT INTO `utilisateurs` VALUES (NULL, 'directeur', '', 'Equipe', 'projet', 'fr', 'Metal',2);


#################################
## Création Evaluation
#################################
CREATE TABLE `evaluations`
(
`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`idProcessus` VARCHAR(200) NOT NULL,
`idExecutionProcessus` INT NOT NULL,
`numIteration` INT,
`RUA` FLOAT,
`RCT` FLOAT,
`RUR` FLOAT,
`RUP` FLOAT,
`evalQualitative` LONGTEXT,
`debut` DATE,
`fin` DATE,
UNIQUE(idProcessus,idExecutionProcessus,numIteration)
) TYPE = INNODB;

CREATE TABLE `ordreactivite`
(
`idevaluation` INT NOT NULL,
`rang` INT NOT NULL,
`nomTache` TEXT NOT NULL,
`idActivite` VARCHAR(100) NOT NULL,
PRIMARY KEY(idevaluation,rang,idActivite)
) TYPE = INNODB;

CREATE TABLE `rolesevaluations`
(
`idevaluation` INT NOT NULL,
`nom` TEXT NOT NULL,
`dehors` CHAR NOT NULL,
`type` CHAR NOT NULL
) TYPE = INNODB;

CREATE TABLE `chargesParComposant`
(
	`idevaluation` INT NOT NULL,
	`idcomposant` VARCHAR(200) NOT NULL,
	`charge` FLOAT,
	PRIMARY KEY(idevaluation,idcomposant)
) TYPE = INNODB;

##############################
## CreationExecutionProcessus
##############################

CREATE TABLE `executionProcessus`
(
`notreId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`id` INT NOT NULL,
`id_processus` VARCHAR(200) NOT NULL REFERENCES processus(id),
`version_processus` TEXT,
`nom` TEXT NOT NULL,
`description` TEXT NOT NULL,
`dateDebut` DATE NOT NULL, 
`dateFin` DATE NOT NULL, 
`commentaire` LONGTEXT NULL
) TYPE = INNODB;

###############################
## Creation Seuils
###############################
CREATE TABLE `indicateurs`
(
`id` 		INT 			NOT NULL	PRIMARY KEY,
`nom` 		TEXT 	NOT NULL,
`formule` 	TEXT 	NOT NULL
) TYPE = INNODB;

CREATE TABLE `seuils`
(
`id` 				INT 		NOT NULL AUTO_INCREMENT PRIMARY KEY,
`id_processus` 		VARCHAR(200)		NOT NULL REFERENCES processus(id),
`id_indicateur` 	INT 		NOT NULL REFERENCES indicateurs(id),
`id_utilisateur` 	INT 		NOT NULL REFERENCES utilisateurs(numero),
`min` 				DOUBLE,
`max` 				DOUBLE 
) TYPE = INNODB;

INSERT INTO `indicateurs` VALUES (1,'RUA','OO_GestionSeuils_formule_RUA');
INSERT INTO `indicateurs` VALUES (2,'RCT','OO_GestionSeuils_formule_RCT');
INSERT INTO `indicateurs` VALUES (3,'RUR','OO_GestionSeuils_formule_RUR');
INSERT INTO `indicateurs` VALUES (4,'RUP','OO_GestionSeuils_formule_RUP');


###############################
## Creation TablesProcessus
###############################
## **************** Creation des tables *****************

CREATE TABLE `processus`
(
`id` VARCHAR(200) NOT NULL PRIMARY KEY,
`nom` TEXT,
`nomAuteur` TEXT,
`emailAuteur` TEXT,
`description` TEXT,
`cheminGeneration` TEXT,
`nivMaturite` INT,
`comMaturite` TEXT,
`descnivmaturite` TEXT,
`idResponsable` INT
) TYPE = INNODB;

CREATE TABLE versionprocessus
(
`idProcessus` VARCHAR(200) NOT NULL,
`dateExport` VARCHAR(50) NOT NULL,
PRIMARY KEY(idProcessus, dateExport)
) TYPE = INNODB;

CREATE TABLE `composants`
(
`id` VARCHAR(200) NOT NULL,
`nom` TEXT,
`version` TEXT,
`nomAuteur` TEXT,
`emailAuteur` TEXT,
`datePlacement` TEXT,
`description` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idInterfaceFournie` VARCHAR(100),
`idInterfaceRequise` VARCHAR(100),
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `roles`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idComposant` VARCHAR(200),
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `produits`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idComposant` VARCHAR(200),
`idRole` VARCHAR(100),
`idTypeProduit` VARCHAR(100),
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `definitions`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idComposant` VARCHAR(200),
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `activites`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idDefinition` VARCHAR(100),
`idRole` VARCHAR(100),
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `interfaces`
(
`id` VARCHAR(100) NOT NULL, 
`idProcessus` VARCHAR(200) NOT NULL,
`idInterfaceFournieComposant` VARCHAR(200),
`idInterfaceRequiseComposant` VARCHAR(200),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `typesProduit`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `etats`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `paquetagesPresentation`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`dossierIcone` TEXT,
`dossierContenu` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `elementsPresentation`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`cheminIcone` TEXT,
`cheminContenu` TEXT,
`description` TEXT,
`cheminPage` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idPaquetagePresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `guides`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
`idTypeGuide` VARCHAR(100),
`idElementPresentation` VARCHAR(100),
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `typesGuide`
(
`id` VARCHAR(100) NOT NULL, 
`nom` TEXT,
`idProcessus` VARCHAR(200) NOT NULL,
PRIMARY KEY(id, idProcessus)
) TYPE = INNODB;

CREATE TABLE `liensProduitInterface`
(
`idProduit` VARCHAR(100) NOT NULL, 
`idInterface` VARCHAR(100) NOT NULL,
`idProcessus` VARCHAR(200) NOT NULL,
 PRIMARY KEY(idProduit, idInterface, idProcessus)
) TYPE = INNODB;

CREATE TABLE `liensProduitEtat`
(
`idProduit` VARCHAR(100) NOT NULL, 
`idEtat` VARCHAR(100) NOT NULL,
`idProcessus` VARCHAR(200) NOT NULL,
PRIMARY KEY(idProduit, idEtat, idProcessus)
) TYPE = INNODB;

CREATE TABLE `liensProdActEntree`
(
`idProduit` VARCHAR(100) NOT NULL, 
`idActivite` VARCHAR(100) NOT NULL,
`idProcessus` VARCHAR(200) NOT NULL,
PRIMARY KEY(idProduit, idActivite, idProcessus)
) TYPE = INNODB;

CREATE TABLE `liensProdActSortie`
(
`idProduit` VARCHAR(100) NOT NULL, 
`idActivite` VARCHAR(100) NOT NULL,
`idProcessus` VARCHAR(200) NOT NULL,
PRIMARY KEY(idProduit, idActivite, idProcessus)
) TYPE = INNODB;


CREATE TABLE `demandesModifications`
(
`id_demande` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`id_processus` VARCHAR(200) NOT NULL REFERENCES processus(id),
`date_demande` DATE NOT NULL,
`version_a_modifier` TEXT NOT NULL,
`nom_composant` TEXT NOT NULL,
`description` TEXT NOT NULL
) TYPE = INNODB;

CREATE TABLE `historiqueModifications`
(
`id_modif` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`id_processus` VARCHAR(200) NOT NULL REFERENCES processus(id),
`date_demande` VARCHAR(20) NOT NULL,
`version_a_modifier` TEXT NOT NULL,
`nom_composant` TEXT NOT NULL,
`description` TEXT NOT NULL,
`version_modifiee` TEXT
) TYPE = INNODB;

###################################
## Mise a jour v1.3
###################################
CREATE TABLE cycleDeVie
(
  id_cycle int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  id_projet int(11) NOT NULL UNIQUE,
  version_creation VARCHAR(50) NOT NULL
) TYPE = INNODB;

CREATE TABLE lienCycleProduit
(
  id_cycle int(11) NOT NULL,
  id_produit varchar(100) NOT NULL,
  PRIMARY KEY  (id_cycle,id_produit)
) TYPE = INNODB;

CREATE TABLE lienCycleActivite
(
  id_cycle int(11) NOT NULL,
  id_activite varchar(100) NOT NULL,
  PRIMARY KEY  (id_cycle,id_activite)
) TYPE = INNODB;

CREATE TABLE lienCycleRole
(
  id_cycle int(11) NOT NULL,
  id_role varchar(100) NOT NULL,
  PRIMARY KEY  (id_cycle,id_role)
) TYPE = INNODB;

#####################################
## Mise à jour v1.4
#####################################
CREATE TABLE `listeDescriptionMaturite`
(
  `niveauMaturite` int(11) NOT NULL default '0',
  `Description` text NOT NULL,
  UNIQUE KEY niveauMaturite (niveauMaturite)
) TYPE = InnoDB;


CREATE TABLE `niveauMaturite`
(
  `idProcessus` VARCHAR( 200 ) NOT NULL ,
  `nivMaturite` TINYINT NOT NULL ,
  `comMaturite` TEXT,
  `datePassage` DATE,
  `dateObjectif` DATE,
  PRIMARY KEY ( `idProcessus` , `nivMaturite` ) 
)  TYPE = InnoDB;

ALTER TABLE `processus` DROP `descnivmaturite`;
ALTER TABLE `processus` DROP `comMaturite`;
ALTER TABLE `versionprocessus` ADD `numeroVersion` TEXT NOT NULL ;

CREATE TABLE `mesureRepresentation`
(
  `idProcessus` VARCHAR(200) NOT NULL, 
  `dateExport` VARCHAR(50) NOT NULL,
  `interfaceE` INT NOT NULL, 
  `interfaceS` INT NOT NULL, 
  `scenario` INT NOT NULL, 
  `comScenario` TEXT, 
  `explicite` INT NOT NULL, 
  `comExplicite` TEXT, 
  `estime` INT NOT NULL, 
  `comEstime` TEXT, 
  `evaluationIT` DOUBLE NOT NULL, 
  `comEvalIT` TEXT, 
  `coherence` INT NOT NULL, 
  `comCoherence` TEXT, 
  `competude` INT NOT NULL, 
  `comCompletude` TEXT, 
  `nbDef` INT NOT NULL, 
  `nbAct` INT NOT NULL, 
  `nbProd` INT NOT NULL, 
  `nbRole` INT NOT NULL, 
  `evaluationMo` DOUBLE NOT NULL, 
  `comEvalMo` TEXT, 
  `nbProdType` INT NOT NULL, 
  `comNbProdType` TEXT, 
  `pourPlan` DOUBLE NOT NULL, 
  `nbGuide` INT NOT NULL, 
  `comGuide` TEXT, 
  `pourGuide` DOUBLE NOT NULL, 
  `nbDocu` INT NOT NULL, 
  `comDocu` TEXT, 
  `pourDocu` DOUBLE NOT NULL, 
  `evaluationDocu` DOUBLE NOT NULL, 
  `comEvalDocu` TEXT,
  PRIMARY KEY (`idProcessus`, `dateExport`)
)  TYPE = InnoDB;

ALTER TABLE `serveurs` CHANGE `type` `type` VARCHAR(3) DEFAULT '0' NOT NULL;

CREATE TABLE `revuesProcessus`
(
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  `dateRevue` DATE NOT NULL, 
  `decision` TEXT NOT NULL, 
  `action` TEXT NOT NULL,
  `idProcessus` VARCHAR(200) NOT NULL
)  TYPE = InnoDB;

#####################################
## Mise à jour v1.5
#####################################
ALTER TABLE `revuesProcessus` ADD `dateProchaineRevue` DATE AFTER `dateRevue`;

CREATE TABLE `mesureAcces`
(
  `id` INT NOT NULL AUTO_INCREMENT ,
  `mois` DATE NOT NULL ,
  `nombre` INT NOT NULL ,
  `idProcessus` VARCHAR(200) NOT NULL,
  PRIMARY KEY ( `id` ) 
) TYPE = InnoDB;

CREATE TABLE `mesureFormation`
(
  `id` INT NOT NULL AUTO_INCREMENT ,
  `dateMesure` DATE NOT NULL ,
  `effectifProcessus` INT NOT NULL ,
  `effectifMesure` INT NOT NULL ,
  `note` DOUBLE NOT NULL ,
  `idProcessus` VARCHAR(200) NOT NULL,  
  PRIMARY KEY ( `id` ) 
) TYPE = InnoDB;

CREATE TABLE `mesureUtilisation`
(
  `id` INT NOT NULL AUTO_INCREMENT ,
  `idProjet` INT NOT NULL ,
  `nombrePlanType` INT NOT NULL ,
  `pourPlanType` DOUBLE NOT NULL ,
  `nombreActivite` INT NOT NULL ,
  `pourActivite` DOUBLE NOT NULL ,
  `idProcessus` VARCHAR(200) NOT NULL,  
  PRIMARY KEY ( `id` ) 
) TYPE = InnoDB;

ALTER TABLE `demandesModifications` DROP `version_a_modifier`;
ALTER TABLE `demandesModifications` ADD `type` TEXT NOT NULL,ADD `severite` TINYINT NOT NULL;
ALTER TABLE `historiqueModifications` DROP `version_a_modifier` ;
ALTER TABLE `historiqueModifications` ADD `type` TEXT NOT NULL,ADD `severite` TINYINT NOT NULL;

CREATE TABLE `mesureAmelioration`
(
  `idProcessus` VARCHAR( 200 ) NOT NULL ,
  `dateExport` VARCHAR( 50 ) NOT NULL ,
  `dateChangement` DATE NOT NULL ,  
  `nombrePropose` INT NOT NULL ,
  `nombrePris` INT NOT NULL ,
  `presentation` INT NOT NULL ,
  `modele` INT NOT NULL ,
  `documentation` INT NOT NULL ,
  PRIMARY KEY ( `idProcessus` , `dateExport` ) 
) TYPE = InnoDB;