package com.shyam.lastminutelauncher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shyam.lastminutelauncher.constants.SQLiteConfig;
import com.shyam.lastminutelauncher.model.AppDetail;

/**
 * Created by shyam on 28/6/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context)
    {
        super(context, SQLiteConfig.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + SQLiteConfig.TABLE_NAME +
                        "(" + SQLiteConfig.COLUMN_PACKAGE_NAME + " text primary key, "
                        + SQLiteConfig.COLUMN_TIMES_OPENED + " integer, "
                        + SQLiteConfig.COLUMN_APP_LABEL + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean updateAppHits(AppDetail app) {
        String packageName = app.getName().toString();
        SQLiteDatabase db = this.getWritableDatabase();
        int timesOpened = 0;
        Cursor cursor = getData(packageName);
        if(cursor != null) {
            timesOpened = cursor.getColumnIndex(SQLiteConfig.COLUMN_TIMES_OPENED);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteConfig.COLUMN_PACKAGE_NAME, packageName);
        contentValues.put(SQLiteConfig.COLUMN_TIMES_OPENED, ++timesOpened);
        contentValues.put(SQLiteConfig.COLUMN_APP_LABEL, app.getLabel().toString());

        db.update(SQLiteConfig.TABLE_NAME, contentValues, SQLiteConfig.COLUMN_PACKAGE_NAME + "=?", new String[]{packageName});

        return true;
    }

    public Cursor getData(String packageName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + SQLiteConfig.TABLE_NAME
                + " where " + SQLiteConfig.COLUMN_PACKAGE_NAME + "='"
                + packageName + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select * from " + SQLiteConfig.TABLE_NAME + " order by "
                + SQLiteConfig.COLUMN_TIMES_OPENED + ", " + SQLiteConfig.COLUMN_APP_LABEL;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
