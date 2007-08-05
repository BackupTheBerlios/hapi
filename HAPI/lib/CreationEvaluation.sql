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