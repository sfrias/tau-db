CREATE TABLE species 
	(species_id int(11) NOT NULL AUTO_INCREMENT,
	species_fb_id varchar(40) NOT NULL,
	species_name varchar(80) NOT NULL,
	PRIMARY KEY (species_id));

CREATE TABLE creator
	(creator_id int(11) NOT NULL AUTO_INCREMENT,
	creator_fb_id varchar(40) NOT NULL,
	creator_name varchar(80) NOT NULL,
	PRIMARY KEY (creator_id));

CREATE TABLE organization 
	(organization_id int(11) NOT NULL AUTO_INCREMENT,
	organization_fb_id varchar(40) NOT NULL,
	organization_name varchar(80) NOT NULL,
	PRIMARY KEY (organization_id));
	
CREATE TABLE gender 
	(gender_id int(11) NOT NULL AUTO_INCREMENT,
	gender_fb_id varchar(40) NOT NULL,
	gender_name varchar(80) NOT NULL,
	PRIMARY KEY (gender_id));	
	
CREATE TABLE universe 
	(universe_id int(11) NOT NULL AUTO_INCREMENT,
	universe_fb_id varchar(40) NOT NULL,
	universe_name varchar(80) NOT NULL,
	KEY ix_universe_fb_id (universe_fb_id),
	PRIMARY KEY (universe_id, universe_fb_id));
	
CREATE TABLE school 
	(school_id int(11) NOT NULL AUTO_INCREMENT,
	school_fb_id varchar(40) NOT NULL,
	school_name varchar(80) NOT NULL,
	PRIMARY KEY (school_id));

CREATE TABLE rank
	(rank_id int(11) NOT NULL AUTO_INCREMENT,
	rank_fb_id varchar(40) NOT NULL,
	rank_name varchar(80) NOT NULL,
	PRIMARY KEY (rank_id));


CREATE TABLE ethnicity
	(ethnicity_id int(11) NOT NULL AUTO_INCREMENT,
	ethnicity_fb_id varchar(40) NOT NULL,
	ethnicity_name varchar(80) NOT NULL,
	PRIMARY KEY (ethnicity_id));

CREATE TABLE occupation
	(occupation_id int(11) NOT NULL AUTO_INCREMENT,
	occupation_fb_id varchar(40) NOT NULL,
	occupation_name varchar(80) NOT NULL,
	PRIMARY KEY (occupation_id));

CREATE TABLE powers
	(power_id int(11) NOT NULL AUTO_INCREMENT,
	power_fb_id varchar(40) NOT NULL,
	power_name varchar(140) NOT NULL,
	PRIMARY KEY (power_id));

CREATE TABLE jobs
	(job_id int(11) NOT NULL AUTO_INCREMENT,
	job_fb_id varchar(40) NOT NULL,
	job_name varchar(80) NOT NULL,
	PRIMARY KEY (job_id));

CREATE TABLE diseases
	(disease_id int(11) NOT NULL AUTO_INCREMENT,
	disease_fb_id varchar(40) NOT NULL,
	disease_name varchar(80) NOT NULL,
	PRIMARY KEY (disease_id));


CREATE TABLE locations 
	(location_id int(11) NOT NULL AUTO_INCREMENT,
	location_name varchar(80) NOT NULL, 
	location_universe_fb_id varchar(40) NOT NULL,
	PRIMARY KEY (location_id,location_name),
	FOREIGN KEY (location_universe_fb_id) REFERENCES universe (universe_fb_id));