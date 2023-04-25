INSERT INTO broker.assets(name)
VALUES ('AAPL'), ('AMZN'), ('FB'), ('GOOG'), ('MSFT'), ('NFLX'), ('TSLA')
ON CONFLICT(name)
DO NOTHING;
