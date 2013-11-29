package com.example.shoppinglist;

import java.util.List;

import com.example.shoppinglist.sqlite.model.BarCodeDataBase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter_bar_code_list extends ArrayAdapter<BarCodeDataBase>{
	 
    Context context;
    int layoutResourceId;   
    List<BarCodeDataBase> data = null;
 
    public Adapter_bar_code_list(Context context, int layoutResourceId, List<BarCodeDataBase> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RowBeanHolder holder = null;
 
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
 
            holder = new RowBeanHolder();
            holder.name_bar_code = (TextView)row.findViewById(R.id.name_bar_code);
 
            row.setTag(holder);
        }
        else
        {
            holder = (RowBeanHolder)row.getTag();
        }
 
        BarCodeDataBase object = data.get(position);
        holder.name_bar_code.setText(object.getName());
 
        return row;
    }
 
    static class RowBeanHolder
    {
        TextView name_bar_code;
    }
}