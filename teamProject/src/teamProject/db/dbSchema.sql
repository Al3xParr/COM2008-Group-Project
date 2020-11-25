DROP TABLE IF EXISTS StudentsToModules;
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
    salt CHAR(16) NOT NULL,
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
    regNum INT NOT NULL,
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
    FOREIGN KEY (courseCode) REFERENCES Course(courseCode) ON DELETE SET NULL
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
    FOREIGN KEY (deptCode) REFERENCES Departments(deptCode) ON DELETE SET NULL
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
    degreeLvl TINYINT NOT NULL,
    CONSTRAINT id PRIMARY KEY (moduleCode, courseCode),
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

CREATE TABLE StudentsToModules(
    regNum INT NOT NULL,
    moduleCode CHAR(7) NOT NULL,
    mark DECIMAL(5,4),
    resitMark DECIMAL(5,4),
    label CHAR(1) NOT NULL,
    courseCode CHAR(6) NOT NULL,
    degreeLvl TINYINT NOT NULL,
    CONSTRAINT id PRIMARY KEY (regNum, moduleCode),
    FOREIGN KEY (regNum) REFERENCES Students(regNum) ON DELETE CASCADE,
    FOREIGN KEY (moduleCode) REFERENCES Modules(moduleCode) ON DELETE CASCADE
);

CREATE TABLE Teacher(
    username VARCHAR(50) NOT NULL,
    fullName VARCHAR(50) NOT NULL,
    PRIMARY KEY (username),
    FOREIGN KEY (username) REFERENCES Accounts(username) ON DELETE CASCADE
);
