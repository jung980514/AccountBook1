package com.example.householderproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.householderproject.fragment.Fragment1;
import com.example.householderproject.fragment.Fragment2;
import com.example.householderproject.fragment.Fragment3;

import com.example.householderproject.fragment.Fragment4;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import android.util.Log;
import android.util.Base64;
import java.security.MessageDigest;
import java.util.Stack;
import android.content.pm.Signature;
import android.content.pm.PackageInfo;

import android.database.sqlite.SQLiteDatabase;

import com.example.householderproject.util.DBHelper;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_RECEIVE_PERMISSION = 1;
    public static ArrayList<String> categoryList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;

    public static Context myContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getHash();

       /* DBHelper dbHelper1 = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase1 = dbHelper1.getWritableDatabase();
        dbHelper1.onUpgrade(sqLiteDatabase1, 1, 2);
*/
        permissionCheckMethod(this);

        myContext = MainActivity.this;

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();

    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationManagerCompat.from(this).cancel(0);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_1: //fragmentManager1 화면 전환
                        setChangeFragment(0);
                        break;
                    case R.id.action_2: //fragmentManager2 화면 전환
                        setChangeFragment(1);
                        break;
                    case R.id.action_3: //fragmentManager3 화면 전환
                        setChangeFragment(2);
                        break;
                    case R.id.action_4: //fragmentManager4 화면 전환
                        setChangeFragment(3);
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_1);
    }

    //화면 전환
    private void setChangeFragment(int position) {
        //화면을 전환하기 위해서 fragmentManager 필요
        fragmentManager = getSupportFragmentManager();
        //fragmentManager의 권한을 받아서 화면 체인지 하는 Transaction 필요
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                fragmentTransaction.replace(R.id.frameLayout, fragment1);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.frameLayout, fragment2);
                fragmentTransaction.commit();
                break;
            case 2:
                fragmentTransaction.replace(R.id.frameLayout, fragment3);
                fragmentTransaction.commit();
                break;
            case 3:
                fragmentTransaction.replace(R.id.frameLayout, fragment4);
                fragmentTransaction.commit();
                break;
        }
    }

    private void permissionCheckMethod(MainActivity context) {
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(context, "Already SMS Accept", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(context, "No Sms Accept", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.RECEIVE_SMS)) {

                Toast.makeText(context, "Need SMS Accept", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_RECEIVE_PERMISSION);

            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case SMS_RECEIVE_PERMISSION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "SMS ACCEPT", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getApplicationContext(), "SMS NOT ACCEPT", Toast.LENGTH_LONG).show();

                }

                break;

        }

    }

    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment).commit();

    }
}

