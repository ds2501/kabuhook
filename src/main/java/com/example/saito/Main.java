package com.example.saito;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
/*
Twitterから株価情報抽出するアプリ
*/

public class Main {
    private static final int WAIT_TIME_SECONDS = 10;//何秒ごとに実行するか（10秒以上で設定可能）

    public static void main(String[] args) {
        //検索・分析オブジェクト
        Search search = new Search();
        //タイマー
        Timer timer = new Timer();
        //Slack送信用オブジェクト
        String[] properties = getProperties();
        Slack sellSlack = new Slack(properties[0], "#kabu-sell", "tanukiti");
        Slack buySlack = new Slack(properties[1], "#kabu-buy", "tanukiti");
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
        timer.scheduleAtFixedRate(task, WAIT_TIME_SECONDS * 1000, WAIT_TIME_SECONDS * 1000);
    }

    public static String[] getProperties() {
        Properties settings = new Properties();
        InputStream in;
        String path = "";//properties file path
        try {
            in = new FileInputStream(path);
            settings.load(in);
            in.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        settings.list(System.out);

        return new String[]{settings.getProperty("buy_channel"), settings.getProperty("sell_channel")};
    }
}