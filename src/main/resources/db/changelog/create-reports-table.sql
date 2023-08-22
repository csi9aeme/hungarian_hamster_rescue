CREATE TABLE weekly_reports
(
    id_of_report BIGINT auto_increment,
    date_of_measure DATE,
    weight DOUBLE,
    report_text VARCHAR(1000),
    PRIMARY KEY (id_of_report)

);
