DROP TABLE IF EXISTS pipeline;

CREATE TABLE pipeline (
pipeline_id bigserial NOT NULL,
name VARCHAR(255) NOT NULL,
description varchar(255) NOT NULL,
status varchar(255) NOT NULL,
start_time TIMESTAMP,
end_time TIMESTAMP,
is_break boolean DEFAULT FALSE,
PRIMARY KEY (pipeline_id)
);

DROP TABLE IF EXISTS task;

CREATE TABLE task (
  task_id BIGSERIAL NOT NULL,
  pipeline_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  action VARCHAR(255) NOT NULL,
  description varchar(255) NOT NULL,
  status varchar(255) NOT NULL,
  start_time TIMESTAMP,
  end_time TIMESTAMP,
  PRIMARY KEY (task_id)
);