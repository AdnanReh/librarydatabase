DROP TABLE publisher;
DROP TABLE reservation;
DROP TABLE fine;
DROP TABLE borrows;
DROP TABLE bookClub;
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
    b_minAge INTEGER,
    pub_name VARCHAR(25) UNIQUE,
    pub_location VARCHAR(25) NOT NULL,
    pub_date DATE
    );
    
CREATE TABLE publisher(
    pub_name VARCHAR(25),
    pub_location VARCHAR(25)
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
    isbn VARCHAR(25) REFERENCES book(isbn),
    f_rate NUMBER(5,2) CHECK (f_rate > 0.00 AND f_rate < 100.00),
    r_date DATE,
    d_date DATE NOT NULL,
    f_amount NUMBER CHECK (f_amount >= 0)
    );

CREATE TABLE bookClub(
    mem_name VARCHAR(25) NOT NULL,
    b_title VARCHAR(25),
    meeting_date DATE
    );
    
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (123, 'John', 25);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (456, 'Sue', 13);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (789, 'Mary', 65);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (101, 'Albert', 5);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (098, 'Jim', NULL);

INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (1111, 'Edith Wharton', 'House of Mirth', 'novel', 5, 0,'Scholastic','New York',TO_DATE('1905/10/14','yyyy/mm/dd')); 
INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (2222, 'Dmitry Glukhovsky', 'Metro 2033', 'science fiction novel', 2, 13,'Gollancz','Moscow',TO_DATE('2010/03/18','yyyy/mm/dd')); 
INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (3333, 'J.R.R Toilken', 'The Hobbit', 'fantasy novel', 10, 0,'George Allen & Unwin','London',TO_DATE('1937/09/21','yyyy/mm/dd')); 
INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (1111, 'George R. R. Martin', 'A Game of Thrones', 'fantasy novel', 3, 0,'Bantam Spectra','New York',TO_DATE('1996/08/01','yyyy/mm/dd'));

INSERT INTO fine(mem_id, isbn, f_rate, r_date, d_date, f_amount) VALUES (123, 1111, 2.5, TO_DATE('2019/04/05','YYYY/MM/DD'), TO_DATE('2019/03/04','YYYY/MM/DD'), f_amount*(r_date-d_date));
INSERT INTO fine(mem_id, isbn, f_rate, r_date, d_date, f_amount) VALUES (123, 1111, 2.5, TO_DATE('2019/10/22','YYYY/MM/DD'), TO_DATE('2019/10/03','YYYY/MM/DD'), f_amount*(r_date-d_date));

INSERT INTO publisher(pub_name, pub_location) VALUES ('Scholastic','New York');
INSERT INTO publisher(pub_name, pub_location) VALUES ('Gollancz','Moscow');
INSERT INTO publisher(pub_name, pub_location) VALUES ('Bantam Spectra','New York');
INSERT INTO publisher(pub_name, pub_location) VALUES ('George Allen Unwin','London');


