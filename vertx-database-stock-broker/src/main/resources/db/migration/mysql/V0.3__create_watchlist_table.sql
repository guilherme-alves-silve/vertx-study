CREATE TABLE watchlist (
  account_id VARCHAR(255) NOT NULL,
  asset VARCHAR(12) NOT NULL,
  FOREIGN KEY(asset) REFERENCES assets(name),
  PRIMARY KEY(account_id, asset)
);
