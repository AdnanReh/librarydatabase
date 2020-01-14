DROP TABLE reservation;
DROP TABLE fine;
DROP TABLE book;
DROP TABLE publisher;
DROP TABLE borrows;
DROP TABLE bookClub;
DROP TABLE member;

CREATE TABLE member(
    mem_id VARCHAR(25) PRIMARY KEY,
    mem_name VARCHAR(25) NOT NULL,
    mem_age INTEGER CHECK (mem_age > -1)
    );

CREATE TABLE publisher(
    pub_name VARCHAR(25) PRIMARY KEY,
    pub_location VARCHAR(25)
    );

CREATE TABLE book(
    isbn VARCHAR(15) PRIMARY KEY,
    b_author VARCHAR(25) NOT NULL,
    b_title VARCHAR(25) NOT NULL,
    b_genre VARCHAR(25) NOT NULL,
    b_count INTEGER CHECK (b_count > -1),
    b_minAge INTEGER,
    pub_name VARCHAR(25) REFERENCES publisher(pub_name) ON DELETE CASCADE,
    pub_location VARCHAR(25) NOT NULL,
    pub_date DATE
    );

CREATE TABLE reservation(
    isbn VARCHAR(15) REFERENCES book(isbn) ON DELETE CASCADE,
    mem_id VARCHAR(25) REFERENCES member(mem_id) ON DELETE CASCADE
    );
    
CREATE TABLE borrows(
    b_date DATE NOT NULL,
    d_date DATE NOT NULL,
    r_date DATE NOT NULL
    );

CREATE TABLE fine(
    mem_id VARCHAR(25) REFERENCES member(mem_id) ON DELETE CASCADE,
    isbn VARCHAR(25) REFERENCES book(isbn) ON DELETE CASCADE,
    f_rate NUMBER(5,2),
    /* r_date DATE, */
    /* d_date DATE NOT NULL, */
    f_amount FLOAT 
    );

CREATE TABLE bookClub(
    start_date DATE,
    end_date DATE,
    mem_name VARCHAR(25) NOT NULL,
    b_title VARCHAR(25),
    meeting_date VARCHAR(25)
    );
    
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (123, 'John', 25);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (456, 'Sue', 13);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (789, 'Mary', 65);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (101, 'Albert', 5);
INSERT INTO member(mem_id, mem_name, mem_age) VALUES (098, 'Jim', NULL);

INSERT INTO publisher(pub_name, pub_location) VALUES ('Scholastic','New York');
INSERT INTO publisher(pub_name, pub_location) VALUES ('Gollancz','Moscow');
INSERT INTO publisher(pub_name, pub_location) VALUES ('Bantam Spectra','New York');
INSERT INTO publisher(pub_name, pub_location) VALUES ('George Allen Unwin','London');

INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (1111, 'Edith Wharton', 'House of Mirth', 'novel', 5, 0,'Scholastic','New York',TO_DATE('1905/10/14','yyyy/mm/dd')); 
INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (2222, 'Dmitry Glukhovsky', 'Metro 2033', 'science fiction novel', 2, 13,'Gollancz','Moscow',TO_DATE('2010/03/18','yyyy/mm/dd')); 
INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (3333, 'J.R.R Toilken', 'The Hobbit', 'fantasy novel', 10, 0,'George Allen Unwin','London',TO_DATE('1937/09/21','yyyy/mm/dd')); 
INSERT INTO book(isbn, b_author, b_title, b_genre, b_count, b_minAge, pub_name, pub_location, pub_date) VALUES (4444, 'George R. R. Martin', 'A Game of Thrones', 'fantasy novel', 3, 0,'Scholastic','New York',TO_DATE('1996/08/01','yyyy/mm/dd'));

INSERT INTO fine(mem_id, f_rate, f_amount) VALUES (123, 1.025, 143.00);
INSERT INTO fine(mem_id, f_rate, f_amount) VALUES (456, 1.025, 0.00);

INSERT INTO bookClub(mem_name, b_title, start_date, end_date, meeting_date) VALUES ('John', 'The Life of Pie', TO_DATE('2018/06/12','YYYY/MM/DD'), TO_DATE('2019/06/14','YYYY/MM/DD'), 'THU');
INSERT INTO bookClub(mem_name, b_title, start_date, end_date, meeting_date) VALUES ('Sue', 'The Hobbit', TO_DATE('2019/12/01', 'YYYY/MM/DD'), TO_DATE('2021/01/23','YYYY/MM/DD'), 'WED');
INSERT INTO bookClub(mem_name, b_title, start_date, end_date, meeting_date) VALUES ('Mary', 'The Hobbit', TO_DATE('2020/03/20','YYYY/MM/DD'), TO_DATE('2022/09/19','YYYY/MM/DD'), 'FRI');
INSERT INTO bookClub(mem_name, b_title, start_date, end_date, meeting_date) VALUES ('Albert', 'The Hobbit', TO_DATE('2019/10/10', 'YYYY/MM/DD'), TO_DATE('2020/06/09','YYYY/MM/DD'), 'SUN');

/* QUERIES(7) */

/* 1. members signed up for the hobbit book club */
SELECT mem_name AS The_Hobbit_Book_Club_Members
FROM bookClub
WHERE b_title = 'The Hobbit';

/* 2. books published by Scholastic */
SELECT book.b_title AS Scholastic_Books
FROM book
WHERE pub_name = 'Scholastic';

/* 3. Member name and name of book that is reserved */
SELECT member.mem_name, book.b_title
FROM member,book,reservation
WHERE reservation.mem_id = member.mem_id
AND reservation.isbn = book.isbn;

/* 4. fantasy novel books in stock */
SELECT book.b_author, book.b_title
FROM book
WHERE book.b_genre = 'fantasy novel';

/* 5. List all members with fines, in descending order */
SELECT member.mem_name, member.mem_id, fine.f_amount AS PEOPLE_OWING_FINES
FROM member, fine
WHERE f_amount > 0
ORDER BY f_amount DESC;

/* 6. List all American publishers */
SELECT publisher.pub_name AS American_Publishers
FROM publisher
WHERE publisher.pub_location = 'New York' OR publisher.pub_location = 'Los Angeles';

/* 7. books by publishers in moscow */
SELECT publisher.pub_name,book.b_title AS Russian_Books
FROM book,publisher
WHERE book.pub_location = 'Moscow'
AND book.pub_location = publisher.pub_location;

SELECT * 
FROM member
INNER JOIN bookClub ON 
    bookClub.mem_name = member.mem_name;
    
SELECT *
FROM book 
INNER JOIN publisher ON 
    publisher.pub_location = book.pub_location;
    
SELECT COUNT(*)
FROM book 
INNER JOIN publisher ON 
    publisher.pub_location = book.pub_location;
    
SELECT pub_name, COUNT(pub_name)
FROM publisher
GROUP BY pub_name;

/* ADVANCED QUERIES */

/* 1. joining publisher and book tables */
SELECT *
FROM book
INNER JOIN publisher ON
	publisher.pub_location = book.pub_location;

/* 2. number of books published by each publisher */
SELECT publisher.pub_name, COUNT(book.pub_name)
FROM book,publisher
WHERE book.pub_name = publisher.pub_name
AND book.pub_location = publisher.pub_location
GROUP BY publisher.pub_name;

 /* 3. average age of members in the hobbit book club */
SELECT AVG(member.mem_age) AS AverageAgeOfTheHobbitBookClub
FROM bookClub, member
WHERE bookClub.b_title = 'The Hobbit'
AND bookClub.mem_name = member.mem_name;

/* 4. members who don't owe fines */
SELECT member.mem_name AS SafeMembers
FROM member
	WHERE NOT EXISTS
	(SELECT *
 	FROM fine
 	WHERE fine.mem_id = member.mem_id);

/* 5. number of books published in each city, sorted alphabetically */
SELECT pub_location, COUNT(pub_location)
FROM book
GROUP BY pub_location
ORDER BY pub_location;

/* 6. teenage members of the library */
SELECT mem_name, mem_age
FROM member
WHERE mem_age BETWEEN 13 AND 18;

/* 7. Information about money owed */
SELECT SUM(f_amount), MAX(f_amount), MIN(f_amount) AS FineInfo
FROM fine;


/* VIEWS(2) */

/* 1. A view about being able to check out books if members fine is below a certain amount */
/* CREATE VIEW can_check_out_books AS */

/* 2. A view about something else */
SELECT * FROM USER_CONSTRAINTS WHERE TABLE_NAME = "member";
SELECT * FROM MEMBER WHERE mem_name = 'adnan';
