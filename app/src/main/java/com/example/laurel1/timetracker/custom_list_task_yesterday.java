package com.example.laurel1.timetracker;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
public class custom_list_task_yesterday extends ArrayAdapter<String> implements AdapterView.OnItemClickListener
{
    private final Activity context;
    private final String[] client_nm;
    private final String[] hours;
    private final String[] project_nm;
    private final String[] task_name;
    private final String[] get_count;
    int position1;
    public custom_list_task_yesterday(Activity context,
                            String[] client_nm, String[] hours, String[] project_nm,String[] task_name, String[] get_count) {
        super(context, R.layout.custom_list,client_nm);
        this.context = context;
        this.client_nm = client_nm;
        this.hours = hours;
        this.project_nm = project_nm;
        this.task_name=task_name;
        this.get_count=get_count;
    }
    @Override
    public int getCount() {
        return get_count.length+1;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        if(position==get_count.length) {
            view = inflater.inflate(R.layout.addbutton2, parent, false);
            CardView add = (CardView) view.findViewById(R.id.card_view2);
            if (add != null) {
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.framelayyesterday, new Add_New_Yesterday());
                        // trans.replace(R.id.first_fragment_root_id, NextFragment.newInstance());
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
            }
            return view;
        }
        View listViewItem= inflater.inflate(R.layout.custom_list, null, true);
        TextView client_name = (TextView) listViewItem.findViewById(R.id.client_nm);
        TextView Hours = (TextView) listViewItem.findViewById(R.id.number_hours);
        TextView project_name = (TextView) listViewItem.findViewById(R.id.project_nm);
        TextView task_nm = (TextView) listViewItem.findViewById(R.id.tsk_nm);
        //custom school activity
        client_name.setText(client_nm[position]);
        Hours.setText(hours[position] +" Hrs");
        project_name.setText(project_nm[position]);
        task_nm.setText(task_name[position]);
        return listViewItem;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("here...............");
    }
}

