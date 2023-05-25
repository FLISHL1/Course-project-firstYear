CREATE TABLE Roles
(
  id int unsigned PRIMARY KEY AUTO_INCREMENT,
  name varchar(50)
);

CREATE TABLE Rights
(
  id int unsigned PRIMARY KEY AUTO_INCREMENT,
  name varchar(50)
);

CREATE TABLE Delivery_Center
(
  id int unsigned PRIMARY KEY AUTO_INCREMENT,
  name varchar(150),
  address varchar(150)
);

CREATE TABLE Users
(
  id int unsigned PRIMARY KEY AUTO_INCREMENT,
  name varchar(150) NOT NULL,
  number_phone varchar(25) NOT NULL,
  address varchar(150),
  id_dc int unsigned,
  FOREIGN KEY (id_dc) REFERENCES Delivery_Center (id)
);

CREATE TABLE Role_Right
(
  id int PRIMARY KEY AUTO_INCREMENT,
  id_role int unsigned,
  id_right int unsigned,
  FOREIGN KEY (id_role) REFERENCES Roles (id),
  FOREIGN KEY (id_right) REFERENCES Rights (id)
);

CREATE TABLE Role_User
(
  id int unsigned PRIMARY KEY AUTO_INCREMENT,
  id_user int unsigned,
  id_role int unsigned,
  FOREIGN KEY (id_user) REFERENCES Users (id),
  FOREIGN KEY (id_role) REFERENCES Roles (id)
);

CREATE TABLE Packs
(
  id serial PRIMARY KEY,
  type_d varchar(20) NOT NULL, --ENUM('Срочная', 'Обычная')
  weight int,
  user_from int unsigned,
  user_to int unsigned,
  id_dc_from int unsigned,
  id_courier int unsigned,
  FOREIGN KEY (user_from) REFERENCES Users (id),
  FOREIGN KEY (user_to) REFERENCES Users (id),
  FOREIGN KEY (id_dc_from) REFERENCES Delivery_Center (id),
  FOREIGN KEY (id_courier) REFERENCES Users (id)
);

CREATE TABLE States
(
  id int unsigned PRIMARY KEY AUTO_INCREMENT,
  name varchar(50)
);

CREATE TABLE Actions
(
  id serial PRIMARY KEY,
  id_pack int unsigned,
  id_state int unsigned,
  `date` date,
  FOREIGN KEY (id_pack) REFERENCES Packs (id),
  FOREIGN KEY (id_state) REFERENCES States (id)
);