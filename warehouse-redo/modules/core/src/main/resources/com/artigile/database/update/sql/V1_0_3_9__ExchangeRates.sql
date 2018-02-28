INSERT INTO ExchangeRate (fromCurrency_id, toCurrency_id, rate)
SELECT a.id, b.id, 1 FROM Currency a, Currency b
 WHERE NOT EXISTS (SELECT 1 FROM ExchangeRate c WHERE c.fromCurrency_id=a.id and c.toCurrency_id=b.id);