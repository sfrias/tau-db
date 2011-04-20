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