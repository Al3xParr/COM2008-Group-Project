INSERT INTO Course VALUES("DEP100", "DEpartmentCourse1",false);
INSERT INTO Departments VALUES("DEP","Department1");
INSERT INTO Accounts VALUES("user1","passwordHash","passwordSalt",0);
INSERT INTO Students VALUES(001,"Mr","SUR","FORE NAMES","fnsur1@univ.com","user1","Teacher1","DEP100",1);
INSERT INTO Accounts VALUES("user2","passwordHash","passwordSalt",0);
INSERT INTO Students VALUES(002,"Mr","SUR","FORE NAMES","fnsur2@univ.com","user2","Teacher1","DEP100",2);
INSERT INTO Accounts VALUES("user3","passwordHash","passwordSalt",1);
INSERT INTO Teacher VALUES("user3", "Teacher1");
INSERT INTO Accounts VALUES("user4","passwordHash","passwordSalt",2);
INSERT INTO Accounts VALUES("user5","passwordHash","passwordSalt",3);
INSERT INTO Modules VALUES("DEP1001","Module1","DEP","SPRING");
INSERT INTO Modules VALUES("DEP1002","Module2","DEP","SPRING");
INSERT INTO Modules VALUES("DEP1003","Module3","DEP","SPRING");
INSERT INTO Modules VALUES("DEP1004","Module4","DEP","SPRING");
INSERT INTO CourseToDepartment VALUES("DEP100","DEP",true);
INSERT INTO ModulesToCourse VALUES("DEP1001","DEP100",true,1);
INSERT INTO ModulesToCourse VALUES("DEP1002","DEP100",false,1);
INSERT INTO ModulesToCourse VALUES("DEP1003","DEP100",false,1);
INSERT INTO ModulesToCourse VALUES("DEP1004","DEP100",true,2);

INSERT INTO StudentsToModules VALUES("001","DEP1001",16.0,84.0,"A","DEP100",1);
INSERT INTO StudentsToModules VALUES("001","DEP1002",70.0,null,"A","DEP100",1);
INSERT INTO StudentsToModules VALUES("001","DEP1003",null,null,"A","DEP100",1);

INSERT INTO StudentsToModules VALUES("002","DEP1001",16.0,32,"A","DEP100",1);
INSERT INTO StudentsToModules VALUES("002","DEP1002",90.0,null,"A","DEP100",1);
INSERT INTO StudentsToModules VALUES("002","DEP1003",67.0,null,"A","DEP100",1);

INSERT INTO StudentsToModules VALUES("002","DEP1001",90.0,null,"B","DEP100",1);
INSERT INTO StudentsToModules VALUES("002","DEP1002",90.0,null,"B","DEP100",1);
INSERT INTO StudentsToModules VALUES("002","DEP1003",67.0,null,"B","DEP100",1);

INSERT INTO StudentsToModules VALUES("002","DEP1004",null,null,"C","DEP100",2);





