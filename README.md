# 根据ISBN获取图书封面图片

## 一、描述
### 1.输入
含有ISBN的数据表；
数据库表为：
```$xslt
MariaDB [LIBSYS]> desc ASORD_MARC;
+-------------------+--------------+------+-----+---------+-------+
| Field             | Type         | Null | Key | Default | Extra |
+-------------------+--------------+------+-----+---------+-------+
| M_ISBN            | varchar(40)  | YES  |     | NULL    |       |
+-------------------+--------------+------+-----+---------+-------+
```
### 输出
保存表中所有记录对应的封面图片的URL，以如下格式保存在表中；
```$xslt
+--------+-------------+------+-----+---------+----------------+
| Field  | Type        | Null | Key | Default | Extra          |
+--------+-------------+------+-----+---------+----------------+
| ID     | int(11)     | NO   | PRI | NULL    | auto_increment |
| M_ISBN | varchar(40) | YES  |     | NULL    |                |
| URL    | varchar(60) | YES  |     | NULL    |                |
+--------+-------------+------+-----+---------+----------------+

```

## 二、步骤
## 1.建库
```
CREATE DATABASE `LIBSYS` DEFAULT CHARACTER SET utf8mb4;
```
## 2.建表
```
CREATE TABLE LIBSYS.IMAGE(ID INT AUTO_INCREMENT PRIMARY KEY,M_ISBN VARCHAR(40),URL VARCHAR(60));
```
## 3.准备txt文件
准备txt文件，每行为ISBN号，如：
```
7-81053-340-1
7-5628-0931-3
7-5628-0924-0
7-5639-0961-3
7-81053-270-7
7-81045-867-1
7-81048-411-7
7-81045-831-0
7-81045-865-5
7-81053-339-8
```
## 4.运行程序
```
java -jar crawler-1.0.jar DB_IP USER PASSWD ISBN_FILE AMOUNT_OF_THREADS"
```
需要指定参数为
* DB_IP：数据库地址
* USER：数据库用户名
* PASSWD：密码
* ISBN_FILE：文本文件路径
* AMOUNT_OF_THREADS：线程数
例如：
```
java -jar crawler-1.0.jar localhost:3306 root 123456 isbn.txt 4
```

## 三、参考文献
* http://docs.oracle.com/javase/8/docs/api/
* https://www.zhihu.com/question/21234530
* http://blog.csdn.net/hu948162999/article/details/40372383?utm_source=tuicool&utm_medium=referral
* http://blog.csdn.net/ku360517703/article/details/41907319
* http://www.infoq.com/cn/news/2011/06/xxb-maven-9-package
