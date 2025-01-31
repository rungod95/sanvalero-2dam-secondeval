-- Director table creation
CREATE TABLE directors (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    awarded BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT PK_directors PRIMARY KEY (id)
);

-- Film table creation
CREATE TABLE films (
    id BIGINT AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,  -- Duration in minutes
    viewed BOOLEAN NOT NULL DEFAULT FALSE,
    director_id BIGINT NOT NULL,  -- Director relationship

    CONSTRAINT PK_films PRIMARY KEY (Id),
    CONSTRAINT FK_films_directors FOREIGN KEY (director_id) REFERENCES directors(id) ON DELETE CASCADE
);