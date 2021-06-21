package com.example.householderproject.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.householderproject.R;
import com.example.householderproject.adapter.StaticsAdapter;
import com.example.householderproject.model.HouseHoldModel;
import com.example.householderproject.util.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class SpentLimitFragment extends Fragment{

    private Button btDatePicker;

    private TextView textViewRemainCredit;
    private TextView textViewPercentage;
    private TextView textViewBudget;
    private TextView textViewSpent;

    private ListView listViewSpent;
    private ListView listViewIncome;

    private ProgressBar progressBar;

    private int yearOfNow = 0;
    private int monthOfNow = 0;

    private ArrayList<HouseHoldModel> spentList = new ArrayList<>();
    private ArrayList<HouseHoldModel> incomeList = new ArrayList<>();
    private ArrayList<HouseHoldModel> incomeListForProgressBar = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.spending_limit_fragment, container, false);

        btDatePicker = view.findViewById(R.id.btDatePicker);
        textViewRemainCredit = view.findViewById(R.id.textViewRemainCredit);
        textViewPercentage = view.findViewById(R.id.textViewPercentage);
        textViewBudget = view.findViewById(R.id.textViewBudget);
        textViewSpent = view.findViewById(R.id.textViewSpent);

        listViewSpent = view.findViewById(R.id.listViewSpent);
        listViewIncome = view.findViewById(R.id.listViewIncome);

        progressBar = view.findViewById(R.id.progressBar);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH) + 1;
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        yearOfNow = year;
        monthOfNow = month;

        btDatePicker.setText(yearOfNow + "년 " + monthOfNow + "월");

        btDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /******************
                 * datePickerDialog 창을 만든다.
                 */
                //애니메이션효과
                Animation animation = new AlphaAnimation(0.3f, 1.0f);
                animation.setDuration(500);
                v.startAnimation(animation);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearOfNow = year;
                        monthOfNow = month + 1;

                        btDatePicker.setText(yearOfNow + "년 " + monthOfNow + "월");

                        spentList = DBHelper.selectYearAndMonthDateDatabase(spentList, getContext(), yearOfNow, monthOfNow, "지출");

                        int spentSum = 0;
                        for(int i = 0; i < spentList.size(); i++) {
                            String stringSpentSum = spentList.get(i).getCredit().replaceAll(",","");
                            spentSum = Integer.parseInt(stringSpentSum) + spentSum;
                        }
                        StaticsAdapter spentStaticsAdapter = new StaticsAdapter(getContext(), spentList);
                        listViewSpent.setAdapter(spentStaticsAdapter);

                        incomeList = DBHelper.selectYearAndMonthDateDatabase(incomeList, getContext(), yearOfNow, monthOfNow, "수입");

                        int incomeSum = 0;
                        for(int i = 0; i < incomeList.size(); i++) {
                            String stringIncomeSum = incomeList.get(i).getCredit().replaceAll(",", "");
                            incomeSum = Integer.parseInt(stringIncomeSum) + incomeSum;
                        }

                        int remainMoney = incomeSum - spentSum;

                        textViewRemainCredit.setText(String.valueOf(remainMoney));
                        textViewBudget.setText(String.valueOf(incomeSum));
                        textViewSpent.setText(String.valueOf(spentSum));

                        try {


                            Log.d("spent", "ddd" + incomeSum);
                            float ratio;
                            ratio = (float) spentSum / incomeSum;

                            int percent = (int) (ratio * 100);

                            progressBar.setMax(100);
                            progressBar.setScaleY(3f);

                            if(percent <= 100) {
                                if(percent > 1) {
                                    progressBar.setProgress(percent);
                                    textViewPercentage.setText(percent + "% 사용중");
                                } else {
                                    progressBar.setProgress(percent);
                                    textViewPercentage.setText("1% 이하 사용중");
                                }
                            } else {
                                progressBar.setProgress(100);
                                textViewPercentage.setText("예산을 초과하였습니다.");
                            }

                        } catch (ArithmeticException e) {
                            progressBar.setProgress(0);
                            textViewPercentage.setText("예산을 설정 하지 않았습니다.");
                        }

                        StaticsAdapter incomeStaticsAdapter1 = new StaticsAdapter(getContext(), incomeList);
                        listViewIncome.setAdapter(incomeStaticsAdapter1);

                    }

                }, year, month - 1, day);
                datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();
            }
        });

        return view;
    }


}
