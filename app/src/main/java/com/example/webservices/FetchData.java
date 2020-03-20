package com.example.webservices;

import android.os.AsyncTask;

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

public class FetchData extends AsyncTask<Void,Void,Void> {
    String data="",dataparsed="",singleparse="",finaldata="";
    String json_string="";
    String fetch_url="https://storage.googleapis.com/network-security-conf-codelab.appspot.com/v2/posts.json";
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url=new URL(fetch_url);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
            InputStream inputStream=httpURLConnection.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line="";
            while (line!=null){
                line=bufferedReader.readLine();
                data=data+line;
            }
            JSONObject jsonObject=new JSONObject(data);
            JSONArray jsonArray=jsonObject.getJSONArray("posts");
            for (int i = 0; i <jsonArray.length() ; i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);

                String name = jsonObject1.getString("name");
                String message = jsonObject1.getString("message");
                String profileImage = jsonObject1.getString("profileImage");
                singleparse="name:"+name+"\n"+
                        "message:"+message+"\n"+
                        "profile image link"+profileImage+"\n";
                dataparsed=dataparsed+singleparse+"\n";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        json_string=this.data;
        MainActivity.data.setText(this.dataparsed);

    }
}