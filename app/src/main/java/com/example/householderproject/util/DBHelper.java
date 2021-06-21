package com.example.householderproject.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.householderproject.model.HouseHoldModel;

import java.util.ArrayList;

import static com.example.householderproject.MainActivity.categoryList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context) {

        super(context,"calenderDB.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.e("database", "onCreate");

        db.execSQL("CREATE TABLE spinnerTBL(spinnercategory CHAR(10) PRIMARY KEY);");
        db.execSQL("CREATE TABLE calenderTBL(id INTEGER PRIMARY KEY AUTOINCREMENT , date CHAR(20), credit CHAR(10), detail CHAR(10), category CHAR(20), location CHAR(30));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS calenderTBL");
        db.execSQL("DROP TABLE IF EXISTS spinnerTBL");
        onCreate(db);

    }

    public static void selectCategoryData(Context context) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Log.e("database", "selectCategoryData");

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM spinnerTBL;", null);

        categoryList.removeAll(categoryList);

        while(cursor.moveToNext()) {

            categoryList.add(cursor.getString(0));

        }

        cursor.close();
        sqLiteDatabase.close();

    }

    public static void insertCategoryData(Context context, String category) {

        DBHelper dbHelper1 = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper1.getWritableDatabase();
        sqLiteDatabase.execSQL("INSERT INTO spinnerTBL VALUES('" + category + "');");
        sqLiteDatabase.close();

    }

    /************************
     *
     * 년도로 데이터를 검색해 온다.
     *
     * @param listViewData
     * @param context
     * @param yearOfNow
     * @param detail
     * @return ArrayList
     */
    public static ArrayList<HouseHoldModel> selectYearDateDatabase(ArrayList<HouseHoldModel> listViewData, Context context, int yearOfNow, String detail) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM calenderTBL WHERE date like '" + yearOfNow + "____' AND detail = '" + detail + "';", null);

        listViewData.removeAll(listViewData);

        while(cursor.moveToNext()) {

            listViewData.add(new HouseHoldModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));

        }

        cursor.close();
        sqLiteDatabase.close();

        return listViewData;

    }

    /*********************
     *
     * 년도와 월로 데이터를 검색해 온다.
     *
     * @param listViewData
     * @param context
     * @param yearOfNow
     * @param monthOfNow
     * @param detail
     * @return ArrayList
     */
    public static ArrayList<HouseHoldModel> selectYearAndMonthDateDatabase(ArrayList<HouseHoldModel> listViewData, Context context, int yearOfNow, int monthOfNow, String detail) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM calenderTBL WHERE date like '" + yearOfNow + "" + monthOfNow + "__' AND detail = '" + detail + "';", null);

        listViewData.removeAll(listViewData);

        while(cursor.moveToNext()) {

            listViewData.add(new HouseHoldModel(cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5)));

        }

        cursor.close();
        sqLiteDatabase.close();

        return listViewData;

    }

    /******************
     *
     * Primary Key 로 데이터를 수정한다.
     *
     * @param context
     * @param credit
     * @param detail
     * @param category
     * @param location
     * @param no
     */
    public static void updateFromNumberDatabase(Context context, String credit, String detail, String category, String location, int no) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("UPDATE calenderTBL SET credit = '" + credit + "', detail = '" + detail + "',category = '" + category + "',location = '" + location + "' WHERE id ="+ no +";");
        sqlDB.close();

    }

    /*******************
     *
     * Primary Key 로 데이터를 삭제한다.
     *
     * @param context
     * @param no
     */
    public static void deleteFromDatabase(Context context, int no) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        sqlDB.execSQL("DELETE FROM calenderTBL WHERE id = " + no + ";");
        sqlDB.close();

    }

    /*********************
     *
     * 날짜로 데이터를 검색해 온다.
     *
     * @param calendarList
     * @param context
     * @param currentDate
     * @return ArrayList
     */
    public static ArrayList<HouseHoldModel> selectDateFromDatabase(ArrayList<HouseHoldModel> calendarList, Context context, String currentDate) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM calenderTBL WHERE date = '" + currentDate + "';", null);

        calendarList.removeAll(calendarList);

        while (cursor.moveToNext()) {

            int getNo = cursor.getInt(0);
            String getDate = cursor.getString(1);
            String getCredit = cursor.getString(2);
            String getDetail = cursor.getString(3);
            String getCategory = cursor.getString(4);
            String getLocation = cursor.getString(5);

            calendarList.add(new HouseHoldModel(getNo, getDate, getCredit, getDetail, getCategory, getLocation));

        }

        cursor.close();
        sqLiteDatabase.close();

        return calendarList;

    }

    /*******************
     *
     * 데이터를 입력한다.
     *
     * @param context
     * @param currentDate
     * @param editTextCredit
     * @param radioButtonPlus
     * @param spinnerFilter
     * @param editTextLocation
     */
    public static void insertIncomeData(Context context, String currentDate, String editTextCredit, String radioButtonPlus, String spinnerFilter, String editTextLocation) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.execSQL("INSERT INTO calenderTBL VALUES(null,'" + currentDate + "','" + editTextCredit + "','"
                + radioButtonPlus + "','" + spinnerFilter + "', '" + editTextLocation + "');");

        sqLiteDatabase.close();

    }

    /***********************
     *
     * 데이터를 입력한다.
     *
     * @param context
     * @param currentDate
     * @param editTextCredit
     * @param radioButtonMinus
     * @param spinnerFilter
     * @param editTextLocation
     */
    public static void insertSpentData(Context context, String currentDate, String editTextCredit, String radioButtonMinus, String spinnerFilter, String editTextLocation) {

        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        sqLiteDatabase.execSQL("INSERT INTO calenderTBL VALUES(null,'" + currentDate + "','" + editTextCredit + "','"
                + radioButtonMinus + "','" + spinnerFilter + "', '" + editTextLocation + "');");

        sqLiteDatabase.close();

    }


}
