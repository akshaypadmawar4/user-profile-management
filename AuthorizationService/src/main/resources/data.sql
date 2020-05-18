DROP TABLE IF EXISTS user;
 
CREATE TABLE user (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  password VARCHAR(250) NOT NULL
);
 
INSERT INTO user (id, username, password) VALUES
  (1, 'Akshay','$2y$12$5YYM0V6ipVks5DY7aBYVsORGra1pw5x5Iyn4r8xMomY6s9bIFJYMK'),
  (2, 'Sagar','$2y$12$i.i1UFP0A11hvAVOR4IxHORpx5UXwfGkfMFK6rrdTtP7dCZncz4wW'),
  (3, 'Vikas','$2y$12$7QNox7Y5IMBovlMP9e9oMeK934y3lCVbyVhgZ89QRi6X.ODBPWRrO');