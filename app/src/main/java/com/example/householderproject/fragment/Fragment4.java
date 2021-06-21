package com.example.householderproject.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.householderproject.MainActivity;
import com.example.householderproject.R;
import com.example.householderproject.model.FlowLayout;
import com.example.householderproject.util.DBHelper;

import static com.example.householderproject.MainActivity.categoryList;
import static com.example.householderproject.fragment.Fragment1.calendarList;
import static com.example.householderproject.fragment.Fragment1.calendarListAdapter;

public class Fragment4 extends Fragment implements View.OnClickListener {

    private View view;
    private LinearLayout line1, line4;
    private ImageButton customDenseGreen, customPink, customGreen, customWhite, customBrightGreen, customDark, customGray;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.e("fragment", "onCreateView");

        view = inflater.inflate(R.layout.activity_setting, container, false);

        line1 = view.findViewById(R.id.line1);
        line4 = view.findViewById(R.id.line4);

        customDenseGreen = view.findViewById(R.id.customDenseGreen);
        customPink = view.findViewById(R.id.customPink);
        customGreen = view.findViewById(R.id.customGreen);
        customWhite = view.findViewById(R.id.customWhite);
        customBrightGreen = view.findViewById(R.id.customBrightGreen);
        customDark = view.findViewById(R.id.customDark);
        customGray = view.findViewById(R.id.customGray);

        line1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final View dialogView = View.inflate(v.getContext(), R.layout.setting_category_dialog, null);
                final EditText editTextAdd = dialogView.findViewById(R.id.editTextAdd);
                final Button btnAdd = dialogView.findViewById(R.id.btnAdd);
                final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
                final FlowLayout dialogFlowLayout = dialogView.findViewById(R.id.flowlayout);

                DBHelper.selectCategoryData(getContext());

                for (int i = 0; i < categoryList.size(); i++) {

                    final Button connectButton = new Button(getContext());
                    connectButton.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    connectButton.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    connectButton.setText(categoryList.get(i));
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(10, 10);
                    connectButton.setLayoutParams(params);
                    dialogFlowLayout.addView(connectButton);

                    connectButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            editTextAdd.setText(connectButton.getText().toString());

                        }

                    });

                }

                btnAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final Button connectButton = new Button(getContext());

                        for (int i = 0; i < categoryList.size(); i++) {

                            if (editTextAdd.getText().toString().equals(categoryList.get(i))) {

                                return;

                            }

                        }

                        if(editTextAdd.getText().equals("")) {

                            Toast.makeText(getContext(), "필터를 입력해 주세요.", Toast.LENGTH_LONG).show();

                            return;

                        }

                        String stringAddButton = editTextAdd.getText().toString();
                        connectButton.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                        connectButton.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                        connectButton.setText(stringAddButton);
                        FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(10, 10);
                        connectButton.setLayoutParams(params);
                        dialogFlowLayout.addView(connectButton);

                        DBHelper.insertCategoryData(getContext(), editTextAdd.getText().toString());

                        DBHelper.selectCategoryData(getContext());

                        connectButton.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                editTextAdd.setText(connectButton.getText().toString());

                            }

                        });


                    }

                });

                btnDelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        String editTextValue = editTextAdd.getText().toString();

                        DBHelper dbHelper1 = new DBHelper(getContext());
                        SQLiteDatabase sqLiteDatabase1 = dbHelper1.getWritableDatabase();
                        sqLiteDatabase1.execSQL("DELETE FROM spinnerTBL WHERE spinnercategory = '" + editTextValue + "';");
                        sqLiteDatabase1.close();

                        dialogFlowLayout.removeAllViews();

                        DBHelper.selectCategoryData(getContext());

                        for (int i = 0; i < categoryList.size(); i++) {

                            final Button connectButton = new Button(getContext());
                            connectButton.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                            connectButton.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                            connectButton.setText(categoryList.get(i));
                            FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(10, 10);
                            connectButton.setLayoutParams(params);
                            dialogFlowLayout.addView(connectButton);
                        }

                    }
                });


                DBHelper.selectCategoryData(getContext());

                dialogFlowLayout.removeAllViews();

                for (int i = 0; i < categoryList.size(); i++) {

                    final Button connectButton = new Button(getContext());
                    connectButton.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    connectButton.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    connectButton.setText(categoryList.get(i));
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(10, 10);
                    connectButton.setLayoutParams(params);
                    dialogFlowLayout.addView(connectButton);

                    connectButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            editTextAdd.setText(connectButton.getText().toString());

                        }

                    });

                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setView(dialogView);
                dialog.setPositiveButton("저장", null);
                dialog.setNegativeButton("취소", null);
                dialog.show();

                AnimationEffect(view);

            }

        });



        line4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("RESET");
                alert.setMessage("정말 초기화를 하시겠습니까? \n초기화를 하면 모든 값들은 기본으로 설정됩니다");
                alert.setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DBHelper dbHelper = new DBHelper(getContext());
                        SQLiteDatabase sqLiteOpenHelper = dbHelper.getReadableDatabase();
                        dbHelper.onUpgrade(sqLiteOpenHelper, 1, 2);

                        ((MainActivity)getActivity()).replaceFragment(Fragment1.newInstance());

                        dialog.dismiss();

                    }

                });

                alert.show();

            }

        });

        // 테마색 변경을 위한 이벤트
        customDenseGreen.setOnClickListener(this);
        customPink.setOnClickListener(this);
        customGreen.setOnClickListener(this);
        customWhite.setOnClickListener(this);
        customBrightGreen.setOnClickListener(this);
        customDark.setOnClickListener(this);
        customGray.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customDenseGreen:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_DENSE_GREEN);
                break;
            case R.id.customPink:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_PINK);
                break;
            case R.id.customGreen:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_GREEN);
                break;
            case R.id.customWhite:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_WHITE);
                break;
            case R.id.customBrightGreen:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_BRIGHT_GREEN);
                break;
            case R.id.customDark:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_DARK);
                break;
            case R.id.customGray:
                AnimationEffect(v);
                Utils.setStatusBarColor(getActivity(), Utils.StatusBarColorType.CUSTOM_GRAY);
                break;

        }
    }
    //애니메이션 효과
    private void AnimationEffect(View v) {

        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);
        v.startAnimation(animation);

    }

    //테마 변경을 위한 클래스
    //Enum클래스형을 기반으로 한 StatusBarColorType을 선언하여 7가지 타입의 StatusBarColor를 정의, 열거형 상수와 관련된 값을 생성자를 통해 연결시켜 사용
    public static class Utils {
        public enum StatusBarColorType {
            CUSTOM_DENSE_GREEN(R.color.customDenseGreen),
            CUSTOM_PINK(R.color.customPink),
            CUSTOM_GREEN(R.color.customGreen),
            CUSTOM_WHITE(R.color.customWhite),
            CUSTOM_BRIGHT_GREEN(R.color.customBrightGreen),
            CUSTOM_DARK(R.color.customDark),
            CUSTOM_GRAY(R.color.customGray);

            private int backgroundColorId;

            StatusBarColorType(int backgroundColorId) {
                this.backgroundColorId = backgroundColorId;
            }

            public int getBackgroundColorId() {
                return backgroundColorId;
            }
        }

        //API level21에서 추가된 setStatusBarColor 함수를 이용해 열거형 상수와 연결된 값으로 StatusBar색상을 변경
        public static void setStatusBarColor(Activity activity, StatusBarColorType colorType) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorType.getBackgroundColorId()));
            }
        }
    }

}
