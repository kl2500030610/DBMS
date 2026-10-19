CREATE TABLE UserMaster (
	id SERIAL PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	dob DATE,
	department VARCHAR(100)
);


INSERT INTO UserMaster (name, dob, department)
VALUES 
('Ravi Kumar', '2000-05-15', 'Computer Science'),
('Anjali Sharma', '1999-08-22', 'Electronics'),
('Suresh Reddy', '2001-01-10', 'Mechanical'),
('Priya Singh', '2000-12-05', 'Civil');

SELECT * FROM UserMaster