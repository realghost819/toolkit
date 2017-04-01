package com.boil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    static CookieStore cookieStore = null;

    public static void main(String[] args) {

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://guanyun.gzdata.com.cn/Web/PoCommon/PoVote/34");

        HttpGet httpget = new HttpGet("http://guanyun.gzdata.com.cn/Web/YProject/Detail/40");
        String cookie = null;
        CookieHandler.setDefault(new CookieManager());
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
//        formparams.add(new BasicNameValuePair("type", "house"));
        UrlEncodedFormEntity uefEntity;
        try {

            httpget.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
            httpget.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            httpget.addHeader("Accept-Encoding", "gzip, deflate, sdch");
            httpget.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            httpget.addHeader("Cookie", "__cfduid=d0e8c0b791e3027fcc65709aa34817b071491019445");
            httpget.addHeader("Host", "guanyun.gzdata.com.cn");
            httpget.addHeader("Cache-Control", "no-cache");
            CloseableHttpResponse response1 = httpclient.execute(httpget);


            Header[] headers = response1.getHeaders("Set-Cookie");
            for (int i = 0; i < headers.length; i++) {
                Header header = headers[i];
                if (header.toString().indexOf("__RequestVerificationToken")!=-1){
                    cookie = response1.getLastHeader("Set-Cookie") == null ? "" :
                            response1.getLastHeader("Set-Cookie").toString();
                }
            }
            String token = cookie.substring(cookie.indexOf("=") +1 , cookie.indexOf(";"));
            System.out.println(cookie);
            System.out.println(token);

            httppost.addHeader("Host", "guanyun.gzdata.com.cn");
            httppost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");
            httppost.addHeader("Accept", "text/plain, */*; q=0.01");
            httppost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            httppost.addHeader("Referer", "http://guanyun.gzdata.com.cn/Web/YProject/Detail/40");
            httppost.addHeader("X-Requested-With", "XMLHttpRequest");
            httppost.addHeader("Cache-Control", "no-cache");
            httppost.addHeader("Cookie", cookie);

            List<NameValuePair> nvps = new ArrayList<NameValuePair>();

            nvps.add(new BasicNameValuePair("__RequestVerificationToken", token));

            httppost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response2 = httpclient.execute(httppost);
            try {
                HttpEntity entity = response2.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }

                List<Header> httpHeaders = Arrays.asList(response2.getAllHeaders());
                for (Header header : httpHeaders) {
                    System.out.println("Headers.. name,value:" + header.getName() + "," + header.getValue());
                }

            } finally {
//                response2.close();
                response1.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
