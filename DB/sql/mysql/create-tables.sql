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
	KEY ix_species_id (species_id),
	PRIMARY KEY (species_id));
	
CREATE TABLE creator
	(creator_id int(11) NOT NULL AUTO_INCREMENT,
	creator_fb_id varchar(40) NOT NULL,
	creator_name varchar(80) NOT NULL,
	KEY ix_creator_id (creator_id),
	PRIMARY KEY (creator_id));
	
CREATE TABLE organization
	(organization_id int(11) NOT NULL AUTO_INCREMENT,
	organization_fb_id varchar(40) NOT NULL,
	organization_name varchar(80) NOT NULL,
	KEY ix_organization_id (organization_id),
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
	
CREATE TABLE location
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
	KEY ix_school_id (school_id),
	PRIMARY KEY (school_id));
	
CREATE TABLE rank
	(rank_id int(11) NOT NULL AUTO_INCREMENT,
	rank_fb_id varchar(40) NOT NULL,
	rank_name varchar(80) NOT NULL,
	KEY ix_rank_id (rank_id),
	PRIMARY KEY (rank_id));
	
CREATE TABLE ethnicity
	(ethnicity_id int(11) NOT NULL AUTO_INCREMENT,
	ethnicity_fb_id varchar(40) NOT NULL,
	ethnicity_name varchar(80) NOT NULL,
	KEY ix_ethnicity_id (ethnicity_id),
	PRIMARY KEY (ethnicity_id));
	
CREATE TABLE occupation 
	(occupation_id int(11) NOT NULL AUTO_INCREMENT,
	occupation_fb_id varchar(40) NOT NULL,
	occupation_name varchar(80) NOT NULL,
	KEY ix_occupation_id (occupation_id),
	PRIMARY KEY (occupation_id));
	
CREATE TABLE power 
	(power_id int(11) NOT NULL AUTO_INCREMENT,
	power_fb_id varchar(40) NOT NULL,
	power_name varchar(140) NOT NULL,
	KEY ix_power_id (power_id),
	PRIMARY KEY (power_id));
	
CREATE TABLE job 
	(job_id int(11) NOT NULL AUTO_INCREMENT,
	job_fb_id varchar(40) NOT NULL,
	job_name varchar(80) NOT NULL,
	KEY ix_job_id (job_id),
	PRIMARY KEY (job_id));
	
CREATE TABLE disease
	(disease_id int(11) NOT NULL AUTO_INCREMENT,
	disease_fb_id varchar(40) NOT NULL,
	disease_name varchar(80) NOT NULL,
	KEY ix_disease_id (disease_id),
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
	FOREIGN KEY (characters_and_genders_character_id) REFERENCES characters(character_id) ON UPDATE RESTRICT ON DELETE CASCADE,
	FOREIGN KEY (characters_and_genders_gender_id) REFERENCES gender(gender_id) ON UPDATE RESTRICT ON DELETE RESTRICT);

CREATE TABLE characters_and_species 
	(characters_and_species_character_id int(11) NOT NULL,
	characters_and_species_species_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_species_character_id,characters_and_species_species_id),
	FOREIGN KEY (characters_and_species_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_species_species_id) REFERENCES species(species_id));

CREATE TABLE characters_and_creators 
	(characters_and_creators_character_id int(11) NOT NULL,
	characters_and_creators_creator_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_creators_character_id,characters_and_creators_creator_id),
	FOREIGN KEY (characters_and_creators_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_creators_creator_id) REFERENCES creator(creator_id));

CREATE TABLE characters_and_organizations 
	(characters_and_organizations_character_id int(11) NOT NULL,
	characters_and_organizations_organization_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_organizations_character_id,characters_and_organizations_organization_id),
	FOREIGN KEY (characters_and_organizations_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_organizations_organization_id) REFERENCES organization(organization_id));

CREATE TABLE characters_and_schools 
	(characters_and_schools_character_id int(11) NOT NULL,
	characters_and_schools_school_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_schools_character_id,characters_and_schools_school_id),
	FOREIGN KEY (characters_and_schools_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_schools_school_id) REFERENCES school(school_id));

CREATE TABLE characters_and_ranks 
	(characters_and_ranks_character_id int(11) NOT NULL,
	characters_and_ranks_rank_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_ranks_character_id,characters_and_ranks_rank_id),
	FOREIGN KEY (characters_and_ranks_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_ranks_rank_id) REFERENCES rank(rank_id));

CREATE TABLE characters_and_ethnicities 
	(characters_and_ethnicities_character_id int(11) NOT NULL,
	characters_and_ethnicities_ethnicity_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_ethnicities_character_id,characters_and_ethnicities_ethnicity_id),
	FOREIGN KEY (characters_and_ethnicities_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_ethnicities_ethnicity_id) REFERENCES ethnicity(ethnicity_id));

CREATE TABLE characters_and_occupations 
	(characters_and_occupations_character_id int(11) NOT NULL,
	characters_and_occupations_occupation_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_occupations_character_id,characters_and_occupations_occupation_id),
	FOREIGN KEY (characters_and_occupations_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_occupations_occupation_id) REFERENCES occupation(occupation_id));

CREATE TABLE characters_and_powers 
	(characters_and_powers_character_id int(11) NOT NULL,
	characters_and_powers_power_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_powers_character_id,characters_and_powers_power_id),
	FOREIGN KEY (characters_and_powers_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_powers_power_id) REFERENCES power(power_id));

CREATE TABLE characters_and_diseases 
	(characters_and_diseases_character_id int(11) NOT NULL,
	characters_and_diseases_disease_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_diseases_character_id,characters_and_diseases_disease_id),
	FOREIGN KEY (characters_and_diseases_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (characters_and_diseases_disease_id) REFERENCES disease(disease_id));


CREATE TABLE marriage 
	(marriage_character_id1 int(11) NOT NULL,
	marriage_character_id2 int(11) NOT NULL,
	PRIMARY KEY (marriage_character_id1,marriage_character_id2),
	FOREIGN KEY (marriage_character_id1) REFERENCES characters(character_id),
	FOREIGN KEY (marriage_character_id2) REFERENCES characters(character_id));

CREATE TABLE romantic_involvement
	(romantic_involvement_character_id1 int(11) NOT NULL,
	romantic_involvement_character_id2 int(11) NOT NULL,
	PRIMARY KEY (romantic_involvement_character_id1,romantic_involvement_character_id2),
	FOREIGN KEY (romantic_involvement_character_id1) REFERENCES characters(character_id),
	FOREIGN KEY (romantic_involvement_character_id2) REFERENCES characters(character_id));

CREATE TABLE sibling
	(sibling_character_id1 int(11) NOT NULL,
	sibling_character_id2 int(11) NOT NULL,
	PRIMARY KEY (sibling_character_id1,sibling_character_id2),
	FOREIGN KEY (sibling_character_id1) REFERENCES characters(character_id),
	FOREIGN KEY (sibling_character_id2) REFERENCES characters(character_id));

CREATE TABLE parent
	(parent_child_character_id int(11) NOT NULL,
	parent_parent_character_id int(11) NOT NULL,
	PRIMARY KEY (parent_child_character_id,parent_parent_character_id),
	FOREIGN KEY (parent_child_character_id) REFERENCES characters(character_id),
	FOREIGN KEY (parent_parent_character_id) REFERENCES characters(character_id));

CREATE TABLE characters_and_jobs
	(characters_and_jobs_job_id int(11) NOT NULL,
	characters_and_jobs_character_id int(11) NOT NULL,
	PRIMARY KEY (characters_and_jobs_job_id,characters_and_jobs_character_id),
	FOREIGN KEY (characters_and_jobs_job_id) REFERENCES job(job_id),
	FOREIGN KEY (characters_and_jobs_character_id) REFERENCES characters(character_id));

