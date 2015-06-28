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
        if(cursor != null && cursor.getCount() > 0) {
            try {
                timesOpened = cursor.getInt(cursor.getColumnIndex(SQLiteConfig.COLUMN_TIMES_OPENED));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        timesOpened++;
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteConfig.COLUMN_PACKAGE_NAME, packageName);
        contentValues.put(SQLiteConfig.COLUMN_TIMES_OPENED, timesOpened);
        contentValues.put(SQLiteConfig.COLUMN_APP_LABEL, app.getLabel().toString());

        db.update(SQLiteConfig.TABLE_NAME, contentValues, SQLiteConfig.COLUMN_PACKAGE_NAME + "=?", new String[]{packageName});

        return true;
    }

    public boolean insertAppHits(AppDetail app) {
        if(getData(app.getName().toString()) == null) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(SQLiteConfig.COLUMN_PACKAGE_NAME, app.getName().toString());
            contentValues.put(SQLiteConfig.COLUMN_TIMES_OPENED, 0);
            contentValues.put(SQLiteConfig.COLUMN_APP_LABEL, app.getLabel().toString());

            db.insert(SQLiteConfig.TABLE_NAME, null, contentValues);

            return true;
        }
        return false;
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
                + SQLiteConfig.COLUMN_TIMES_OPENED + " desc, " + SQLiteConfig.COLUMN_APP_LABEL;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void resetTimesOpened() {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteConfig.COLUMN_TIMES_OPENED, 0);
        db.update(SQLiteConfig.TABLE_NAME, contentValues, null, null);
    }
}
