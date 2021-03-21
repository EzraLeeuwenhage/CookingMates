CREATE DATABASE log_in_database;

CREATE TABLE log_in(
    log_id SERIAL PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255)
);