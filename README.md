# douban-cover-crawler
Get cover link from douban.

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

## 二、过程
## 1.算法
1）读取参数
2）根据参数中的文件名，读取文件内全部信息，生成参数中指定的线程数目个字符串数组；
3）生成生成参数中指定的线程数目个线程，并将数组分别传给每个线程；
4）线程开始，遍历数组，根据字符串拼接成url，类似“http://book.douban.com/isbn/978-7-121-28330-7”，
在其页面解析出class为"nbg"的标签，其"href"属性则为封面图片。

## 三、使用
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

## 四、参考文献
* http://docs.oracle.com/javase/8/docs/api/
* https://www.zhihu.com/question/21234530
* http://blog.csdn.net/hu948162999/article/details/40372383?utm_source=tuicool&utm_medium=referral
* http://blog.csdn.net/ku360517703/article/details/41907319
* http://www.infoq.com/cn/news/2011/06/xxb-maven-9-package

