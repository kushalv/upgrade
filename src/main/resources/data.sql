DROP TABLE IF EXISTS reservation;

CREATE TABLE reservation (
                              id LONG AUTO_INCREMENT  PRIMARY KEY,
                              checkin_Date TIMESTAMP NOT NULL,
                              checkout_Date TIMESTAMP NOT NULL,
                              name VARCHAR(250) DEFAULT NOT NULL,
                              email VARCHAR(250) DEFAULT NOT NULL,
                              status VARCHAR(20) DEFAULT NOT NULL
);
