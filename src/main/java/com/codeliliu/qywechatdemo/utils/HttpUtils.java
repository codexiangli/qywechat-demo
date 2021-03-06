package com.codeliliu.qywechatdemo.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author lixiang
 * @since 2021/5/11
 */
public class HttpUtils {

    /**
     * http get请求
     *
     * @param url
     * @return
     */
    public static JSONObject sendGet(String url) {
        JSONObject jsonObject = null;
        jsonObject = httpRequest(url, "GET", null);
        return jsonObject;
    }


    /**
     * http post请求
     *
     * @param url
     * @return
     */
    public static JSONObject sendPost(String url) {
        JSONObject jsonObject = null;
        jsonObject = httpRequest(url, "POST", null);
        return jsonObject;
    }

    /**
     * http post请求
     *
     * @param url
     * @param output json串
     * @return
     */
    public static JSONObject sendPost(String url, String output) {
        JSONObject jsonObject = null;
        jsonObject = httpRequest(url, "POST", output);
        return jsonObject;
    }


    /**
     * 发起https请求并获取结果
     *
     * @param request       请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param output        提交的数据
     * @return JSONObject(通过JSONObject.get ( key)的方式获取json对象的属性值)
     */
    private static JSONObject httpRequest(String request, String requestMethod, String output) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            //建立连接
            URL url = new URL(request);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(30000);
            connection.setUseCaches(false);
            connection.setRequestMethod(requestMethod);
            if (output != null) {
                OutputStream out = connection.getOutputStream();
                out.write(output.getBytes("UTF-8"));
                out.close();
            }
            //流处理
            inputStream = connection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            //关闭连接、释放资源
            reader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            connection.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 使用finally块来关闭输出流、输入流
            try {
                if (reader != null) {
                    reader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return jsonObject;
    }
}
