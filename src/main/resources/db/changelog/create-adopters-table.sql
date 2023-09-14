CREATE TABLE adopters
(
    adopter_id BIGINT auto_increment,
    name VARCHAR(255),
    zip VARCHAR(4),
    town VARCHAR(255),
    street VARCHAR(255),
    house_number VARCHAR(255),
    other VARCHAR(255),
    PRIMARY KEY (adopter_id)

);
