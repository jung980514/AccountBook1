package com.example.householderproject.calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.householderproject.model.MonthItem;

//부분화면
public class MonthItemView extends AppCompatTextView {
    private MonthItem item;

    public MonthItemView(Context context) {
        super(context);
        init();
    }

    public MonthItemView(Context context, AttributeSet attrs, MonthItem item) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.GRAY);
    }

    public MonthItem getItem() {
        return item;
    }

    public void setItem(MonthItem item) {
        this.item = item;

        int day = item.getDayValue();
        if (day != 0) {
            setText(String.valueOf(day));
        } else {
            setText("");
        }

    }


}