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

ALTER TABLE `demandesModifications` DROP `version_a_modifier` ;
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