-- **************** Creation des tables *****************

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

