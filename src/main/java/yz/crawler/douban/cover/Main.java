package yz.crawler.douban.cover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Main {

    public static String IP;
    public static String USER;
    public static String PASSWD;
    public static String DB;
    public static String ISBN_FILE;
    public static int AMOUNT_OF_THREADS;
    public static Stack<String> stringStack;
    public static DBConn dbConn;

    public static void main(String[] args) {
        //读取参数
        try {
            if (args.length != 6) {
                System.out.println("Useage: java -jar crawler-1.0.jar DB_IP DB USER PASSWD ISBN_FILE AMOUNT_OF_THREADS");
                System.out.println("Please retry, now exit.");
                return;
            } else {
                IP = args[0];
                DB = args[1];
                USER = args[2];
                PASSWD = args[3];
                ISBN_FILE = args[4];
                AMOUNT_OF_THREADS = Integer.valueOf(args[5]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Please retry.");
            return;
        }

        //初始化isbn栈
        stringStack = new Stack<String>();
        //将isbn圧入栈中
        pushAllISBN();
        System.out.println("ISBN amount: " + String.valueOf(stringStack.size() + ";"));
        //初始化数据库连接
        dbConn = new DBConn();
        System.out.println("DB connected;");
        for (int i = 0; i < Main.AMOUNT_OF_THREADS; i++) {
            Worker worker = new Worker("T_" + String.valueOf(i + 1));
            worker.start();
        }

    }

    private static void pushAllISBN() {
        File file = new File(Main.ISBN_FILE);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                //将isbn圧入栈顶
                Main.stringStack.push(tempString.replace("-", ""));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}