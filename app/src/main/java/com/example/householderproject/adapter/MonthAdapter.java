package com.example.householderproject.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.householderproject.R;
import com.example.householderproject.model.HouseHoldModel;
import com.example.householderproject.model.MonthItem;
import com.example.householderproject.util.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthAdapter extends BaseAdapter {

    public Context mContext;
    public MonthItem[] items;//ArrayList<MonthItem> <= 42개
    public Calendar mCalender;

    public int firstDay;//예시 -> 11월 1일 = 금요일 = 5값을 저장
    public int mStartDay;//Calender.SUNDAY =  현재 달력 시작위치 요일(알요일 부터 보여준다)
    public int startDay;//Time.SUNDAY = 현재 달력 시작위치 요일(알요일 부터 보여준다)
    public static int curYear;//현재 달력년도
    public static int curMonth;//현재 달력의 월
    public int lastDay;//현재년도 현재달 마지막일
    public int selectedPosition = -1;//명시적 초기값 = -1
    public ArrayList<HouseHoldModel> list = new ArrayList<>();
    private int day;
    private int month;
    private int year;

    int selectedposition = -1;

    public MonthAdapter() {
    }

    public MonthAdapter(Context mContext, int position) {
        //달력을 계산할 내용으로 셋팅진행
        this.mContext = mContext;
        //-1값을 준다
        selectedPosition = -1;
        init();
    }

    public MonthAdapter(Context context, AttributeSet attributeSet) {
        mContext = context;
        init();
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }


    //**********************************************************************************************
    @Override
    public int getCount() {
        return 42;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.calendar_inner_view_holder, null);
        }
        TextView txtDate = convertView.findViewById(R.id.txtDate);
        ImageView image1 = convertView.findViewById(R.id.image1);

        //현재 위치에 값이 없을때 이미지는 보여주지 않음 == 1~30,30을 표현 해주는 값
        if (items[position].getDayValue() == 0) {

            txtDate.setText("");
            image1.setVisibility(View.INVISIBLE);

        } else {

            String date = curYear + "" + (curMonth + 1) + "" + items[position].getDayValue();

            DBHelper myDBHelper = new DBHelper(parent.getContext());
            //쿼리문 실행
            SQLiteDatabase sqlDB = myDBHelper.getReadableDatabase();
            Cursor cursor;
            //날짜로 검색한 모든데이타(디비에 있는 데이타)를 가져온다
            cursor = sqlDB.rawQuery("SELECT * FROM calenderTBL WHERE date = '" + date + "';", null);

            //해당하는 날짜에 값이 생기면 이미지를 보여주는 여부
            if (cursor.getCount() != 0) {
                image1.setVisibility(View.VISIBLE);
            } else {
                image1.setVisibility(View.INVISIBLE);
            }

            cursor.close();
            sqlDB.close();

            txtDate.setText(String.valueOf(items[position].getDayValue()));

        }

        int columnIndex = position % 7;
        //컬럼 인덱스가 0일때 일요일에 해당
        if (columnIndex == 0) {
            txtDate.setTextColor(Color.RED);
            //컬럼인덱스가 6일때 토요일에 해당
        } else if (columnIndex == 6) {
            txtDate.setTextColor(Color.BLUE);
            //컬럼 인덱스가 그외의 값이면 월요일부터 금요일까지를 가르킴
        } else {
            txtDate.setTextColor(mContext.getResources().getColor(R.color.customGray));
        }
        //현재 날짜에 진하게 글씨 표현 하기
        if ((day == items[position].getDayValue()) && (month==mCalender.get(Calendar.MONTH))&&(year ==mCalender.get(Calendar.YEAR))) {
            txtDate.setTextColor(mContext.getResources().getColor(R.color.customDark));
            /*txtDate.setTypeface(null, Typeface.BOLD);*/
        }
        //현재 보여주는 창에서 비교해준다
        if (items[position].getDayValue() == 0) {
            return convertView;
        } else {
            convertView.setActivated(selectedPosition != -1 && position == selectedPosition);
        }
        return convertView;
    }

    //**********************************************************************************************

    private void init() {
        //전체 6주를 표현하고자함. 총42개 배열을 선언함.
        items = new MonthItem[42];
        //현재 년,월,일,시간을 가져옴
        mCalender = Calendar.getInstance();
        //더 정확한 현재 날짜를 가져옴
        mCalender.setTimeInMillis(System.currentTimeMillis());
        day = mCalender.get(Calendar.DAY_OF_MONTH);
        month = mCalender.get(Calendar.MONTH);
        year = mCalender.get(Calendar.YEAR);

        //11월달, 1일~30일 1일 = 금요일, 달력의 시작점(우리나라 = 일요일 부터 시작)
        //년도, 월, 마지막일(윤년), 1일 -> 요일위치, 달력위치(월,일,토 중 선택);
        recalculate();
        //MonthItem객체 배열에 넣어줘야 함
        resetDayNumbers();
    }

    //각자 나라에 맞게 요일을 숫자로 고쳐서 첫째날을 표현 하는 함수
    private int getFirstDay(int dayOfWeek) {
        // firstDay(5)=dayOfWeek = Calendar.FRIDAY
        int result = 0;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                result = 0;
                break;
            case Calendar.MONDAY:
                result = 1;
                break;
            case Calendar.THURSDAY:
                result = 2;
                break;
            case Calendar.WEDNESDAY:
                result = 3;
                break;
            case Calendar.TUESDAY:
                result = 4;
                break;
            case Calendar.FRIDAY:
                result = 5;
                break;
            case Calendar.SATURDAY:
                result = 6;
                break;
        }
        return result;
    }

    //현재 년도의 현재 달 마지막일을 구하기
    private int getMonthLastDay(int curYear, int curMonth) {
        switch (curMonth + 1) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return (31);
            case 4:
            case 6:
            case 9:
            case 11:
                return (30);
            default:
                if (((curYear % 4 == 0) && (curYear % 100 != 0)) || (curYear % 400 == 0)) {
                    return (29);//2월의 윤년계산
                } else {
                    return (28);
                }
        }
    }

    private int getFirstDayOfWeek() {
        //현재 시스템에 주는 값
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        switch (startDay) {
            case Calendar.MONDAY:
                return Time.MONDAY;
            case Calendar.SATURDAY:
                return Time.SATURDAY;
            case Calendar.SUNDAY:
                return Time.SUNDAY;
        }
        return 0;
    }

    private void recalculate() {

        //현재(2019년 11월 27일)월과 날짜를 기준으로 해서 11월 1일로 셋팅
        mCalender.set(Calendar.DAY_OF_MONTH, 1);

        //calendar.get(Calendar.DAY_OF_WEEK)은 calendar가 가르키는 특정 날짜가 무슨 요일인지 알기 위해 사용함 일요일:0 월요일:1 ~ 토요일: 6
        //여기서는 11월1일이 무슨요일인지 저장함  여기서는 dayOfWeek = Calendar.FRIDAY
        int dayOfWeek = mCalender.get(Calendar.DAY_OF_WEEK);
        firstDay = getFirstDay(dayOfWeek);

        //현재 시스템의 달력에 첫번째를 무슨 요일로 시작하는지
        //예) 일요일로 시작하는 달력, 토요일로 시작하는 달력, 월요일로 시작하는 달력
        //mStraDay = Time.SUNDAY
        mStartDay = mCalender.getFirstDayOfWeek();

        //현재 년도
        curYear = mCalender.get(Calendar.YEAR);
        //현재 달
        curMonth = mCalender.get(Calendar.MONTH);
        //현재 년도의 현재달 마지막일
        lastDay = getMonthLastDay(curYear, curMonth);

        // mCalender.getFirstDayOfWeek(); -> 우리가 계산하는 방식
        startDay = getFirstDayOfWeek();

    }

    private void resetDayNumbers() {
        for (int i = 0; i < 42; i++) {
            // calculate day number  firstDay(5)=dayOfWeek = Calendar.FRIDAY(해당되는 달의 1일 의 요일 위치)
            int dayNumber = (i + 1) - firstDay;// 1 - 5 = -4
            if (dayNumber < 1 || dayNumber > lastDay) {
                dayNumber = 0;
            }
            //인플레이터가 없어서 매치를 해서 값을 넣는 순간임
            //이 자체가 인플레이터
            items[i] = new MonthItem(dayNumber);
        }
    }

    //이전달 구하기
    public void setPreviousMonth() {
        //현재 11월달이라면  -> 10월 달로 감 / 현재가 10월 달이라면 -> 9월달로 감
        mCalender.add(Calendar.MONTH, -1);//recalculate에 보면 11월 1일로 바꿨음
        //시작날짜, 끝날짜,시작하는요일 등등 계산
        recalculate();
        //42개의 객체를 만듬
        resetDayNumbers();
        selectedPosition = -1;
    }

    //다음달 구하기
    public void setNextMonth() {
        //현재 11월달이라면  -> 12월 달로 감 / 현재가 12월 달이라면 -> 1월달로 감
        mCalender.add(Calendar.MONTH, 1);//recalculate에 보면 11월 1일로 바꿨음
        //시작날짜, 끝날짜,시작하는요일 등등 계산
        recalculate();
        //42개의 객체를 만듬
        resetDayNumbers();
        selectedPosition = -1;
    }
}
