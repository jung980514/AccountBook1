package com.example.householderproject.fragment;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.householderproject.R;
import com.example.householderproject.adapter.CalendarListAdapter;
import com.example.householderproject.adapter.MonthAdapter;
import com.example.householderproject.model.HouseHoldModel;
import com.example.householderproject.model.MonthItem;
import com.example.householderproject.util.DBHelper;

import java.util.ArrayList;

import static com.example.householderproject.MainActivity.categoryList;
import static com.example.householderproject.MainActivity.myContext;

public class Fragment1 extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static Fragment1 newInstance() {
        return new Fragment1();
    }

    private TextView btPrevious, btNext, txtDate;
    public static GridView gridViewCalendar;
    private TextView textViewYear;
    private ListView listView;
    private View view;
    private String str;

    private EditText editTextCredit;
    private EditText editTextLocation;
    private Spinner spinnerFilter;
    private RadioButton radioButtonPlus;
    private RadioButton radioButtonMinus;
    public static int selectedPosition;

    public static CalendarListAdapter calendarListAdapter;
    public static ArrayList<HouseHoldModel> calendarList = new ArrayList<>();
    private static MonthAdapter monthAdapter;

    public static AdapterView<?> sParent;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        DBHelper.selectCategoryData(getContext());

        if(categoryList.isEmpty()){

            DBHelper dbHelper = new DBHelper(getContext());
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            sqLiteDatabase.execSQL("INSERT INTO spinnerTBL VALUES('" + "??????" + "');");
            sqLiteDatabase.execSQL("INSERT INTO spinnerTBL VALUES('" + "??????" + "');");
            sqLiteDatabase.execSQL("INSERT INTO spinnerTBL VALUES('" + "??????" + "');");
            sqLiteDatabase.close();

            DBHelper.selectCategoryData(getContext());
        }

        view = inflater.inflate(R.layout.fragment1, container, false);

        btPrevious = view.findViewById(R.id.btnPrevious);
        btNext = view.findViewById(R.id.btnNext);
        listView = view.findViewById(R.id.listView);

        gridViewCalendar = view.findViewById(R.id.gvCalender);
        textViewYear = view.findViewById(R.id.tvYearMonth);

        //1.???????????? ??????
        monthAdapter = new MonthAdapter(myContext, selectedPosition);
        gridViewCalendar.setAdapter(monthAdapter);
        setYearMonth();

        calendarList.removeAll(calendarList);
        calendarListAdapter = new CalendarListAdapter(calendarList, R.layout.calendar_list_view_holder, myContext);
        listView.setAdapter(calendarListAdapter);

        gridViewCalendar.setOnItemClickListener(this);
        gridViewCalendar.setOnItemLongClickListener(this);

        btNext.setOnClickListener(this);
        btPrevious.setOnClickListener(this);

        return view;

    }

    // ?????? ?????????
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnNext:
                monthAdapter.setNextMonth();
                monthAdapter.notifyDataSetChanged();
                calendarList.removeAll(calendarList);
                calendarListAdapter.notifyDataSetChanged();
                setYearMonth();
                break;

            case R.id.btnPrevious:
                monthAdapter.setPreviousMonth();
                monthAdapter.notifyDataSetChanged();
                calendarList.removeAll(calendarList);
                calendarListAdapter.notifyDataSetChanged();
                setYearMonth();
                break;

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        sParent = parent;

        gridViewClickEvent(sParent, position);

    }

    //???????????? ????????? ???????????? ??????
    public static void gridViewClickEvent(View view, int position) {

        MonthItem curItem = (MonthItem) monthAdapter.getItem(position);
        String currentDate = String.valueOf(monthAdapter.curYear) + (monthAdapter.curMonth + 1) + "" +monthAdapter.items[position].getDayValue();
        //???????????? ?????? ????????? ?????? ????????? ?????? ????????? ????????????
        monthAdapter.setSelectedPosition(position);
        monthAdapter.notifyDataSetChanged();
        selectedPosition = position;

        if (curItem.getDayValue() != 0) {

            calendarList = DBHelper.selectDateFromDatabase(calendarList, view.getContext(), currentDate);

            calendarListAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {

        switch (parent.getId()) {

            case R.id.gvCalender:

                MonthItem curItem1 = (MonthItem) monthAdapter.getItem(position);

                String currentDate = monthAdapter.curYear + "" + (monthAdapter.curMonth + 1) + "" + monthAdapter.items[position].getDayValue();

                if (curItem1.getDayValue() != 0) {

                    calendarList = DBHelper.selectDateFromDatabase(calendarList, view.getContext(), currentDate);

                    calendarListAdapter.notifyDataSetChanged();

                }

                View dialogView = View.inflate(view.getContext(), R.layout.calendar_input_data_dialog, null);

                editTextCredit = dialogView.findViewById(R.id.edtLittle);
                editTextLocation = dialogView.findViewById(R.id.editTextLocation);
                spinnerFilter = dialogView.findViewById(R.id.spinnerFilter);

                DBHelper.selectCategoryData(getContext());

                Log.e("fragment1", categoryList.toString());
                spinnerFilter.setAdapter(new ArrayAdapter<>(getContext(), R.layout.spinner_text_holder, categoryList));

                radioButtonPlus = dialogView.findViewById(R.id.rdoPlus);
                radioButtonMinus = dialogView.findViewById(R.id.rdoMinus);
                final MonthItem currentItem = (MonthItem) monthAdapter.getItem(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());

                spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        str = parent.getItemAtPosition(position).toString();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

                dialog.setView(dialogView);
                dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String currentDate = String.valueOf(monthAdapter.curYear) + (monthAdapter.curMonth + 1) + currentItem.getDayValue();
                        Toast.makeText(getContext(), currentDate, Toast.LENGTH_LONG).show();

                        if (radioButtonPlus.isChecked() && !(spinnerFilter.getSelectedItem().toString().equals("????????? ????????? ?????????")) &&
                                !(editTextCredit.getText().toString().equals(""))) {

                            DBHelper.insertIncomeData(view.getContext(), currentDate, editTextCredit.getText().toString(),
                                    radioButtonPlus.getText().toString(), spinnerFilter.getSelectedItem().toString(), editTextLocation.getText().toString());
                            calendarList = DBHelper.selectDateFromDatabase(calendarList, view.getContext(), currentDate);

                        } else if (radioButtonMinus.isChecked() && !(spinnerFilter.getSelectedItem().toString().equals("????????? ????????? ?????????")) &&
                                !(editTextCredit.getText().toString().equals(""))) {

                            DBHelper.insertSpentData(view.getContext(), currentDate, editTextCredit.getText().toString(),
                                    radioButtonMinus.getText().toString(), spinnerFilter.getSelectedItem().toString(), editTextLocation.getText().toString());
                            calendarList = DBHelper.selectDateFromDatabase(calendarList, view.getContext(), currentDate);

                        } else {

                            Toast.makeText(getContext(), "???????????? ?????? ????????? ????????????", Toast.LENGTH_LONG).show();

                        }

                        calendarListAdapter.notifyDataSetChanged();
                        gridViewClickEvent(sParent , selectedPosition);

                    }
                });

                dialog.setNegativeButton("??????", null);
                dialog.show();

                break;
        }

        return false;
    }

    // ??? ?????? ????????? ??????
    private void setYearMonth() {

        String yearMonth = monthAdapter.curYear + "???   " + (monthAdapter.curMonth + 1) + " ??? ";
        textViewYear.setText(yearMonth);

    }

}