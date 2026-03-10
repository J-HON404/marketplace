USE marketplace;

-- backend user profile
CREATE USER IF NOT EXISTS 'marketuser'@'%' IDENTIFIED BY 'marketpass';
GRANT ALL PRIVILEGES ON marketplace.* TO 'marketuser'@'%';

-- auth user profile
CREATE USER IF NOT EXISTS 'authuser'@'%' IDENTIFIED BY 'authpass';
GRANT SELECT, INSERT, UPDATE ON marketplace.profiles TO 'authuser'@'%';
GRANT SELECT, INSERT, UPDATE ON marketplace.shops TO 'authuser'@'%';

FLUSH PRIVILEGES;
