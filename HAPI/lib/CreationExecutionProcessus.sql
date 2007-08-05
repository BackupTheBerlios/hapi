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