CREATE TABLE place_of_birth
		(place_of_birth_id int(11) NOT NULL AUTO_INCREMENT,
		place_of_birth_name varchar(80) NOT NULL,
		KEY ix_place_of_birth_id (place_of_birth_id),
		PRIMARY KEY (place_of_birth_id),UNIQUE (place_of_birth_name)) ENGINE=InnoDB;
	
CREATE TABLE organization
	(organization_id int(11) NOT NULL AUTO_INCREMENT,
	organization_fb_id varchar(40) NOT NULL DEFAULT '',
	organization_name varchar(80) NOT NULL,
	KEY ix_organization_id (organization_id),
	PRIMARY KEY (organization_id)) ENGINE=InnoDB;
	
CREATE TABLE universe
	(universe_id int(11) NOT NULL AUTO_INCREMENT,
	universe_fb_id varchar(40) NOT NULL DEFAULT '',
	universe_name varchar(80) NOT NULL,
	KEY ix_universe_id (universe_id),
	PRIMARY KEY (universe_id)) ENGINE=InnoDB;
	
CREATE TABLE school
	(school_id int(11) NOT NULL AUTO_INCREMENT,
	school_fb_id varchar(40) NOT NULL DEFAULT '',
	school_name varchar(80) NOT NULL,
	KEY ix_school_id (school_id),
	PRIMARY KEY (school_id)) ENGINE=InnoDB;
	
CREATE TABLE occupation 
	(occupation_id int(11) NOT NULL AUTO_INCREMENT,
	occupation_fb_id varchar(40) NOT NULL DEFAULT '',
	occupation_name varchar(80) NOT NULL,
	KEY ix_occupation_id (occupation_id),
	PRIMARY KEY (occupation_id)) ENGINE=InnoDB;
	
CREATE TABLE power 
	(power_id int(11) NOT NULL AUTO_INCREMENT,
	power_fb_id varchar(40) NOT NULL DEFAULT '',
	power_name varchar(140) NOT NULL,
	KEY ix_power_id (power_id),
	PRIMARY KEY (power_id)) ENGINE=InnoDB;
	
CREATE TABLE disease
	(disease_id int(11) NOT NULL AUTO_INCREMENT,
	disease_fb_id varchar(40) NOT NULL DEFAULT '',
	disease_name varchar(80) NOT NULL,
	KEY ix_disease_id (disease_id),
	PRIMARY KEY (disease_id)) ENGINE=InnoDB;
	
CREATE TABLE characters
	(character_id int(11) NOT NULL AUTO_INCREMENT,
	character_fb_id varchar(40) NOT NULL DEFAULT '',
	character_name varchar(80) NOT NULL,
	character_place_of_birth_id int(11) NOT NULL DEFAULT 5,
	KEY ix_character_id (character_id),
	KEY ix_character_and_pob (character_id,character_place_of_birth_id),
	PRIMARY KEY (character_id),
	FOREIGN KEY (character_place_of_birth_id) REFERENCES place_of_birth(place_of_birth_id) ON UPDATE RESTRICT ON DELETE RESTRICT) DEFAULT CHARSET=utf8 ENGINE=InnoDB;
	
CREATE TABLE characters_and_universe 
	(characters_and_universe_character_id int(11) NOT NULL,
	characters_and_universe_universe_id int(11) NOT NULL,
	KEY ix_characters_and_universe (characters_and_universe_character_id, characters_and_universe_universe_id),
	PRIMARY KEY (characters_and_universe_character_id,characters_and_universe_universe_id),
	FOREIGN KEY (characters_and_universe_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_universe_universe_id) REFERENCES universe(universe_id) ON UPDATE RESTRICT ON DELETE RESTRICT) ENGINE=InnoDB;
	

CREATE TABLE characters_and_organization 
	(characters_and_organization_character_id int(11) NOT NULL,
	characters_and_organization_organization_id int(11) NOT NULL,
	KEY ix_characters_and_organization (characters_and_organization_character_id, characters_and_organization_organization_id),	
	PRIMARY KEY (characters_and_organization_character_id,characters_and_organization_organization_id),
	FOREIGN KEY (characters_and_organization_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_organization_organization_id) REFERENCES organization(organization_id) ON UPDATE RESTRICT ON DELETE RESTRICT) ENGINE=InnoDB;

CREATE TABLE characters_and_school 
	(characters_and_school_character_id int(11) NOT NULL,
	characters_and_school_school_id int(11) NOT NULL,
	KEY ix_characters_and_school (characters_and_school_character_id, characters_and_school_school_id),	
	PRIMARY KEY (characters_and_school_character_id,characters_and_school_school_id),
	FOREIGN KEY (characters_and_school_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_school_school_id) REFERENCES school(school_id) ON UPDATE RESTRICT ON DELETE RESTRICT) ENGINE=InnoDB;

CREATE TABLE characters_and_occupation 
	(characters_and_occupation_character_id int(11) NOT NULL,
	characters_and_occupation_occupation_id int(11) NOT NULL,
	KEY ix_characters_and_organization (characters_and_occupation_character_id, characters_and_occupation_occupation_id),	
	PRIMARY KEY (characters_and_occupation_character_id,characters_and_occupation_occupation_id),
	FOREIGN KEY (characters_and_occupation_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_occupation_occupation_id) REFERENCES occupation(occupation_id) ON UPDATE RESTRICT ON DELETE RESTRICT) ENGINE=InnoDB;

CREATE TABLE characters_and_power 
	(characters_and_power_character_id int(11) NOT NULL,
	characters_and_power_power_id int(11) NOT NULL,
	KEY ix_characters_and_organization (characters_and_power_character_id, characters_and_power_power_id),	
	PRIMARY KEY (characters_and_power_character_id,characters_and_power_power_id),
	FOREIGN KEY (characters_and_power_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_power_power_id) REFERENCES power (power_id) ON UPDATE RESTRICT ON DELETE RESTRICT) ENGINE=InnoDB;

CREATE TABLE characters_and_disease 
	(characters_and_disease_character_id int(11) NOT NULL,
	characters_and_disease_disease_id int(11) NOT NULL,
	KEY ix_characters_and_disease (characters_and_disease_character_id, characters_and_disease_disease_id),	
	PRIMARY KEY (characters_and_disease_character_id,characters_and_disease_disease_id),
	FOREIGN KEY (characters_and_disease_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_disease_disease_id) REFERENCES disease(disease_id) ON UPDATE RESTRICT ON DELETE RESTRICT) ENGINE=InnoDB;


CREATE TABLE romantic_involvement
	(romantic_involvement_character_id1 int(11) NOT NULL,
	romantic_involvement_character_id2 int(11) NOT NULL,
	KEY ix_romantic_involvement (romantic_involvement_character_id1, romantic_involvement_character_id2),	
	PRIMARY KEY (romantic_involvement_character_id1,romantic_involvement_character_id2),
	FOREIGN KEY (romantic_involvement_character_id1) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (romantic_involvement_character_id2) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE) ENGINE=InnoDB;

CREATE TABLE parent
	(parent_child_character_id int(11) NOT NULL,
	parent_parent_character_id int(11) NOT NULL,
	KEY ix_romantic_involvement (parent_parent_character_id, parent_child_character_id),	
	PRIMARY KEY (parent_child_character_id,parent_parent_character_id),
	FOREIGN KEY (parent_child_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (parent_parent_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE) ENGINE=InnoDB;

CREATE TABLE history
	(character_id1 int(11) NOT NULL,
	character_id2 int(11) NOT NULL,
	date date DEFAULT NULL,
	information varchar(200),
	count int(11) DEFAULT 1,
	KEY ix_history (character_id1, character_id2),	
	PRIMARY KEY (character_id1,character_id2),
	FOREIGN KEY (character_id1) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (character_id2) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE) ENGINE=InnoDB;

CREATE TABLE failed_searches
	(character_id1 int(11) NOT NULL,
	character_id2 int(11) NOT NULL,
	date date DEFAULT NULL,
	KEY ix_failed (character_id1, character_id2),	
	PRIMARY KEY (character_id1,character_id2),
	FOREIGN KEY (character_id1) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (character_id2) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE) ENGINE=InnoDB;


CREATE TABLE statistic
	(character_id1 int(11) NOT NULL,
	character_id2 int(11) NOT NULL,
	num_of_connections int(11),
	time int(11),
	PRIMARY KEY (character_id1,character_id2),
	FOREIGN KEY (character_id1) REFERENCES characters(character_id),
	FOREIGN KEY (character_id2) REFERENCES characters(character_id));

