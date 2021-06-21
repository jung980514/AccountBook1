package com.example.householderproject.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householderproject.MainActivity;
import com.example.householderproject.R;
import com.example.householderproject.adapter.RecyclerViewAdapter;
import com.example.householderproject.model.HouseHoldModel;
import com.example.householderproject.util.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment3 extends Fragment implements DatePicker.OnDateChangedListener {

    public static Fragment3 newInstance() {
        return new Fragment3();
    }

    private ArrayList<HouseHoldModel> list = new ArrayList<>();
    private RecyclerViewAdapter recyclerViewAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment3, container, false);

        DatePicker datePicker = view.findViewById(R.id.datePicker);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.myContext);

        recyclerView.setLayoutManager(linearLayoutManager);

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();

        String transDate = transformDate(year, month, day);

        recyclerViewAdapter = new RecyclerViewAdapter(list, R.layout.recyclerview_list_item_holder);

        selectFromDatabase(transDate);

        recyclerView.setAdapter(recyclerViewAdapter);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);

        Log.e("!!!", "onCreateView");

        return view;

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        String date = "";

        date = transformDate(year, monthOfYear, dayOfMonth);

        selectFromDatabase(date);

    }

    private void selectFromDatabase(String date) {

        list = DBHelper.selectDateFromDatabase(list, getContext(), date);

        recyclerViewAdapter.notifyDataSetChanged();

    }

    private String transformDate(int year, int monthOfYear, int dayOfMonth) {

        String date = "";

        if(monthOfYear < 10 && dayOfMonth < 10) {
            date = year + "0" + (monthOfYear+1) + "0" + dayOfMonth;
        } else if(monthOfYear < 10 && dayOfMonth > 10) {
            date = year + "0" + (monthOfYear+1) + "" +dayOfMonth;
        } else if (monthOfYear > 10 && dayOfMonth < 10) {
            date = year + "" + (monthOfYear+1) + "0" +dayOfMonth;
        } else {
            date = String.valueOf(year) + (monthOfYear+1) + (dayOfMonth);
        }

        return date;

    }

}
