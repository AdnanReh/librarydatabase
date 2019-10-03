DROP TABLE publisher;
DROP TABLE reservation;
DROP TABLE fine;
DROP TABLE borrows;
DROP TABLE book;
DROP TABLE member;

CREATE TABLE member(
    mem_id VARCHAR(25) PRIMARY KEY,
    mem_name VARCHAR(25) NOT NULL,
    mem_age INTEGER CHECK (mem_age > -1)
    );

CREATE TABLE book(
    isbn VARCHAR(15) PRIMARY KEY,
    b_author VARCHAR(25) NOT NULL,
    b_title VARCHAR(25) NOT NULL,
    b_genre VARCHAR(25) NOT NULL,
    b_count INTEGER CHECK (b_count > -1),
    pub_name VARCHAR(25) UNIQUE,
    pub_location VARCHAR(25) NOT NULL,
    pub_date DATE
    );
    
CREATE TABLE publisher(
    pub_name VARCHAR(25),
    pub_location VARCHAR(25),
    pub_date DATE
    );

CREATE TABLE reservation(
    isbn VARCHAR(15) REFERENCES book(isbn),
    mem_id VARCHAR(25) REFERENCES member(mem_id)
    );
    
CREATE TABLE borrows(
    b_date DATE NOT NULL,
    d_date DATE NOT NULL,
    r_date DATE NOT NULL
    );

CREATE TABLE fine(
    mem_id VARCHAR(25) REFERENCES member(mem_id),
    f_rate NUMBER(5,2) CHECK (f_rate > 0.00 AND f_rate < 100.00),
    r_date DATE,
    d_date DATE,
    f_amount NUMBER CHECK (f_amount >= 0)
    );


