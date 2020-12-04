/**
 * Team Project COM2008 year 20/21
 * @author Nathan Mitchell
 * @author Alex Parr
 * @author Julia Jablonska
 * @author Zbigniew Lisak 
 */
DROP TABLE IF EXISTS Grades;
DROP TABLE IF EXISTS StudyPeriods;
DROP TABLE IF EXISTS ModulesToCourse;
DROP TABLE IF EXISTS CourseToDepartment;
DROP TABLE IF EXISTS Teacher;
DROP TABLE IF EXISTS BachEquiv;
DROP TABLE IF EXISTS Modules;
DROP TABLE IF EXISTS Students;
DROP TABLE IF EXISTS Departments;
DROP TABLE IF EXISTS Course;
DROP TABLE IF EXISTS Accounts;

CREATE TABLE Accounts(
    username VARCHAR(50) NOT NULL,
    passwordHash CHAR(64) NOT NULL,
    salt CHAR(32) NOT NULL,
    accessLvl TINYINT NOT NULL,
    PRIMARY KEY (username)    
);

CREATE TABLE Course(
    courseCode CHAR(6) NOT NULL,
    fullName VARCHAR(50) NOT NULL,
    yearInIndustry BOOLEAN NOT NULL,
    PRIMARY KEY (courseCode)
);

CREATE TABLE Students(
    regNum INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(3),
    surname VARCHAR(25),
    forenames VARCHAR(25),
    email VARCHAR(50),
    username VARCHAR(50) NOT NULL,
    tutor VARCHAR(50),
    courseCode CHAR(6),
    degreeLvl CHAR(1),
    PRIMARY KEY (regNum),
    FOREIGN KEY (username) REFERENCES Accounts(username) ON DELETE CASCADE,
    FOREIGN KEY (courseCode) REFERENCES Course(courseCode) ON DELETE CASCADE
);

CREATE TABLE Departments(
    deptCode CHAR(3) NOT NULL,
    fullName VARCHAR(50) NOT NULL,
    PRIMARY KEY (deptCode)
);

CREATE TABLE Modules(
    moduleCode CHAR(7) NOT NULL,
    fullName VARCHAR(50) NOT NULL,
    deptCode CHAR(3) ,
    timeTaught VARCHAR(25) NOT NULL,
    PRIMARY KEY (moduleCode),
    FOREIGN KEY (deptCode) REFERENCES Departments(deptCode) ON DELETE CASCADE
);

CREATE TABLE BachEquiv(
    courseCode CHAR(6) NOT NULL,
    bachEquiv CHAR(6) ,
    PRIMARY KEY (courseCode),
    FOREIGN KEY (courseCode) REFERENCES Course(courseCode) ON DELETE CASCADE,
    FOREIGN KEY (bachEquiv) REFERENCES Course(courseCode) ON DELETE SET NULL
);

CREATE TABLE ModulesToCourse(
    moduleCode CHAR(7) NOT NULL,
    courseCode CHAR(6) NOT NULL,
    core BOOLEAN NOT NULL,
    degreeLvl CHAR(1) NOT NULL,
    CONSTRAINT id PRIMARY KEY (moduleCode, courseCode, degreeLvl),

    FOREIGN KEY (moduleCode) REFERENCES Modules(moduleCode) ON DELETE CASCADE,
    FOREIGN KEY (courseCode) REFERENCES Course(courseCode) ON DELETE CASCADE
);

CREATE TABLE CourseToDepartment(
    courseCode CHAR(6) NOT NULL,
    deptCode CHAR(3) NOT NULL,
    mainDept BOOLEAN NOT NULL,
    CONSTRAINT id PRIMARY KEY (courseCode, deptCode),
    FOREIGN KEY (courseCode) REFERENCES Course(courseCode) ON DELETE CASCADE,
    FOREIGN KEY (deptCode) REFERENCES Departments(deptCode) ON DELETE CASCADE
);

CREATE TABLE StudyPeriods(
    regNum INT NOT NULL,
    label CHAR(1) NOT NULL,
    courseCode CHAR(6) NOT NULL,
    degreeLvl TINYINT NOT NULL,
    startDate DATE NOT NULL,
    endDate DATE NOT NULL,
    PRIMARY KEY(regNum,label),
    FOREIGN KEY (regNum) REFERENCES Students(regNum) ON DELETE CASCADE
);

CREATE TABLE Grades(
    regNum INT NOT NULL,
    label CHAR(1) NOT NULL,
    moduleCode CHAR(7) NOT NULL,
    mark DECIMAL(5,2),
    resitMark DECIMAL(5,2),
    CONSTRAINT id PRIMARY KEY (regNum, moduleCode, label),
    FOREIGN KEY (regNum,label) REFERENCES StudyPeriods(regNum,label) ON DELETE CASCADE,
    FOREIGN KEY (moduleCode) REFERENCES Modules(moduleCode) ON DELETE CASCADE
);

CREATE TABLE Teacher(
    username VARCHAR(50) NOT NULL,
    fullName VARCHAR(50) NOT NULL,
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES Accounts(username) ON DELETE CASCADE
);
