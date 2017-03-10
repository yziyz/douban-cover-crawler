package yz.crawler.douban.cover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Util {
    //test
    public static void main(String[] args) {
        for (int i = 0; i < 40; i++) {
            System.out.println(UserAgent.getUserAgent());
        }
    }

    public static String[][] getArrayOfISBN() {
        //读取指定的文本文件并返回二位数组
        ArrayList<String> strings = new ArrayList<String>();
        File file = new File(Main.ISBN_FILE);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                strings.add(tempString);
            }
            reader.close();
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
        int lengthOfEachArray = (int) Math.ceil((double) strings.size() / (double) Main.AMOUNT_OF_THREADS);
        String[][] stringArray = new String[Main.AMOUNT_OF_THREADS][lengthOfEachArray];
        int count = 0;
        for (int i = 0; i < Main.AMOUNT_OF_THREADS; i++) {
            for (int j = 0; j < lengthOfEachArray; j++) {
                if (count < strings.size()) {
                    stringArray[i][j] = strings.get(count);
                    count++;
                }
            }
        }
        //删除strings，释放资源
        strings = null;
        System.gc();
        return stringArray;
    }

}
