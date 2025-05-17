-- Insert Directors
INSERT INTO directors (id, name, last_name, birth_date, nationality, awarded) VALUES
  (1, 'Quentin',        'Tarantino',     '1963-03-27', 'USA',   TRUE),
  (2, 'Hayao',          'Miyazaki',      '1941-01-05', 'Japan', TRUE),
  (3, 'Greta',          'Gerwig',        '1983-08-04', 'USA',   FALSE),
  (4, 'Christopher',    'Nolan',         '1970-07-30', 'UK',    FALSE),
  (5, 'Martin',         'Scorsese',      '1942-11-17', 'USA',   TRUE),
  (6, 'Sofia',          'Coppola',       '1971-05-14', 'USA',   TRUE),
  (7, 'Guillermo',      'del Toro',      '1964-10-09', 'Mexico',TRUE),
  (8, 'Kathryn',        'Bigelow',       '1951-11-27', 'USA',   TRUE);

-- Insert Films
INSERT INTO films (id, title,               genre,         release_date, duration, viewed, director_id) VALUES
  ( 1, 'Pulp Fiction',     'Crime',        '1994-10-14',    154,     TRUE,  1),
  ( 2, 'Spirited Away',    'Animation',    '2001-07-20',    125,     TRUE,  2),
  ( 3, 'Little Women',     'Drama',        '2019-12-25',    135,     FALSE, 3),
  ( 4, 'Inception',        'Sci-Fi',       '2010-07-16',    148,     TRUE,  4),
  ( 5, 'The Departed',     'Thriller',     '2006-10-06',    151,     TRUE,  5),
  ( 6, 'Lost in Translation','Drama',      '2003-09-12',    102,     FALSE, 6),
  ( 7, 'Pan’s Labyrinth',  'Fantasy',      '2006-10-11',    118,     TRUE,  7),
  ( 8, 'The Hurt Locker',  'War',          '2008-06-26',    131,     TRUE,  8);
