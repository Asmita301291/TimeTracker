package com.example.laurel1.timetracker;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentThree.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentThree#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentThree extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int icount;
       FrameLayout fl;
    custom_list_task_selectdate adapter;
    ListView tasklistdate;
    TextView totalhrs;
    ImageView imgadd;
    String[] client_name,hours,project_name,task_name,get_count,total_hours;
    ProgressDialog pDialog;
    String date,Total_hours;
    public static String selectdate;
    private OnFragmentInteractionListener mListener;
    public FragmentThree() {
    // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentThree.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentThree newInstance(String param1, String param2) {
        FragmentThree fragment = new FragmentThree();
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
       /* final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date = String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear+1)
                        +"/"+String.valueOf(year);
                FragmentThree.selectdate=date;
           //     tfDescription.setText(date);
             //   tfDate.setText(date);

                if(date.equals(" "))
                {
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                }
                else {
                    new datewiselist().execute(date);
                }
            }
        }, yy, mm, dd);
        datePicker.show();*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_three, container, false);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tasklistdate=(ListView)view.findViewById(R.id.lsttaskdate3);
        totalhrs=(TextView) view.findViewById(R.id.number_hours3);
        try {
            date=Default.date;
            if((date).equals(""))
            {
                Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
            }
            else {
                //date=Default.date;
           /*Bundle i=getActivity().getIntent().getExtras();
            date=i.getString("date");*/
                new datewiselist().execute(date);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

       /* StringBuilder date = Default.date;
        String date1=date.toString();
        new datewiselist().execute(date1);*/
    }
/* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class datewiselist extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            HttpEntity entity;
            String url = "http://kritii.co/TimeTracker/get_selected_date_tasks.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameterList = new ArrayList<NameValuePair>(1);
                parameterList.add(new BasicNameValuePair("selected_date", params[0]));
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
                     }
                else {
                    result = "";
                    Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
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
                    total_hours = new String[jsonArray.length()];
                    for ( icount = 0; icount < jsonArray.length(); icount++)
                    {
                        final JSONObject jtemp = jsonArray.getJSONObject(icount);
                        client_name[icount]=jtemp.getString("client_name");//jsonObject.getString("customer_fname") + " " +jsonObject.getString("customer_lname")
                        hours[icount]=jtemp.getString("hours");
                        project_name[icount]=jtemp.getString("project_name");
                        task_name[icount]=jtemp.getString("task_name");
                        get_count[icount]=jtemp.getString("count_rows");//total_hours_worked
                        total_hours[icount]=jtemp.getString("total_hours_worked");
                    }

                    adapter = new custom_list_task_selectdate(getActivity(),client_name,hours,project_name,task_name,get_count);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tasklistdate.setAdapter(adapter);
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
                    adapter = new custom_list_task_selectdate(getActivity(),client_name,hours,project_name,task_name,get_count);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            tasklistdate.setAdapter(adapter);
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
          //  totalhrs.setText(total_hours[0]);
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
            String url = "http://kritii.co/TimeTracker/get_selected_date_hours_count.php";
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
                    Total_hours= res.getString("total_hours").toString();
                    if (Total_hours.equals("")) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                totalhrs.setText("00");
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                totalhrs.setText(Total_hours);
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
            super.onPreExecute();
        }
    }
}
