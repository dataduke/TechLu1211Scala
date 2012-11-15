# --- !Ups

CREATE SEQUENCE seq_person_id;

CREATE TABLE person (
  id BIGINT NOT NULL DEFAULT nextval('seq_person_id') PRIMARY KEY,
  givenname VARCHAR(32) NOT NULL,
  surname VARCHAR(32) NOT NULL,
  age INTEGER NOT NULL
);

CREATE TABLE information (
  id INTEGER NOT NULL,
  message VARCHAR(64) NOT NULL
);

INSERT INTO information (id, message) VALUES (1, 'This is a terrible test!')


# --- !Downs

DROP TABLE person;

DROP SEQUENCE seq_person_id;
