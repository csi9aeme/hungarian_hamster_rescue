CREATE TABLE hosts
(
    host_id BIGINT auto_increment,
    name VARCHAR(255),
    holding_capacity INT,
    host_status VARCHAR(255),
    zip VARCHAR(4),
    town VARCHAR(255),
    street VARCHAR(255),
    house_number VARCHAR(255),
    other VARCHAR(255),
    PRIMARY KEY (host_id)
);


