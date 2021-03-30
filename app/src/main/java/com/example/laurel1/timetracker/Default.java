package com.example.laurel1.timetracker;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.DatePickerDialog;
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

import com.example.laurel1.timetracker.*;

public class Default extends AppCompatActivity {
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    // private Toolbar toolbar;
    private TabLayout tabLayout;
    static final int DATE_DIALOG_ID = 0;
    static final int DATE_DIALOG_ID1 = 1;
    private int mYear,mMonth,mDay;
    private ViewPager mViewPager;
    private int[] tabIcons={R.drawable.clock};
    String Total_time;
    TextView total_time;
    DatePickerDialog datePickerDialog ;
    int Year, Month, Day ;
    int icount;
  //  String date;
    FrameLayout fl;
    //custom_list_task adapter;
    ListView tasklistdate;
    ImageView imgadd;
    String[] client_name,hours,project_name,task_name,get_count;
    ProgressDialog pDialog;
    public static String date;
    CardView additem;
    int n=0;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        Calendar c=Calendar.getInstance();
        mYear=c.get(Calendar.YEAR);
        mMonth=c.get(Calendar.MONTH);
        mDay=c.get(Calendar.DAY_OF_MONTH);
        Intent i = getIntent();
        name = i.getStringExtra("tab");
        try {
            n = Integer.valueOf(name);
        }
        catch (Exception e)
        {
        }
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setTabGravity(1);
        //selectPage(2);
        tabLayout.setupWithViewPager(mViewPager);
        /*calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR) ;
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);*/
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        //mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //mSectionsPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        /*viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);*/
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
        mViewPager.setCurrentItem(n);
        tabLayout.setupWithViewPager(mViewPager);
        setupTabIcons();
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 2:
                        showDialog(DATE_DIALOG_ID);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }
    void selectPage(int pageIndex)
    {
        mViewPager.setCurrentItem(n);
        tabLayout.setupWithViewPager(mViewPager);
    }
    protected Dialog onCreateDialog(int id) {
        switch (id) {
                        case DATE_DIALOG_ID:
             return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }//DatePickerDialog datePicker = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT,this, mYear, mMonth, mDay);
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            tabLayout.getTabAt(2).setText(new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear));
            StringBuilder data = new StringBuilder().append(mDay).append("/").append(mMonth+1).append("/").append(mYear);
            String date1=data.toString();
            Default.date = date1;
            Intent intent = new Intent(Default.this,Default.class);
            intent.putExtra("tab","2");
            intent.putExtra("date",date1);
            startActivity(intent);
           //new datewiselist().execute(date1);
            Log.e("date",date1);
                 }
    };
    private void setupTabIcons(){
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_date);
        try {
            if (Default.date.equals("")) {

            } else {
                tabLayout.getTabAt(2).setText(Default.date);
               //tabLayout.getTabAt(2).setText(new StringBuilder().append(mDay).append("/").append(mMonth + 1).append("/").append(mYear));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
      private void setupViewPager(ViewPager mViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Todaylist(), "Today");
        adapter.addFragment(new FragmentTwo(), "Yesterday");
        adapter.addFragment(new FragmentThree(), "");
        mViewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
   /* public class datewiselist extends AsyncTask<String,String,String>
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
                    adapter = new custom_list_task(Default.this,client_name,hours,project_name,task_name,get_count);
                 runOnUiThread(new Runnable() {
                        public void run() {
                            tasklistdate.setAdapter(adapter);
                        }
                    });
                   *//* runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // txt_username.setText(User_Name[icount]);

                        }
                    });*//*
                }
                else
                {
                    Toast.makeText(getApplicationContext(), msg.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO: handle exception
//                pDialog.dismiss();
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
          *//*pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*//*
            super.onPreExecute();
        }
    }*/

}
