package com.kanzankazu.itungitungan.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class RequestHandler {
    public static final MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    /*public String sendPostRequestJSONOkHTTP1(Context context, String requestURL, RequestBody formBody) throws IOException {
        //RequestBody body = RequestBody.create(JSON, postDataParams);
        OkHttpClient client = new OkHttpClient.Builder
                //().addNetworkInterceptor(new ChuckInterceptor(context)).connectTimeout
                (60, TimeUnit.SECONDS).writeTimeout
                (40, TimeUnit.SECONDS).readTimeout
                (40, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(requestURL).post(formBody).build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }*/

    /*public String sendGetRequestJSONOkHTTP1(Context context, String requestURL) throws IOException {
        //RequestBody body = RequestBody.create(JSON, postDataParams);
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new ChuckInterceptor(context)).connectTimeout(60, TimeUnit.SECONDS).writeTimeout(40, TimeUnit.SECONDS).readTimeout(40, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(requestURL).get().addHeader("Content-Type", "application/json").addHeader("Authorization", "Your Token").addHeader("cache-control", "no-cache").build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }*/

    public String sendPostRequestJSON(String requestURL, JSONObject postDataParams) {
        URL url;
        StringBuilder sb = new StringBuilder();
        try {
            url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.addRequestProperty("content-type", "application/json");
            conn.setRequestProperty("connection", "close");
            System.setProperty("http.keepAlive", "false");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataStringJSON(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                //Reading server response
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String sendGetRequestJSON(String requestURL, String id) {
        HttpURLConnection httpURLConnection = null;
        String jsonString = null;
        try {
            URL u = new URL(requestURL + id);
            httpURLConnection = (HttpURLConnection) u.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestProperty("connection", "close");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + '\n');
            }
            jsonString = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*finally {
            httpURLConnection.disconnect();
        }*/
        return jsonString;
    }

    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) {
        //Creating a URL
        URL url;
        //StringBuilder object to store the message retrieved from the server
        StringBuilder sb = new StringBuilder();
        try {
            //Initializing Url
            url = new URL(requestURL);
            //Creating an httmlurl connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Configuring connection properties
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("connection", "close");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            //Creating an output stream
            OutputStream os = conn.getOutputStream();
            //Writing parameters to the request
            //We are using a method getPostDataStringHASH which is defined below
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataStringHASH(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                sb = new StringBuilder();
                String response;
                //Reading server response
                while ((response = br.readLine()) != null) {
                    sb.append(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String sendGetRequest(String requestURL) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("connection", "close");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public String sendGetRequestParam(String requestURL, String id) {
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(requestURL + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestProperty("connection", "close");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s + "\n");
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    private String getPostDataStringHASH(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public String getPostDataStringJSON(JSONObject params) throws Exception {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Iterator<String> itr = params.keys();
        while (itr.hasNext()) {
            String key = itr.next();
            Object value = params.get(key);
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));
        }
        return result.toString();
    }
}
