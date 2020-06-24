package com.example.saito;

import twitter4j.*;
import java.util.ArrayList;

public class Tweet {
    private Time time = new Time();//時間取得用オブジェクト
    private Twitter twitter = new TwitterFactory().getInstance();//ツイッター検索オブジェクト
    private ArrayList<Kabu> kabuList = new ArrayList<>();//検索結果格納
    private Query query = new Query();//検索オプション設定オブジェクト

    public Tweet() {
        setCount(100);
    }//100件最大読み込めるようにする

    public void setCount(int num) {
        query.setCount(num);
    }//最大読み込み数設定

    public void searchKabu(String word, int seconds) {
        try {
            //検索ワードをQueryにセット
            query.setQuery(word);
            //時間指定（何秒前からか）
            query.since(time.getPastTime(seconds));
            //検索結果用オブジェクト
            QueryResult result = twitter.search(query);
            System.out.println("ヒット数 : " + result.getTweets().size());
            if (kabuList.size() != 0) {
                kabuList.clear();
            }
            //検索結果格納
            for (Status tweet : result.getTweets()) {
                //String str = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(tweet.getCreatedAt());
                kabuList.add(new Kabu(tweet.getText(), tweet.getUser().getScreenName(), tweet.getCreatedAt(), String.valueOf(tweet.getId())));
            }
        } catch (TwitterException e) {
            //API利用制限
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public ArrayList<Kabu> analyzeKabu(String type) {
        //type＝買値か売値
        for (Kabu kabu : kabuList) {//ツイート情報から株価情報など分析
            kabu.analyzeText();
        }
        ArrayList<Kabu> out = new ArrayList<>();
        for (Kabu kabu : kabuList) {//カブ価情報で選別して出力
            int bell = kabu.getBell();
            if (bell >= 400 && bell <= 670 && type.equals("高額（売値）")) {
                out.add(kabu);
            } else if (bell >= 90 && bell <= 100 && type.equals("格安（買値）")) {
                kabu.setType("格安（買値）");
                out.add(kabu);
            }
        }
        return out;
    }
}
