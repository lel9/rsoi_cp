CREATE DATABASE statistic_service WITH TEMPLATE = template0 ENCODING = 'UTF8';

CREATE TABLE IF NOT EXISTS operations (
  id UUID PRIMARY KEY,
  entity_name VARCHAR(255) NOT NULL,
  parent_entity_name VARCHAR(255),
  entity_id VARCHAR(255) NOT NULL,
  parent_entity_id VARCHAR(255),
  operation_name CHAR,
  date bigint
);