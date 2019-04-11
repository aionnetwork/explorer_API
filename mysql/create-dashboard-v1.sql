CREATE DATABASE dashboard_v1;

USE dashboard_v1;

CREATE TABLE feedback(
	id bigint(64) PRIMARY KEY NOT NULL,
	topic varchar(64) NOT NULL,
    message text NOT NULL,
    timestamp bigint(32) NOT NULL
)ENGINE = InnoDB;

CREATE INDEX topic_feedback on feedback(topic);
