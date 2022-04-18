# Game2048

## 1、游戏简介

2048是一款休闲益智类的数字叠加小游戏。

## 2、游戏玩法

在 4*4 的16宫格中，您可以选择上、下、左、右四个方向进行操作，数字会按方向移动，相邻的两个数字相同就会合并，组成更大的数字，每次移动或合并后会自动增加一个数字。

当16宫格中没有空格子，且四个方向都无法操作时，游戏结束。 

## 3、数据库及表结构
SQLite数据库

### 建表sql：
         CREATE TABLE GameScore(
          id integer primary key autoincrement,
		      username varchar(50) unique not null,
		      gameScore int not null);

### GameScore表结构：
                id  int  primary key
                username(游戏名)  varchar(50)  unique
                gameScore(分数)  int
