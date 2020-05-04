CREATE DATABASE session_service WITH TEMPLATE = template0 ENCODING = 'UTF8';

CREATE TABLE IF NOT EXISTS oauth_client_details (
  client_id VARCHAR(255) PRIMARY KEY,
  resource_ids VARCHAR(255),
  client_secret VARCHAR(255),
  scope VARCHAR(255),
  authorized_grant_types VARCHAR(255),
  web_server_redirect_uri VARCHAR(255),
  authorities VARCHAR(255),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_client_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_access_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication_id VARCHAR(255) PRIMARY KEY,
  user_name VARCHAR(255),
  client_id VARCHAR(255),
  authentication BYTEA,
  refresh_token VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS oauth_refresh_token (
  token_id VARCHAR(255),
  token BYTEA,
  authentication BYTEA
);

CREATE TABLE IF NOT EXISTS oauth_code (
  code VARCHAR(255), authentication BYTEA
);

CREATE TABLE IF NOT EXISTS oauth_approvals (
    userId VARCHAR(255),
    clientId VARCHAR(255),
    scope VARCHAR(255),
    status VARCHAR(10),
    expiresAt TIMESTAMP,
    lastModifiedAt TIMESTAMP
);

CREATE TYPE role AS ENUM ('ROLE_USER', 'ROLE_EXPERT', 'ROLE_OPERATOR', 'ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS users (
  id UUID NOT NULL,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(100) NOT NULL,
  CONSTRAINT users_id_key PRIMARY KEY (id),
  CONSTRAINT username_unique UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS roles (
  id UUID NOT NULL,
  rolename VARCHAR(100) NOT NULL,
  CONSTRAINT roles_id_key PRIMARY KEY (id),
  CONSTRAINT rolename_unique UNIQUE (rolename)
);

CREATE TABLE IF NOT EXISTS user_roles (
  user_id UUID NOT NULL REFERENCES users(id),
  role_id UUID NOT NULL REFERENCES roles(id),
  CONSTRAINT roluser_unique UNIQUE (user_id, role_id)
);

INSERT INTO users(id, username, password)
VALUES('12412cdb-398f-4def-9cec-325b11968b56', 'olga', '$2a$10$bO0R953eHWoIFMBU8c.fpeAd9hFvyEI/nqXUBylhRciam01q6XSX2');

INSERT INTO roles(id, rolename)
VALUES('12412cdb-398f-4def-9cec-325b11968b57', 'ROLE_ADMIN');

INSERT INTO roles(id, rolename)
VALUES('12412cdb-398f-4def-9cec-325b11968b58', 'ROLE_USER');

INSERT INTO roles(id, rolename)
VALUES('12412cdb-398f-4def-9cec-325b11968b59', 'ROLE_OPERATOR');

INSERT INTO roles(id, rolename)
VALUES('12412cdb-398f-4def-9cec-325b11968b60', 'ROLE_EXPERT');

INSERT INTO user_roles(user_id, role_id)
VALUES('12412cdb-398f-4def-9cec-325b11968b56', '12412cdb-398f-4def-9cec-325b11968b57');

INSERT INTO oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types,
access_token_validity, refresh_token_validity, additional_information, autoapprove)
VALUES ('client', 'all', '$2a$10$Odvg916t3HI8m96htwghqufjg63XzYwFgzWC5g/KlI2whSWJZL7iq', 'all', 'password,refresh_token', 3600, 2592000, 'frontend app', 'true');