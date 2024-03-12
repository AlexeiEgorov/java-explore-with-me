CREATE TABLE IF NOT EXISTS users (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name VARCHAR(250),
email VARCHAR(254),
CONSTRAINT email_uniqueness UNIQUE(email)
);

CREATE TABLE IF NOT EXISTS categories (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
name VARCHAR(50),
CONSTRAINT uq_category_name UNIQUE(name)
);

CREATE TABLE IF NOT EXISTS events (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
annotation VARCHAR(2000),
category_id BIGINT REFERENCES categories (id) ON DELETE RESTRICT,
created_on TIMESTAMP WITHOUT TIME ZONE,
confirmed_requests INT,
description VARCHAR(7000),
event_date TIMESTAMP WITHOUT TIME ZONE,
initiator_id INT REFERENCES users (id) ON DELETE CASCADE,
lat DOUBLE PRECISION,
lon DOUBLE PRECISION,
paid BOOLEAN,
participant_limit INT,
published_on TIMESTAMP WITHOUT TIME ZONE,
request_moderation BOOLEAN,
state VARCHAR(20),
title VARCHAR(120)
);

CREATE TABLE IF NOT EXISTS event_requests (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
created TIMESTAMP WITHOUT TIME ZONE,
event_id BIGINT REFERENCES events (id) ON DELETE CASCADE,
requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
status VARCHAR(20),
CONSTRAINT uq_request UNIQUE(event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
title VARCHAR(50),
pinned BOOLEAN
);

CREATE TABLE IF NOT EXISTS compilation_events (
compilation_id BIGINT REFERENCES compilations (id) ON DELETE CASCADE,
event_id BIGINT REFERENCES events (id),
PRIMARY KEY (compilation_id, event_id)
);

--CREATE TABLE IF NOT EXISTS requests (
--id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--description VARCHAR(1024),
--requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
--created TIMESTAMP WITHOUT TIME ZONE
--);

--CREATE TABLE IF NOT EXISTS items (
--id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--name VARCHAR(255),
--description VARCHAR(2048),
--available BOOLEAN,
--owner_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
--request_id BIGINT REFERENCES requests (id) ON DELETE CASCADE
--);
--
--CREATE TABLE IF NOT EXISTS bookings (
--id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--start TIMESTAMP WITHOUT TIME ZONE,
--end_b TIMESTAMP WITHOUT TIME ZONE,
--item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
--booker_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
--status INT
--);
--
--CREATE TABLE IF NOT EXISTS comments (
--id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
--text VARCHAR(1024),
--item_id BIGINT REFERENCES items (id) ON DELETE CASCADE,
--author_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
--created TIMESTAMP WITHOUT TIME ZONE
--);