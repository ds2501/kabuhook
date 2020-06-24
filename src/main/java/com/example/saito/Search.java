package com.example.saito;

import java.util.*;

/*
動作概要
①ツイッターから検索ワードで100件まで取得（検索期間：10秒前～実行時）
②その中で高額もしくは格安なツイートを抽出
*/

class Search {

    private Tweet tweet = new Tweet();
    private Time time = new Time();

    public Search() {}

    public void execute(String word, int seconds) {
        //検索
        tweet.searchKabu(word, seconds);
    }
    public List<Kabu> execute( String type) {
        //分析
        return tweet.analyzeKabu(type);
    }
}
