-- --------------------------------------------------
-- Set up variables
-- --------------------------------------------------
SET NAMES 'utf8mb4';
SET character_set_client = 'utf8mb4';
SET character_set_connection = 'utf8mb4';
SET character_set_results = 'utf8mb4';
SET collation_connection = 'utf8mb4_unicode_ci';

-- --------------------------------------------------
-- Table creation
-- --------------------------------------------------
-- Director table
CREATE TABLE IF NOT EXISTS directors (
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    last_name   VARCHAR(255) NOT NULL,
    birth_date  DATE        NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    awarded     BOOLEAN     NOT NULL DEFAULT FALSE,
    CONSTRAINT PK_directors PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Film table
CREATE TABLE IF NOT EXISTS films (
    id           BIGINT AUTO_INCREMENT,
    title        VARCHAR(255) NOT NULL,
    genre        VARCHAR(255) NOT NULL,
    release_date DATE        NOT NULL,
    duration     INT         NOT NULL,    -- in minutes
    viewed       BOOLEAN     NOT NULL DEFAULT FALSE,
    director_id  BIGINT      NOT NULL,    -- Foreign key to directors table
    CONSTRAINT PK_films PRIMARY KEY (id),
    CONSTRAINT FK_films_directors
      FOREIGN KEY (director_id)
      REFERENCES directors(id)
      ON DELETE CASCADE
) ENGINE=InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
