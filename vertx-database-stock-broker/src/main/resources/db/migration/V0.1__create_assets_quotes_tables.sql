CREATE TABLE assets (
  name VARCHAR(12) PRIMARY KEY
);

CREATE TABLE quotes (
    id SERIAL PRIMARY KEY,
    bid DECIMAL(10, 2),
    ask DECIMAL(10, 2),
    last_price DECIMAL(10, 2),
    volume DECIMAL(10, 2),
    asset VARCHAR(12),
    FOREIGN KEY (asset) REFERENCES assets(name),
    CONSTRAINT last_price_is_positive CHECK(last_price > 0),
    CONSTRAINT volume_is_positive_or_zero CHECK(volume >= 0)
);
