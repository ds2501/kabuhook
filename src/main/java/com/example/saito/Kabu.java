package com.example.saito;

import com.ibm.icu.text.Transliterator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Kabu {
    private int bell = 0;//ベル
    private String text;//Tweet本文
    private String id;//ツイートid
    private Date time;//投稿日時
    private String name;//ユーザ名
    private String type = "高額（売値）";//売りか買いか
    private int mile = 0;//マイル旅行券の枚数
    private int gold = 0;//金鉱石の個数
    private String pass = "？";//パスワード

    public Kabu() {
    }

    public Kabu(String text, String name, Date time, String id) {
        this.text = text;
        this.name = name;
        this.time = time;
        this.id = id;
    }


    public void analyzeText() {
        //ひらがなにしている
        Transliterator transliterator = Transliterator.getInstance("Katakana-Hiragana");
        String result = transliterator.transliterate(text);
        //かぶの文字列の位置を確認
        int idx = result.indexOf("べる");
        bell = analyzeNumber(result, idx - 3, idx, 3);
        if (bell == 0) {
            bell = analyzeNumber(result, idx - 2, idx, 2);
        }
        //略しているかどうかも見る
        idx = result.indexOf("旅行券");
        if (idx == -1) {
            if (!result.contains("まいる券")) {
                mile = -1;
            } else {
                idx = result.indexOf("まいる券");
                mile = analyzeNumber(result, idx + 4, idx + 7, 1);
            }
        } else {
            mile = analyzeNumber(result, idx + 3, idx + 6, 1);
        }
        idx = result.indexOf("金鉱石");
        if (idx == -1) {
            gold = -1;
        } else {
            gold = analyzeNumber(result, idx + 3, idx + 6, 1);
        }
        pass = analyzePass(result);
    }

    public int analyzeNumber(String text, int start_index, int last_index, int length) {
        //結合用
        StringBuilder buf = new StringBuilder();
        //正規表現
        Pattern p = Pattern.compile("[0-9]");
        int out = 0;
        for (int i = start_index; i < last_index + 1; i++) {
            String word;
            try {
                word = String.valueOf(text.charAt(i));
            } catch (Exception e) {
                return 0;
            }
            Matcher m = p.matcher(word);
            if (m.find()) {
                //見つかったらバッファに追加
                buf.append(word);
            } else {
                //数字でないなら出力に追加し、バッファをリセット
                if (buf.length() == length) {
                    out = Integer.parseInt(buf.toString());
                    break;
                }
                buf.setLength(0);
            }
        }
        return out;
    }

    public String analyzePass(String result) {
        String[] pass_patern = {"ぱすわーど", "島ぱすわーど", "pass", "ぱす", "ぱすわあど"};
        int flag = -1;
        String flag_word = "";
        for (String word : pass_patern) {
            int idx = result.indexOf(word);
            if (idx != -1) {
                flag = idx;
                flag_word = word;
                break;
            }
        }
        if (flag == -1) {
            return "？";
        } else {
            String subResult;
            if (flag+flag_word.length()+10 > result.length()-1) {
                subResult = result.substring(flag + flag_word.length(), flag + flag_word.length() + result.length() -1 -flag - flag_word.length());
            } else {
                subResult = result.substring(flag + flag_word.length(), flag + flag_word.length() + 10);
            }
            String regex = "[0-9a-zA-Z]{5}";
            Pattern p = Pattern.compile(regex);
            Matcher m1 = p.matcher(subResult);
            if (m1.find()) {
                return m1.group();
            } else {
                return "？";
            }
        }
    }

    public void print() {
        System.out.println("<-------------------------------------------->");
        // 発言したユーザ
        System.out.println(id);
        // 本文
        System.out.println(text);
        // 発言した日時
        System.out.println(time);
        System.out.println("--------------------------------------------");
        System.out.println(bell + "べる");
        System.out.println("<-------------------------------------------->");
    }

    public String getUrl() {
        //ツイッターのURL取得
        return "https://twitter.com/" + name + "/status/" + id;
    }

    public String getText() {
        return text;
    }

    public int getBell() {
        return bell;
    }

    public String getName() {
        return name;
    }

    public Date getTime() {
        return time;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMile() {
        return mile;
    }

    public int getGold() {
        return gold;
    }

    public String getPass() {
        return pass;
    }
}