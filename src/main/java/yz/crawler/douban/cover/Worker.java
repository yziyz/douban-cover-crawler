package yz.crawler.douban.cover;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Worker implements Runnable {

    private Thread thread;
    private String threadName;

    public Worker(String threadName) {
        this.threadName = threadName;
    }

    public void run() {
        System.out.println("Running " + threadName);
        process();
    }

    public void start() {
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
        }
    }

    private void process() {
        while (!Main.stringStack.empty()) {
            String isbn = Main.stringStack.pop();
            if (getImage(isbn) == 1) {
                //打印到屏幕
                System.out.printf("%-5s : %7s : %-40s\n", this.threadName, Main.stringStack.size(), isbn);
            } else {
                //打印到屏幕
                System.err.printf("Error : %s\n", isbn);
            }
        }
    }

    private int getImage(String isbn) {
        int status = 0;
        String url = "http://book.douban.com/isbn/" + isbn;
        //try 5 times
        Document doc;
        Elements elements;
        for (int i = 0; i < 5; i++) {
            try {
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                        .header("Accept-Encoding", "gzip, deflate, sdch, br")
                        .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.6,en;q=0.4")
                        .header("User-Agent", UserAgent.getUserAgent())
                        .get();
                //按照class获取封面图片所在的a标签
                elements = doc.getElementsByClass("nbg");
                //若不为空
                if (!elements.isEmpty()) {
                    //获取图片url
                    String imageURL = elements.first().attr("href");
                    Main.dbConn.saveRecord(isbn, imageURL);
                    status = 1;
                    //等待10秒
                    Thread.sleep(10000);
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return status;
        //如何根据isbn得到图书信息
    }
}
