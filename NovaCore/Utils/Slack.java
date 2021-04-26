package net.novaprison.Utils;

import org.json.simple.JSONObject;

import java.io.BufferedOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Slack {

    public static String statusURL = "https://hooks.slack.com/services/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";

    public static void send(final String m, final String p, final String url)
    {
        final String xurl = "https://hooks.slack.com/services/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";
        Runnable r1 = new Runnable() {
            public void run() {
                if(url.length() < 2) {
                    payload(m, p, xurl);
                } else {
                    payload(m, p, url);
                }
            }
        };

        Thread t = new Thread(r1);
        t.start();
    }

    public static void send(final String m, final String p, final String url, final String skin)
    {
        final String xurl = "https://hooks.slack.com/services/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";
        Runnable r1 = new Runnable() {
            public void run() {
                if(url.length() < 2) {
                    payload(m, p, xurl, skin);
                } else {
                    payload(m, p, url, skin);
                }
            }
        };

        Thread t = new Thread(r1);
        t.start();
    }

    public static boolean payload(String m, String p, String url) {
        JSONObject j = new JSONObject();
        j.put("text", m);
        j.put("username", p);
        j.put("icon_url", "https://cravatar.eu/helmhead/" + p + "/100.png");
        String b = "payload=" + j.toJSONString();
        return post(b, url);
    }

    public static boolean payload(String m, String p, String url, String skin) {
        JSONObject j = new JSONObject();
        j.put("text", m);
        j.put("username", p);
        j.put("icon_url", "https://cravatar.eu/helmhead/" + skin + "/100.png");
        String b = "payload=" + j.toJSONString();
        return post(b, url);
    }

    private static boolean post(String b, String url) {
        int i = 0;
        try {
            URL u = new URL(url);
            HttpURLConnection C = (HttpURLConnection) u.openConnection();
            C.setRequestMethod("POST");
            C.setDoOutput(true);
            BufferedOutputStream B = new BufferedOutputStream(C.getOutputStream());
            B.write(b.getBytes("utf8"));
            B.flush();
            i = C.getResponseCode();
            String o = Integer.toString(i);
            String c = C.getResponseMessage();
            C.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i == 200;
    }
}
