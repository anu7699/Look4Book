package com.wink.anu.look4book;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by WELLCOME on 01-08-2017.
 */

public final class QueryUtils {
    public static final String LOG_TAG="QueryUtils";
    public QueryUtils()
    {

    }
    public static ArrayList<Book> extractBookList(String JSON_resp)
    {
        ArrayList<Book> books=new ArrayList<>();

        try {
            JSONObject root= new JSONObject(JSON_resp);
            JSONArray items=root.getJSONArray("items");
            for(int i=0;i<items.length();i++)
            {
                JSONObject item=items.getJSONObject(i);
                JSONObject volumeInfo=item.getJSONObject("volumeInfo");
                String title=volumeInfo.getString("title");

                JSONArray author=volumeInfo.getJSONArray("authors");
                int l=author.length();
                String[] authorArray=new String[l];
                for (int j=0;j<l;j++)
                {
                    authorArray[j]=author.getString(j);

                }
                String Url =volumeInfo.getString("infoLink");
                JSONObject imageLinks=volumeInfo.getJSONObject("imageLinks");
                String imgUrl=imageLinks.getString("thumbnail");
                Book thisBook=new Book(title,TextUtils.join(",",authorArray),Url,imgUrl);
                books.add(thisBook);

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,"Problem parsing json",e);
        }
        return books;
    }
    public static ArrayList<Book> fetchBookList(String link)
    {
        if(link.equals(null))
            return null;
        URL url=createUrl(link);
        if(url==null)
            return null;
        String JSONresp="";
        try {
            JSONresp=makeHttpConn(url);
        }catch (IOException e)
        {
            Log.e(LOG_TAG,"problem connecting to the server",e);
            return null;
        }
        return extractBookList(JSONresp);
    }

    private static String makeHttpConn(URL url) throws IOException {
        if(url==null)
            return "";
        String JSONresp="";
        HttpURLConnection connection=null;
        InputStream is=null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.connect();
            if (connection.getResponseCode() != 200)
                Log.e(LOG_TAG, "problem connecting...");
            else {
                is = connection.getInputStream();
                JSONresp = readFromStream(is);
            }
        }catch (IOException e)
        {   Log.e(LOG_TAG,"error while making http request");
            }
        finally {
            if(connection!=null)
                connection.disconnect();
            if(is!=null)
                is.close();

        }
        return JSONresp;


    }

    private static String readFromStream(InputStream is) throws IOException {
        StringBuilder output=new StringBuilder();
        if(is!=null) {
            InputStreamReader reader = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader buffer_reader = new BufferedReader(reader);
            String line=buffer_reader.readLine();
            while (line!=null) {
                output.append(line);
                line=buffer_reader.readLine();
            }


        }
        return output.toString();

    }

    private static URL createUrl(String link) {
        URL url=null;
        try {
            url=new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
