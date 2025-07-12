package com.jas.parker.module.inquire;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by User on 2016/7/14.
 */
public class CatchWebsiteParameters implements Runnable {

    public static URL url;
    ArrayList<String> hiddenInput = new ArrayList<String>();

    @Override
    public void run() {

        //抓網站隱藏輸入
        try {
            url = new URL("https://ebill.ba.org.tw/CPP//DesktopDefault.aspx?TabIndex=4&TabId=108");
            Document xmlDoc = Jsoup.parse(url, 3000);
            Elements hidden = xmlDoc.select("input[type*=hidden]");

            for(Element h : hidden){
                hiddenInput.add(h.attr("value")); //如果是hidden的input 就加進arrayList裡面

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setUrl(URL u){
        url = u;
    }
    public URL getUrl(){
        return url;
    }
    public void setHiddenInput(ArrayList<String> hiddenInput1){
        hiddenInput = hiddenInput1;
    }
    public ArrayList<String> getHiddenInput(){
        return hiddenInput;
    }
}
