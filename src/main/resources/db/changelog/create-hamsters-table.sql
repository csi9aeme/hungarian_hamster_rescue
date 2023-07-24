CREATE TABLE hamsters
(
    hamster_id         BIGINT auto_increment,
    name               VARCHAR(255),
    hamster_species    VARCHAR(255),
    gender             VARCHAR(255),
    date_of_birth      DATE,
    hamster_status     VARCHAR(255),
    start_of_fostering DATE,
    date_of_adoption   DATE,
    PRIMARY KEY (hamster_id)
);
