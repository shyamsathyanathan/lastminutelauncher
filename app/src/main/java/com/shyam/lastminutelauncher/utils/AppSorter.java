package com.shyam.lastminutelauncher.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import com.shyam.lastminutelauncher.constants.SQLiteConfig;
import com.shyam.lastminutelauncher.database.DBHelper;
import com.shyam.lastminutelauncher.model.AppDetail;

import java.util.ArrayList;

/**
 * Created by shyam on 28/6/15.
 */
public class AppSorter {

    private Context mContext;

    public AppSorter(Context context) {
        this.mContext = context;
    }

    public ArrayList<AppDetail> getSortedApps(ArrayList<AppDetail> apps) {
        ArrayList<AppDetail> sortedApps = new ArrayList<>();
        Cursor cursor = new DBHelper(mContext).getAllData();
        if(cursor != null) {
            try {
                cursor.moveToFirst();
                do {
                    AppDetail app = new AppDetail();
                    app.setLabel(cursor.getString(cursor.getColumnIndex(SQLiteConfig.COLUMN_APP_LABEL)));
                    app.setName(cursor.getString(cursor.getColumnIndex(SQLiteConfig.COLUMN_PACKAGE_NAME)));
                    app.setIcon(getIconFromName(app.getName().toString(), apps));
                    sortedApps.add(app);
                } while (cursor.moveToNext());
            } catch (Exception e) {
                return apps;
            }
        }

        return sortedApps;
    }

    private Drawable getIconFromName(String packageName, ArrayList<AppDetail> apps) {
        for(AppDetail app: apps) {
            if(app.getName().equals(packageName)) {
                return app.getIcon();
            }
        }
        return null;
    }

}
