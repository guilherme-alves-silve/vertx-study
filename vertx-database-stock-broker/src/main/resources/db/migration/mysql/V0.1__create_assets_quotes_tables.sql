CREATE TABLE assets (
  name VARCHAR(12) PRIMARY KEY
);

CREATE TABLE quotes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    bid NUMERIC,
    ask NUMERIC,
    last_price NUMERIC,
    volume NUMERIC,
    asset VARCHAR(12),
    FOREIGN KEY (asset) REFERENCES assets(name)
);
