package yz.crawler.douban.cover;

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

    private void process(String[] isbnArray) {
        for (int i = 0; i < isbnArray.length; i++) {
            if (isbnArray[i] != null) {
                //获取URL
                if (getImage(isbnArray[i]) == 1) {
                    //打印到屏幕
                    System.out.printf("%3s : %7s/%7s : %-40s\n", this.threadName, String.valueOf(i + 1), String.valueOf(isbnArray.length), isbnArray[i]);
                } else {
                    //打印到屏幕
                    System.out.printf("Error : %s\n", isbnArray[i]);
                }
                i++;
            }
        }
    }

    private int getImage(String isbn) {
        int status = 0;
        String url = "http://book.douban.com/isbn/" + isbn;
        //try 5 times
        for (int i = 0; i < 5; i++) {
            try {
                Document doc = null;
                Elements elements = null;
                doc = Jsoup.connect(url)
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .header("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3")
                        .header("Accept-Encoding", "none")
                        .header("Accept-Language", "en-US,en;q=0.8")
                        .header("Connection", "keep-alive")
                        .header("Cookie", "bid=jWHJzCwzjrM; ll=\"118162\"; gr_user_id=8e65c180-924b-4ae3-8a72-33f5bb78721c; dbcl2=\"158886871:pcmrZVXxbGk\"; ck=jvkP; ps=y; ap=1; __utmt_douban=1; __utmt=1; gr_session_id_22c937bbd8ebd703f2d8e9445f7dfd03=733b90cb-438f-412e-95f4-94c8fb845d67; gr_cs1_733b90cb-438f-412e-95f4-94c8fb845d67=user_id%3A1; push_noty_num=0; push_doumail_num=0; __utma=81379588.332662854.1488959892.1488959892.1489147073.2; __utmb=81379588.6.10.1489147073; __utmc=81379588; __utmz=81379588.1488959892.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _pk_id.100001.3ac3=e8f4dc7edd9cf588.1488959892.2.1489148025.1488959946.; _pk_ses.100001.3ac3=*; _vwo_uuid_v2=CDF0BC251A2A7706B0CA7D8A7B0DC816|be683557eebf151e47f4954dbb452135; __utmt=1; __utma=30149280.494770583.1488959108.1488959108.1489146275.2; __utmb=30149280.10.10.1489146275; __utmc=30149280; __utmz=30149280.1489146275.2.2.utmcsr=bing|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided)")
                        .header("User-Agent", UserAgent.getUserAgent())
                        .get();
                //按照class获取封面图片所在的a标签
                elements = doc.getElementsByClass("nbg");
                //若不为空
                if (!elements.isEmpty()) {
                    //获取图片url
                    String imageURL = elements.first().attr("href");
                    dbConn.saveRecord(isbn, imageURL);
                    status = 1;
                    //等待4秒
                    Thread.sleep(4000);
                    break;
                } else {
                    Thread.sleep(4000);
                    continue;
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return status;
    }
}
