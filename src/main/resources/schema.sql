CREATE TABLE IF NOT EXISTS package
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    type           VARCHAR NOT NULL,
    postal_code    VARCHAR NOT NULL,
    address        VARCHAR NOT NULL,
    recipient_name VARCHAR
);

CREATE TABLE IF NOT EXISTS office
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR NOT NULL,
    address VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS history
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status     VARCHAR NOT NULL,
    date_time  TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    office_id  BIGINT REFERENCES office (id) ON DELETE CASCADE,
    package_id BIGINT REFERENCES package (id) ON DELETE CASCADE
);

-- добавляем пару почтовых офисов
INSERT INTO office (name, address)
VALUES ('Great Mail', 'Russia, Moscow'),
       ('Turtle Mail', 'Russia, Moscow City');