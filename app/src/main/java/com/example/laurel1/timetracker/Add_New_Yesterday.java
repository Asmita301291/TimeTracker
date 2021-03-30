package com.example.laurel1.timetracker;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import org.json.JSONException;
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
public class Add_New_Yesterday extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    String date;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Spinner sp_clientname=(Spinner)view.findViewById(R.id.client_nm_y);
        final Spinner sp_projectname=(Spinner)view.findViewById(R.id.project_nm_y);
        final EditText task=(EditText)view.findViewById(R.id.task_y);
        final EditText hours=(EditText)view.findViewById(R.id.hours_y);
       /* DateFormat df = new SimpleDateFormat("d/MM/yyyy");
        final String date = df.format(Calendar.getInstance().getTime());*/
        DateFormat df = new SimpleDateFormat("d/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        date= df.format(cal.getInstance().getTime());
        // new Showtask().execute(date);
        Submit=(Button)view.findViewById(R.id.btnsub_y);
      /*  Client c = new Client();
        c.getClass();*/
        new Client().execute();
        new Project().execute();
        adapterclient = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout,R.id.txtright, listclient);
        sp_clientname.setAdapter(adapterclient);
        adapterproject = new ArrayAdapter<String>(getContext(), R.layout.spinner_layout,R.id.txtright, listproject);
        sp_projectname.setAdapter(adapterproject);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp_clientname.getSelectedItem().toString().equals("Select Client Name"))///"Select Project Name"
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("oops!");
                    alertDialog.setMessage("Select Valid Client Name ");
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                    sp_clientname.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(sp_clientname, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                    alertDialog.show();
                    return;
                }
                else {
                    Client=sp_clientname.getSelectedItem().toString();
                }
                if(sp_projectname.getSelectedItem().toString().equals("Select Project Name"))///"Select Project Name"
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("oops!");
                    alertDialog.setMessage("Select Valid Project Name ");
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                    sp_projectname.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(sp_projectname, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                    alertDialog.show();
                    return;
                }
                else {
                    Project=sp_projectname.getSelectedItem().toString();

                }
                if (task.getText().toString().equals(""))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("oops!");
                    alertDialog.setMessage("Select task ");
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                    task.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(task, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                    alertDialog.show();
                    return;
                }
                else {
                    Task=task.getText().toString();

                }
                if (hours.getText().toString().equals(""))
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("oops!");
                    alertDialog.setMessage("Select hours ");
                    alertDialog.setButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //dismiss the dialog
                                    hours.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(hours, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                    alertDialog.show();
                    return;
                }
                else {
                    Hours=hours.getText().toString();
                }
                new Submittask().execute(Client, Project, Task, Hours, date);
                FragmentTransaction trans = getFragmentManager().beginTransaction();
                trans.replace(R.id.frameaddnewyesterday, new FragmentTwo());
                // trans.replace(R.id.first_fragment_root_id, NextFragment.newInstance());
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);
                trans.commit();
                           }
        });
    }
    private String mParam2;
    Button Submit;
    ArrayList<String> listclient= new ArrayList<>();
    ArrayList<String> listproject= new ArrayList<>();
    ArrayAdapter<String> adapterclient;
    ArrayAdapter<String> adapterproject;
    String Client,Project,Task,Hours;
    ProgressDialog pDialog;
    String Totaltime;
    private OnFragmentInteractionListener mListener;
    public Add_New_Yesterday() {
        // Required empty public constructor
    }
       // TODO: Rename and change types and number of parameters
    public static Add_New_Yesterday newInstance(String param1, String param2) {
        Add_New_Yesterday fragment = new Add_New_Yesterday();
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
        return inflater.inflate(R.layout.fragment_add__new_yesterday, container, false);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public class Submittask extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result = "";
            HttpEntity entity;
            String url = "http://kritii.co/TimeTracker/save_yesterday_task.php";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            try {
                List<NameValuePair> parameterList = new ArrayList<NameValuePair>(5);
                parameterList.add(new BasicNameValuePair("Client", params[0]));
                parameterList.add(new BasicNameValuePair("Project", params[1]));
                parameterList.add(new BasicNameValuePair("Task", params[2]));
                parameterList.add(new BasicNameValuePair("Hours", params[3]));
                parameterList.add(new BasicNameValuePair("Date", params[4]));
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
                 }
                else
                {
                    Toast.makeText(getActivity(), msg.getString("error_msg"), Toast.LENGTH_SHORT).show();
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
        private class Client extends AsyncTask<Void, Void, Void>
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
            listclient.add("Select Client Name");
            listclient.addAll(listc);
            adapterclient.notifyDataSetChanged();
        }
    }
    private class Project extends AsyncTask<Void, Void, Void>
    {
        ArrayList<String> listp;
        protected void onPreExecute()
        {
            super.onPreExecute();
            listp=new ArrayList<>();
        }
        protected Void doInBackground(Void...params){
            InputStream is = null;
            String result="";
            try
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost= new HttpPost("http://kritii.co/TimeTracker/project_list.php");
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
                    listp.add(jsonObject.getString("pr_name"));
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
            listproject.add("Select Project Name");
            listproject.addAll(listp);
            adapterproject.notifyDataSetChanged();
        }
    }
}
