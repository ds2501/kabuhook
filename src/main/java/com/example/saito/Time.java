package com.example.saito;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    private LocalDateTime nowTime;

    public Time() {
        nowTime = LocalDateTime.now();
    }

    public LocalDateTime getNowTime() {
        nowTime = LocalDateTime.now();
        return nowTime;
    }

    public String getPastTime(int num) {
        //現在時刻-1取得
        nowTime = LocalDateTime.now();
        LocalDateTime date1 = nowTime.minusSeconds(num);
        //形式変更
        DateTimeFormatter dtformat1 =
                DateTimeFormatter.ofPattern("yyyy-MM-dd_");
        DateTimeFormatter dtformat2 =
                DateTimeFormatter.ofPattern("HH:mm:ss");
        //年月日
        String fdate1 = dtformat1.format(date1);
        //時間
        String fdate2 = dtformat2.format(date1);
        //検索用に変換
        return fdate1+fdate2+"_JST";
    }
}
