# Game2048
2048
数据库：SQLite
建GameScore表sql：CREATE TABLE GameScore(id integer primary key autoincrement,username varchar(50) unique not null,gameScore int not null);
GameScore表结构：
                id  int  primary key
                username(游戏名)  varchar(50)  unique
                gameScore(分数)  int
