CREATE TABLE IF NOT EXISTS users
(
    id    INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254) UNIQUE                  NOT NULL,
    name  VARCHAR(250)                         NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) UNIQUE                   NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations
(
    id  INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat REAL                                 NOT NULL,
    lon REAL                                 NOT NULL,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                        NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    description        VARCHAR(7000)                        NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    paid               BOOLEAN                              NOT NULL,
    participant_limit  INTEGER                              NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                              NOT NULL,
    state              VARCHAR(255)                         NOT NULL,
    title              VARCHAR(120)                         NOT NULL,
    category_id        INT                                  NOT NULL,
    user_id            INT                                  NOT NULL,
    location_id        INT                                  NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created      TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    status       VARCHAR(255)                         NOT NULL,
    event_id     INT                                  NOT NULL,
    requester_id INT                                  NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    pinned BOOLEAN                              NOT NULL,
    title  VARCHAR(120)                         NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id INT,
    event_id       INT,
    FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id          INT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text        VARCHAR(2056)                        NOT NULL,
    event_id    INT                                  NOT NULL,
    user_id     INT                                  NOT NULL,
    create_time TIMESTAMP WITHOUT TIME ZONE          NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);