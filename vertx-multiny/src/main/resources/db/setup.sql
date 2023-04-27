CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

INSERT INTO users(id, name) VALUES (1, 'Alice');
INSERT INTO users(id, name) VALUES (2, 'Bob');
INSERT INTO users(id, name) VALUES (3, 'Charlie');
