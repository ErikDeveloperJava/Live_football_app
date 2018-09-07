create database live_football character set utf8 collate utf8_general_ci;

use live_football;

create table user(
  id int not null auto_increment primary key ,
  name varchar(255) not null ,
  username varchar(255) not null ,
  password varchar(255) not null ,
  img_url varchar(255) not null ,
  role varchar(255) not null
)engine InnoDB character set utf8 collate utf8_general_ci;

create table league(
  id int not null auto_increment primary key ,
  name varchar(255) not null ,
  description text not null ,
  club_count int ,
  img_url varchar(255) not null
)engine InnoDB character set utf8 collate utf8_general_ci;

create table club(
  id int not null auto_increment primary key ,
  name varchar(255) not null ,
  description text not null ,
  trainer varchar(255) not null ,
  stadium varchar(255) not null ,
  owner varchar(255) not null,
  league_id int not null ,
  img_url varchar(255) not null ,
  is_champion boolean not null ,
  foreign key (league_id) references league(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table league_table(
  id int not null auto_increment primary key ,
  played int not null ,
  won int not null ,
  drawn int not null ,
  lost int not null ,
  points int not null ,
  club_id int not null ,
  foreign key (club_id) references club(id)on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table player(
  id int not null auto_increment primary key ,
  name varchar(255) not null ,
  surname varchar(255) not null ,
  nationality varchar(255) not null ,
  birth_date DATETIME not null ,
  age int not null ,
  position varchar(255) not null,
  img_url varchar(255) not null,
  club_id int not null ,
  foreign key (club_id) references club(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table matches(
  id int not null auto_increment primary key ,
  master int not null ,
  guest int not null ,
  date datetime not null ,
  account varchar(255),
  foreign key (master) references club(id)on delete cascade ,
  foreign key (guest) references club(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table news(
  id int not null auto_increment primary key ,
  title varchar(255) not null ,
  description text not null ,
  created_date datetime not null ,
  img_url varchar(255) not null ,
  video_url varchar(255) not null ,
  league_id int not null ,
  foreign key (league_id) references league(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table comment(
  id int not null auto_increment primary key,
  comment varchar(255) not null ,
  user_id int not null ,
  news_id int not null ,
  send_date datetime not null,
  foreign key (user_id) references user(id) on delete cascade ,
  foreign key (news_id) references news(id) on delete cascade
)engine InnoDB character set utf8 collate utf8_general_ci;

create table gallery(
  id int not null auto_increment primary key ,
  url varchar(255) not null
)engine InnoDB character set utf8 collate utf8_general_ci;

insert into user(name, username, password, img_url, role) VALUES
  ('Admin','admin','$2a$04$vJXF14SxHnOtDfiRm3uog.FjMCGH4HYmxbVZzTlsxlwWPYjdL49hO','','ADMIN');