CREATE TABLE students 
	(student_id int(11) NOT NULL,
	student_name varchar(80) NOT NULL,
	PRIMARY KEY (student_id));

CREATE TABLE courses 
	(course_id int(11) NOT NULL,
	course_name varchar(80) NOT NULL,
	PRIMARY KEY (course_id));

CREATE TABLE colors 
	(color_id int(11) NOT NULL,
	color_name varchar(80) NOT NULL,
	PRIMARY KEY (color_id));


CREATE TABLE cities 
	(city_id int(11) NOT NULL,
	city_name varchar(80) NOT NULL,
	PRIMARY KEY (city_id));

CREATE TABLE conn 
	(student_id int(11) NOT NULL,
	course_id int(11) NOT NULL,			
	PRIMARY KEY (student_id,course_id));

CREATE TABLE conn2 
	(student_id int(11) NOT NULL,
	city_id int(11) NOT NULL,			
	PRIMARY KEY (student_id,city_id));

CREATE TABLE conn3 
	(student_id int(11) NOT NULL,
	color_id int(11) NOT NULL,			
	PRIMARY KEY (student_id,color_id));
	
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
	KEY ix_gender_id (gender_id),
	PRIMARY KEY (gender_id));
	
CREATE TABLE universe
	(universe_id int(11) NOT NULL AUTO_INCREMENT,
	universe_fb_id varchar(40) NOT NULL,
	universe_name varchar(80) NOT NULL,
	KEY ix_universe_id (universe_id),
	PRIMARY KEY (universe_id,universe_fb_id));
	
CREATE TABLE locations
	(location_id int(11) NOT NULL AUTO_INCREMENT,
	location_name varchar(80) NOT NULL,
	location_universe_id int(11) NOT NULL,
	KEY ix_location_id (location_id),
	PRIMARY KEY (location_id,location_name),
	FOREIGN KEY (location_universe_id) REFERENCES universe (universe_id));
	
CREATE TABLE place_of_birth
	(place_of_birth_id int(11) NOT NULL AUTO_INCREMENT,
	place_of_birth_name varchar(80) NOT NULL,
	KEY ix_place_of_birth_id (place_of_birth_id),
	PRIMARY KEY (place_of_birth_id),UNIQUE (place_of_birth_name));
	
CREATE TABLE school
	(school_id int(11) NOT NULL AUTO_INCREMENT,
	school_fb_id varchar(40) NOT NULL,
	school_name varchar(80) NOT NULL,
	PRIMARY KEY (school_id));
	
CREATE TABLE rank
	(rank_id int(11) NOT NULL AUTO_INCREMENT,
	rank_fb_id varchar(40) NOT NULL,
	rank_name varchar(80) NOT NULL,PRIMARY KEY (rank_id));
	
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
	
CREATE TABLE characters 
	(character_id int(11) NOT NULL AUTO_INCREMENT,
	character_fb_id varchar(40) NOT NULL,
	character_name varchar(80) NOT NULL,
	character_place_of_birth_id int(11) NOT NULL,
	KEY ix_character_id (character_id),
	PRIMARY KEY (character_id),
	FOREIGN KEY (character_place_of_birth_id) REFERENCES place_of_birth(place_of_birth_id)) DEFAULT CHARSET=utf8;
	
CREATE TABLE characters_and_universes 
	(characters_and_universes_character_id int(11) NOT NULL,
	characters_and_universes_universe_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_universes_character_id,characters_and_universes_universe_id),
	FOREIGN KEY (characters_and_universes_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_universes_universe_id) REFERENCES universe(universe_id));
	
CREATE TABLE characters_and_genders 
	(characters_and_genders_character_id int(11) NOT NULL,
	characters_and_genders_gender_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_genders_character_id,characters_and_genders_gender_id),
	FOREIGN KEY (characters_and_genders_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_genders_gender_id) REFERENCES gender(gender_id));