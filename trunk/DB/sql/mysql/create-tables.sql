create table CHARACTERS
  (CHAR_ID integer NOT NULL,
  CHAR_NAME varchar(40) NOT NULL,
  PRIMARY KEY (CHAR_ID));
  
 create table FICTIONAL_ORGANIZATIONS
 	(ORG_NAME varchar(40) NOT NULL,
 	ORG_ID varchar(40) NOT NULL,
 	ORG_MEMBER varchar(40),
 	ORG_TYPE varchar(40),
 	ORG_APPEARS varchar(40),
 	ORG_FOUNDER varchar(40),
 	ORG_PARENT_ORG varchar(40),
 	ORG_SUB_ORG varchar(40),
 	PRIMARY KEY (ORG_ID));