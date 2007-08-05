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