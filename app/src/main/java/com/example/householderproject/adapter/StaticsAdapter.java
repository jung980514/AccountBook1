package com.example.householderproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.householderproject.R;
import com.example.householderproject.model.HouseHoldModel;

import java.util.ArrayList;

public class StaticsAdapter extends BaseAdapter {

    Context context;
    ArrayList<HouseHoldModel> list;

    public StaticsAdapter(Context context, ArrayList<HouseHoldModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewCategory;
        TextView textViewDate;
        TextView textViewLocation;
        TextView textViewCredit;


        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment2_list_view_holder, null);
        }
        textViewCategory = convertView.findViewById(R.id.textViewCategory);
        textViewDate = convertView.findViewById(R.id.textViewDate);
        textViewLocation = convertView.findViewById(R.id.textViewLocation);
        textViewCredit = convertView.findViewById(R.id.textViewCredit);

        textViewCategory.setText(list.get(position).getCategory());
        textViewCredit.setText(list.get(position).getCredit());
        textViewLocation.setText(list.get(position).getLocation());
        textViewDate.setText(list.get(position).getDate());

        return convertView;
    }
}
