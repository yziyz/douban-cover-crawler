package yz.crawler.douban.cover;

/**
 * Created by yz on 12/31/16.
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Worker implements Runnable {

    private Thread thread;
    private String threadName;
    private String[] isbnArray;
    private DBConn dbConn;

    public Worker(String threadName, String[] isbnArray) {
        this.threadName = threadName;
        this.isbnArray = isbnArray;
    }

    public void run() {
        System.out.println("Running " + threadName + ", length " + isbnArray.length);
        dbConn = new DBConn();
        process(this.isbnArray);
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    public void process(String[] isbnArray) {
        for (int i = 0; i < isbnArray.length; i++) {
            if (isbnArray[i] != null) {
                //获取URL
                if (getImage(isbnArray[i]) == 1) {
                    //打印到屏幕
                    System.out.printf("%3s : %7s/%7s : %-40s\n", this.threadName, String.valueOf(i + 1), String.valueOf(isbnArray.length), isbnArray[i]);
                }else {
                    //打印到屏幕
                    System.out.printf("Error : %s\n", isbnArray[i]);
                }
                i++;
            }
        }
    }

    public int getImage(String isbn) {
        int status = 0;
        String url = "http://book.douban.com/isbn/" + isbn;
        Document doc = null;
        Elements elements = null;
        try {
            doc = Jsoup.connect(url)
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Accept-Encoding", "gzip, deflate, sdch, br")
                    .header("Accept-Language", "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3")
                    .header("Connection", "keep-alive")
                    .header("Host", "book.douban.com")
                    .header("Referer", "https://book.douban.com/")
                    .header("Upgrade-Insecure-Requests", "1")
                    .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                    .get();
            //按照class获取封面图片所在的a标签
            elements = doc.getElementsByClass("nbg");
            //若不为空
            if (!elements.isEmpty()) {
                //获取图片url
                String imageURL = elements.first().attr("href");
                dbConn.saveRecord(isbn, imageURL);
                status = 1;
                //等待1.5秒
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.printf("%21s : %-40s\n", "Not found", isbn);
        }
        return status;
    }
}
