package com.example.saito;
/*
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
*/
import java.util.Timer;
import java.util.TimerTask;
/*
Twitterから株価情報抽出するアプリ
*/

public class Main {
    private static final String SLACK_INCOMING_WEBHOOK_BUY = "";
    private static final String SLACK_INCOMING_WEBHOOK_SELL = "";
    private static final int WAIT_TIME_SECONDS = 10;//何秒ごとに実行するか（10秒以上で設定可能）

    public static void main(String[] args) {
        //検索・分析オブジェクト
        Search search = new Search();
        //タイマー
        Timer timer = new Timer();
        //Slack送信用オブジェクト
        Slack sellSlack = new Slack(SLACK_INCOMING_WEBHOOK_SELL, "#kabu-sell", "tanukiti");
        Slack buySlack = new Slack(SLACK_INCOMING_WEBHOOK_BUY, "#kabu-buy", "tanukiti");
        //タスク
        TimerTask task = new TimerTask() {
            //実行内容
            public void run() {
                //検索・分析・送信
                search.execute("あつ森 OR あつもり", WAIT_TIME_SECONDS);
                sellSlack.sendSlack(search.execute("高額（売値）"));
                buySlack.sendSlack(search.execute("格安（買値）"));
            }
        };
        //WAIT_TIME_SECONDS*1000[ms]後に初実行、その後はWAIT_TIME_SECONDS*1000[ms]ごとに再実行
        timer.scheduleAtFixedRate(task, WAIT_TIME_SECONDS*1000, WAIT_TIME_SECONDS*1000);
    }
}