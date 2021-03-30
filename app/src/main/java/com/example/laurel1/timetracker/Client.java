package com.example.laurel1.timetracker;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Laurel 1 on 11/4/2016.
 */
public class Client {
    ArrayList<String> listclient= new ArrayList<>();
    ArrayAdapter<String> adapterclient;
    private class Client1 extends AsyncTask<Void, Void, Void>
    {
        ArrayList<String> listc;
        protected void onPreExecute()
        {
            super.onPreExecute();
            listc=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is = null;
            String result="";
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost= new HttpPost("http://kritii.co/TimeTracker/client_list.php");
                HttpResponse httpResponse=httpClient.execute(httpPost);
                HttpEntity entity=httpResponse.getEntity();
                is= entity.getContent();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line="";
                while ((line = bufferedReader.readLine()) != null) {
                    result+=line;
                }
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray=new JSONArray(result);
                for (int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject jsonObject  =jsonArray.getJSONObject(i);
                    listc.add(jsonObject.getString("pc_client_name"));
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listclient.add("Client Name");
            listclient.addAll(listc);
            adapterclient.notifyDataSetChanged();
        }
    }
}
