package com.example.householderproject.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.householderproject.R;
import com.example.householderproject.adapter.StaticsAdapter;
import com.example.householderproject.model.HouseHoldModel;
import com.example.householderproject.util.DBHelper;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

public class Fragment2 extends Fragment implements View.OnClickListener {

    private int yearOfNow = 0;
    private int monthOfNow = 0;

    private FrameLayout frameLayout;
    private LinearLayout layoutSepntingIncome;
    private LinearLayout layoutChartAndListView;
    private LinearLayout layoutDatePicker;
    private Button btYearAndMonthDatePicker;
    private Button btYearDatePicker;
    private Button btSpend;
    private Button btEarn;
    private PieChart pieChart;
    private ListView listView;

    private ArrayList<HouseHoldModel> listViewData = new ArrayList<>();

    private Description description = null;

    private ArrayList<PieEntry> pieEntries;
    private Animation animation=null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment2, container, false);

        Button btMonthReport = view.findViewById(R.id.btMonthReport);
        Button btYearReport = view.findViewById(R.id.btYearReport);
        Button btSpentLimit = view.findViewById(R.id.btSpentLimit);

        layoutDatePicker = view.findViewById(R.id.layoutDatePicker);

        btYearAndMonthDatePicker = view.findViewById(R.id.btYearAndMonthDatePicker);
        btYearDatePicker = view.findViewById(R.id.btYearDatePicker);

        layoutSepntingIncome = view.findViewById(R.id.layoutSepndingIncome);
        layoutChartAndListView = view.findViewById(R.id.layoutChartAndListView);

        btSpend = view.findViewById(R.id.btSpend);
        btEarn = view.findViewById(R.id.btEarn);

        pieChart = view.findViewById(R.id.pieChart);

        listView = view.findViewById(R.id.listView);

        frameLayout = view.findViewById(R.id.frameLayout);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        yearOfNow = calendar.get(Calendar.YEAR);
        monthOfNow = calendar.get(Calendar.MONTH) + 1;
        btYearAndMonthDatePicker.setText(yearOfNow + "년 " + monthOfNow + "월");
        btYearDatePicker.setText(yearOfNow + "년");

        btYearReport.setOnClickListener(this);
        btMonthReport.setOnClickListener(this);

        btSpend.setOnClickListener(this);
        btEarn.setOnClickListener(this);

        btYearAndMonthDatePicker.setOnClickListener(this);
        btYearDatePicker.setOnClickListener(this);
        btSpentLimit.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btYearReport :
                animationEffect(v);
                buttonYearReportHandler();
                break;
            case R.id.btMonthReport :
                animationEffect(v);
                buttonMonthReportHandler();
                break;
            case R.id.btSpend :
                animationEffect(v);
                buttonSpendHandler();
                break;
            case R.id.btEarn :
                animationEffect(v);
                buttonEarnHandler();
                break;
            case R.id.btYearAndMonthDatePicker :
                animationEffect(v);
                buttonYearAndMonthDatePickerHandler();
                break;
            case R.id.btYearDatePicker :
                animationEffect(v);
                buttonYearDatePickerHandler();
                break;
            case R.id.btSpentLimit :
                animationEffect(v);
                buttonSpentLimitHandler();
                break;
        }
    }

    //버튼 애니메이션 효과
    private void animationEffect(View v) {
        animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);
        v.startAnimation(animation);
    }

    private void buttonYearReportHandler() {

        yearOfNow = Calendar.getInstance().get(Calendar.YEAR);
        btYearDatePicker.setText(yearOfNow + "년");
        monthOfNow = 0;

        layoutVisibleSetting(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.VISIBLE);
    }

    private void buttonMonthReportHandler() {

        yearOfNow = Calendar.getInstance().get(Calendar.YEAR);
        monthOfNow = Calendar.getInstance().get(Calendar.MONTH)+1;
        btYearAndMonthDatePicker.setText(yearOfNow + "년 " + monthOfNow + "월");

        layoutVisibleSetting(View.GONE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE);

    }

    private void buttonEarnHandler() {

        Log.e("Fragment2", yearOfNow + "수입" + monthOfNow);

        if(monthOfNow == 0) {
            listViewData = DBHelper.selectYearDateDatabase(listViewData, getContext(), yearOfNow, "수입");
        } else {
            listViewData = DBHelper.selectYearAndMonthDateDatabase(listViewData, getContext(), yearOfNow, monthOfNow, "수입");
        }

        setPieChart(listViewData);
        description.setText(btEarn.getText().toString());

        StaticsAdapter staticsAdapter = new StaticsAdapter(getContext(), listViewData);
        listView.setAdapter(staticsAdapter);

    }

    private void buttonSpendHandler() {

        Log.e("Fragment2", yearOfNow + "지출" + monthOfNow);

        if(monthOfNow == 0) {
            listViewData = DBHelper.selectYearDateDatabase(listViewData, getContext(), yearOfNow, "지출");
        } else {
            listViewData = DBHelper.selectYearAndMonthDateDatabase(listViewData, getContext(), yearOfNow, monthOfNow, "지출");
        }

        setPieChart(listViewData);
        description.setText(btSpend.getText().toString());

        StaticsAdapter staticsAdapter = new StaticsAdapter(getContext(), listViewData);
        listView.setAdapter(staticsAdapter);

    }

    private void buttonYearAndMonthDatePickerHandler() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        monthOfNow = month + 1;

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                yearOfNow = year;
                monthOfNow = month + 1;

                btYearAndMonthDatePicker.setText(yearOfNow + "년 " + monthOfNow + "월");

            }
        }, year, month, day);

        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.show();
    }

    private void buttonYearDatePickerHandler() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearOfNow = year;

                btYearDatePicker.setText(yearOfNow + "년");

            }
        }, year, month, day);

        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.show();

    }

    private void buttonSpentLimitHandler() {
        layoutVisibleSetting(View.VISIBLE, View.GONE, View.GONE, View.GONE, View.GONE, View.VISIBLE);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new SpentLimitFragment();
        //지출현황프래그먼트에서 뒤로가기 버튼을 누르면 월간보고서 첫화면으로 돌아간다.
        fragmentTransaction.replace(R.id.frameLayout, fragment).addToBackStack(null).commit();

    }

    /******************
     *
     * @param listViewData
     * 기능 작업을 하는 메소드
     *
     */
    private void setPieChart(ArrayList<HouseHoldModel> listViewData) {

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5 , 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(61f);

        pieEntries = new ArrayList<>();

        for(int i = 0; i < listViewData.size(); i++) {

            String spendMoney = listViewData.get(i).getCredit();
            String spendCategory = listViewData.get(i).getCategory();

            spendMoney = spendMoney.replaceAll(",", "");
            spendMoney = spendMoney.replaceAll("\n", "");

            Log.e("!!!!!!" , spendMoney + "||||" + spendCategory);

            pieEntries.add(new PieEntry (Integer.parseInt(spendMoney), spendCategory));

        }

        description = new Description();
        description.setTextSize(15);

        pieChart.setDescription(description);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        PieDataSet dataSet = new PieDataSet(pieEntries, "*파이차트 단위: %");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);

    }

    private void layoutVisibleSetting(int gone, int visible, int visible1, int visible2, int visible3, int gone1) {

        frameLayout.setVisibility(gone);
        layoutSepntingIncome.setVisibility(visible);
        layoutChartAndListView.setVisibility(visible1);
        layoutDatePicker.setVisibility(visible2);
        btYearAndMonthDatePicker.setVisibility(visible3);
        btYearDatePicker.setVisibility(gone1);

    }

}
