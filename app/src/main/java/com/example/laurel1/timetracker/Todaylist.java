package com.example.laurel1.timetracker;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Todaylist extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int icount;
    String date;
    FrameLayout fl;
    String Total_hours;
    custom_list_task_today adapter;
    ListView tasklist;
    ImageView imgadd;
    TextView ttlhrs;
    String[] client_name,hours,project_name,task_name,get_count;
    ProgressDialog pDialog;
    private OnFragmentInteractionListener mListener;
    public Todaylist() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Todaylist.
     */
    // TODO: Rename and change types and number of parameters
    public static Todaylist newInstance(String param1, String param2) {
        Todaylist fragment = new Todaylist();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todaylist, container, false);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState ){
        super.onViewCreated(view,savedInstanceState);
        tasklist=(ListView)view.findViewById(R.id.lsttasktoday);
        fl = (FrameLayout)view.findViewById(R.id.frametodaylist);
        ttlhrs=(TextView)view.findViewById(R.id.number_hours1);
        DateFormat df = new SimpleDateFormat("d/MM/yyyy");
        date = df.format(Calendar.getInstance().getTime());
        new todaylist().execute(date);
       /*imgadd=(ImageView)view.findViewById(R.id.imgaddtoday);
        imgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fl.removeAllViews();
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.frametodaylist, new Add_New());
                // trans.replace(R.id.first_fragment_root_id, NextFragment.newInstance());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
            }
        });*/
     }
      @Override
    public void onDetach() {
                            super.onDetach();
                            mListener = null;
    }
        public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class todaylist extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            HttpEntity entity;
            String url = "http://kritii.co/TimeTracker/get_today_list.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameterList = new ArrayList<NameValuePair>(1);
                parameterList.add(new BasicNameValuePair("Date", params[0]));
                httpPost.setEntity(new UrlEncodedFormEntity(parameterList));
                HttpResponse responce = httpClient.execute(httpPost);
                entity = responce.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String Line = null;
                    while ((Line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(Line);
                    }
                    inputStream.close();
                    result = stringBuilder.toString();
                } else {
                    result = "";
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                    /*AlertDialog ad = new AlertDialog.Builder(getContext())
                            .create();
                    ad.setCancelable(false);
                    ad.setTitle("oops!");
                    ad.setMessage("User name field is empty");
                    ad.setButton(getContext().getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();*/
                }
            } catch (ClientProtocolException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            try {
                JSONObject jsonobject = (JSONObject) new JSONObject(result);
                JSONObject msg = jsonobject.getJSONObject("error");
                if (Integer.parseInt(msg.getString("error_code")) == 0)
                {
                    JSONArray jsonArray = jsonobject.getJSONArray("response");
                    client_name = new String[jsonArray.length()];
                    hours = new String[jsonArray.length()];
                    project_name = new String[jsonArray.length()];
                    task_name = new String[jsonArray.length()];
                    get_count = new String[jsonArray.length()];
                    for ( icount = 0; icount < jsonArray.length(); icount++)
                    {
                        final JSONObject jtemp = jsonArray.getJSONObject(icount);
                        client_name[icount]=jtemp.getString("client_name");//jsonObject.getString("customer_fname") + " " +jsonObject.getString("customer_lname")
                        hours[icount]=jtemp.getString("hours");
                        project_name[icount]=jtemp.getString("project_name");
                        task_name[icount]=jtemp.getString("task_name");
                        get_count[icount]=jtemp.getString("count_rows");
                    }
                    adapter = new custom_list_task_today(getActivity(),client_name,hours,project_name,task_name,get_count);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tasklist.setAdapter(adapter);
                        }
                    });
                }
                else
                {
                    result = "";
                    client_name= new String[0];
                    hours = new String[0];
                    project_name = new String[0];
                    task_name = new String[0];
                    get_count = new String[0];
                    adapter = new custom_list_task_today(getActivity(),client_name,hours,project_name,task_name,get_count);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tasklist.setAdapter(adapter);
                        }
                    });
                    Toast.makeText(getContext(), msg.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }
                            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            new totalhours().execute(date);
            super.onPreExecute();
        }
    }
    public class totalhours extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            HttpEntity entity;
            String url = "http://kritii.co/TimeTracker/get_today_hours_count.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameterList = new ArrayList<NameValuePair>(1);
                parameterList.add(new BasicNameValuePair("Date", params[0]));
                httpPost.setEntity(new UrlEncodedFormEntity(parameterList));
                HttpResponse responce = httpClient.execute(httpPost);
                entity = responce.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(inputStream, "iso-8859-1"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String Line = null;
                    while ((Line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(Line);
                    }
                    inputStream.close();
                    result = stringBuilder.toString();
                } else {
                    result = "";
                }
            } catch (ClientProtocolException e) {
                result = e.getMessage();
            } catch (IOException e) {
                result = e.getMessage();
            }
            try {
                JSONObject jsonobject = (JSONObject) new JSONObject(result);
                JSONObject msg = jsonobject.getJSONObject("error");
                if (Integer.parseInt(msg.getString("error_code")) == 0) {
                    JSONObject res = jsonobject.getJSONObject("response");
                    Total_hours = res.getString("total_hours").toString();
                    if (Total_hours.equals("")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ttlhrs.setText("00");
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ttlhrs.setText(Total_hours);
                            }
                        });
                    }
                }
                else {
                      final String data= msg.getString("error_msg");
                      Toast.makeText(getContext(), msg.getString("error_msg"),
                                    Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception

                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
          /*pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
            super.onPreExecute();
        }
    }
}
