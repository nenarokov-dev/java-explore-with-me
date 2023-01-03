DROP TABLE IF EXISTS endpoint_hits;

CREATE TABLE IF NOT EXISTS endpoint_hits
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    app        VARCHAR(256)                            NOT NULL,
    uri        VARCHAR(256)                            NOT NULL,
    ip         VARCHAR(156)                            NOT NULL,
    time_stamp TIMESTAMP                          NOT NULL
);
