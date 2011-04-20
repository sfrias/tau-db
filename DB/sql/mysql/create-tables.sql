CREATE TABLE species 
	(`species_id` int(11) NOT NULL AUTO_INCREMENT,
	`species_fb_id` varchar(40) NOT NULL,
	`species_name` varchar(80) NOT NULL,
	PRIMARY KEY (`species_id`));

CREATE TABLE creator
	(`creator_id` int(11) NOT NULL AUTO_INCREMENT,
	`creator_fb_id` varchar(40) NOT NULL,
	`creator_name` varchar(80) NOT NULL,
	PRIMARY KEY (`creator_id`));

CREATE TABLE organization 
	(`organization_id` int(11) NOT NULL AUTO_INCREMENT,
	`organization_fb_id` varchar(40) NOT NULL,
	`organization_name` varchar(80) NOT NULL,
	PRIMARY KEY (`organization_id`));
	
CREATE TABLE gender 
	(`gender_id` int(11) NOT NULL AUTO_INCREMENT,
	`gender_fb_id` varchar(40) NOT NULL,
	`gender_name` varchar(80) NOT NULL,
	PRIMARY KEY (`gender_id`));	
	
CREATE TABLE universe 
	(`universe_id` int(11) NOT NULL AUTO_INCREMENT,
	`universe_fb_id` varchar(40) NOT NULL,
	`universe_name` varchar(80) NOT NULL,
	PRIMARY KEY (`universe_id`));
	
CREATE TABLE school 
	(`school_id` int(11) NOT NULL AUTO_INCREMENT,
	`school_fb_id` varchar(40) NOT NULL,
	`school_name` varchar(80) NOT NULL,
	PRIMARY KEY (`school_id`));