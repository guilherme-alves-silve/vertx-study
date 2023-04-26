INSERT INTO broker.assets(name)
VALUES ('AAPL'), ('AMZN'), ('FB'), ('GOOG'), ('MSFT'), ('NFLX'), ('TSLA')
ON CONFLICT(name)
DO NOTHING;

INSERT INTO broker.quotes (bid, ask, last_price, volume, asset)
VALUES
	 (1.00, 2.00, 3.00, 4.00, 'AMZN'),
	 (3.00, 3.00, 3.00, 3.00, 'MSFT'),
	 (5.00, 6.00, 7.00, 4.00, 'NFLX');
