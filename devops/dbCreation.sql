-- Director table creation
CREATE TABLE Director (
    id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    nationality VARCHAR(100) NOT NULL,
    awarded BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT PK_Director PRIMARY KEY (id)
);

-- Film table creation
CREATE TABLE Film (
    id INT AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL,  -- Duration in minutes
    viewed BOOLEAN NOT NULL DEFAULT FALSE,
    director_id INT NOT NULL,  -- Director relationship

    CONSTRAINT PK_Film PRIMARY KEY (Id),
    CONSTRAINT FK_Film_Director FOREIGN KEY (director_id) REFERENCES Director(id) ON DELETE CASCADE
);