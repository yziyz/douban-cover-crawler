# 根据ISBN获取图书封面图片

## 一、描述
### 1.输入
含有ISBN的数据表；
数据库表为：
```$xslt
MariaDB [LIBSYS]> desc ASORD_MARC;
+-------------------+--------------+------+-----+---------+-------+
| Field    | Type| Null | Key | Default | Extra |
+-------------------+--------------+------+-----+---------+-------+
| M_ISBN   | varchar(40)  | YES  |     | NULL    |       |
+-------------------+--------------+------+-----+---------+-------+
```
### 输出
保存表中所有记录对应的封面图片的URL，以如下格式保存在表中；
```$xslt
+--------+-------------+------+-----+---------+----------------+
| Field  | Type        | Null | Key | Default | Extra |
+--------+-------------+------+-----+---------+----------------+
| ID     | int(11)     | NO   | PRI | NULL    | auto_increment |
| M_ISBN | varchar(40) | YES  |     | NULL    |       |
| URL    | varchar(60) | YES  |     | NULL    |       |
+--------+-------------+------+-----+---------+----------------+

```

## 二、步骤
## 1.建库
```
CREATE DATABASE `TEST` DEFAULT CHARACTER SET utf8mb4;
```
## 2.建表
```
CREATE TABLE TEST.IMAGE(ID INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,M_ISBN VARCHAR(40),URL VARCHAR(60));
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
## 5.编译程序
```
mvn package
```
## 4.运行程序
```
java -jar crawler-1.0.jar DB_IP DB USER PASSWD ISBN_FILE AMOUNT_OF_THREADS
```
需要指定参数为
* DB_IP：数据库地址
* DB：数据库名
* USER：数据库用户名
* PASSWD：密码
* ISBN_FILE：文本文件路径
* AMOUNT_OF_THREADS：线程数
例如：
```
java -jar crawler-1.0.jar localhost:3306 TEST root 123456 ~/isbn.txt 1
```
显示如下：
```
Starting T_1
  T_1 : 1194867 : 978-7-112-18558-0     
  T_1 : 1194866 : 978-7-5074-3034-9     
  T_1 : 1194865 : 978-7-300-21983-7     
  T_1 : 1194864 : 978-7-5136-3987-3     
  T_1 : 1194863 : 978-7-5429-4319-4
```
## 三、参考文献
* http://docs.oracle.com/javase/8/docs/api/
* https://www.zhihu.com/question/21234530
* http://blog.csdn.net/hu948162999/article/details/40372383?utm_source=tuicool&utm_medium=referral
* http://blog.csdn.net/ku360517703/article/details/41907319
* http://www.infoq.com/cn/news/2011/06/xxb-maven-9-package
