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


