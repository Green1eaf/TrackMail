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
    date_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    office_id  BIGINT,
    package_id BIGINT,
    FOREIGN KEY (office_id) REFERENCES office (id) ON DELETE CASCADE,
    FOREIGN KEY (package_id) REFERENCES package (id) ON DELETE CASCADE
);

-- добавляем пару почтовых офисов
INSERT INTO office (name, address)
VALUES ('Great Mail', 'Russia, Moscow'),
       ('Turtle Mail', 'Russia, Moscow City');