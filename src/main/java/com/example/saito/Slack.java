package com.example.saito;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.RequestEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

public class Slack {
    private String url;//webhookのURL
    private String channel;//チャンネルの名前
    private String username;//ユーザー名（いらない）
    private JSONObject section2_text;

    public Slack(String url, String channel, String username) {
        this.url = url;
        this.channel = channel;
        this.username = username;
    }

    public void sendSlack(List<Kabu> result) {
        for (Kabu kabu : result) {
            try {
                // API 呼び出し
                RequestEntity<?> req = RequestEntity //
                        .post(URI.create(url)) //
                        .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8") //
                        .body(createJson(kabu));
                new RestTemplate().exchange(req, String.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String createJson(Kabu kabu) {
        JSONObject out = new JSONObject();
        out.put("channel", channel);
        if (kabu.getBell() >= 600 || kabu.getBell() <= 91) {
            if (kabu.getType().equals("高額（売値）")) {
                out.put("text", "<!channel>" + " :tanukiti:*高額カブ価情報だなも*:tanukiti:");
            } else if (kabu.getType().equals("格安（買値）")) {
                out.put("text", "<!channel>" + " :tanukiti:*格安カブ価情報だなも*:tanukiti:");
            }
        }
        out.put("username", username);
        JSONArray attachments = new JSONArray();
        attachments.put(createAttachment(String.valueOf(kabu.getBell()), kabu.getUrl(), kabu.getTime().toString(), kabu.getGold(), kabu.getMile(), kabu.getPass(), kabu.getText()));
        out.put("attachments", attachments);

        return out.toString();
    }

    public JSONObject createAttachment(String price, String url, String time, int gold, int mile, String pass, String text) {
        JSONObject section = new JSONObject();
        section.put("type", "section");
        JSONObject section_text = new JSONObject();
        section_text.put("type", "mrkdwn");
        section_text.put("text", "*URL:*\n" + url);
        section.put("text", section_text);
        JSONObject section_field1 = new JSONObject();
        section_field1.put("type", "mrkdwn");
        section_field1.put("text", ":kabu:*カブ価:*\n" + price);
        JSONObject section_field2 = new JSONObject();
        section_field2.put("type", "mrkdwn");
        section_field2.put("text", ":twitter-logo:*投稿日時:*\n" + time);
        JSONObject section_field3 = new JSONObject();
        section_field3.put("type", "mrkdwn");
        if (gold == 0) {
            section_field3.put("text", ":goldore:*金鉱石:*\n？個");
        } else if (gold == -1) {
            section_field3.put("text", ":goldore:*金鉱石:*\n0個");
        } else {
            section_field3.put("text", ":goldore:*金鉱石:*\n" + gold + "個");
        }
        JSONObject section_field4 = new JSONObject();
        section_field4.put("type", "mrkdwn");
        if (mile == 0) {
            section_field4.put("text", ":mileticket:*マイル旅行券:*\n？枚");
        } else if (mile == -1) {
            section_field4.put("text", ":mileticket:*マイル旅行券:*\n0枚");
        } else {
            section_field4.put("text", ":mileticket:*マイル旅行券:*\n" + mile + "枚");
        }
        JSONObject section_field5 = new JSONObject();
        section_field5.put("type", "mrkdwn");
        section_field5.put("text", ":key:*パスワード:*\n" + pass);
        JSONObject section_field6 = new JSONObject();
        section_field6.put("type", "mrkdwn");
        if (text.contains("DM")) {
            section_field6.put("text", ":dancer:*DM:*\n:ok:");
        } else {
            section_field6.put("text", ":dancer:*DM:*\n:進入禁止:");
        }
        JSONArray filedsArray = new JSONArray();
        filedsArray.put(section_field1);
        filedsArray.put(section_field2);
        filedsArray.put(section_field3);
        filedsArray.put(section_field4);
        filedsArray.put(section_field5);
        filedsArray.put(section_field6);
        section.put("fields", filedsArray);
        //--------------attchment_Array------------------------//
        JSONArray blocksArray = new JSONArray();
        blocksArray.put(section);
        JSONObject divider = new JSONObject();
        divider.put("type", "divider");
        blocksArray.put(divider);
        JSONObject attachment = new JSONObject();
        attachment.put("blocks", blocksArray);
        if (Integer.parseInt(price) >= 600 || Integer.parseInt(price) <= 91) {
            attachment.put("color", "#ffe615");
        } else {
            attachment.put("color", "#d3d3d3");
        }
        return attachment;
    }
}
