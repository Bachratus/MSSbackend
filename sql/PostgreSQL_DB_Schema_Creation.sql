-- create database mss_local_db with encoding 'UTF-8';

CREATE TABLE authority(
    name varchar(50) PRIMARY KEY,
    description varchar(70) NULL
);

CREATE TABLE users (
    id bigserial PRIMARY KEY,
    login varchar(50) NOT NULL UNIQUE,
    email varchar(191) NOT NULL UNIQUE,
    password_hash varchar(60) NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    active boolean DEFAULT true NOT NULL
);

CREATE TABLE user_authority (
    user_id bigint NOT NULL,
    authority_name varchar(50) NOT NULL,
    PRIMARY KEY (user_id, authority_name),
    FOREIGN KEY (authority_name) REFERENCES authority(name),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE department (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE user_department (
    user_id bigint NOT NULL,
    department_id bigint NOT NULL,
    supervisor boolean DEFAULT false NOT null,
    PRIMARY KEY (user_id, department_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (department_id) REFERENCES department(id)
);

CREATE TABLE project (
    id bigserial PRIMARY KEY,
    code varchar(255) NOT NULL,
    name varchar(255) NOT NULL,
    date_from date NULL,
    date_to date NULL,
    hours_predicted int NULL,
    max_hours int NULL
);

CREATE TABLE subproject_type (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL
);

CREATE TABLE subproject (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    date_from date NULL,
    date_to date NULL,
    subproject_type_id bigint NULL,
    project_id bigint NULL,
    hours_predicted int NULL,
    code varchar(255) NOT NULL,
    max_hours int NULL,
    FOREIGN KEY (project_id) REFERENCES project(id),
    FOREIGN KEY (subproject_type_id) REFERENCES subproject_type(id)
);

CREATE TABLE task (
    id bigserial PRIMARY KEY,
    name varchar(255) NOT NULL,
    from_date date NULL,
    to_date date NULL,
    subproject_id bigint NULL,
    hours_predicted int NULL,
    max_hours int NULL,
    FOREIGN KEY (subproject_id) REFERENCES subproject(id)
);

CREATE TABLE task_report (
    id bigserial PRIMARY KEY,
    status boolean DEFAULT false NOT NULL,
    description varchar(255) NULL,
    date date NOT NULL,
    hours float NOT NULL,
    task_id bigint NULL,
    user_id bigint NULL,
    FOREIGN KEY (task_id) REFERENCES task(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE default_task (
    user_id bigint NOT NULL,
    task_id bigint NOT NULL,
    date date NOT NULL,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (task_id) REFERENCES task(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
