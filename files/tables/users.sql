CREATE TABLE users (
    id int NOT NULL AUTO_INCREMENT,
    username varchar(200) NOT NULL,
    email varchar(200) NOT NULL,
    password text NOT NULL,
    gender varchar(6) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id)
);
