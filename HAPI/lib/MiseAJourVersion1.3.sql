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