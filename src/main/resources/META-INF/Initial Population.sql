use ejb3;

INSERT INTO user
(ID, EMAIL, FIRSTNAME, LASTNAME, PASSWORD, USERNAME, WHOCREATED)
VALUES
('1', 'e-mail', 'name', 'surname', '12345', 'admin','admin');

INSERT INTO user
(ID, EMAIL, FIRSTNAME, LASTNAME, PASSWORD, USERNAME, WHOCREATED, EDUCATIONINSTITUTION,  COURSE, STUDYGROUP)
VALUES
('2', 'e-mail', 'name', 'surname', '123', 'user1','admin','institute','3','20a'),
('3', 'e-mail', 'name', 'surname', '12', 'user2','admin','institute','4','20b'),
('4', 'e-mail', 'name', 'surname', '123', 'user3','admin','institute','4','20b');

INSERT INTO ROLE
(`ID`, `ROLEDESC`, `ROLENAME`)
VALUES
('1', 'Administrator', 'Administrators'),
('2', 'Manager', 'Managers'),
('3', 'User', 'Users');

INSERT INTO user_roles
(Role_roleid, User_userid)
VALUES
('1', '1'),
('2', '2'),
('3', '3'),
('3', '4');
