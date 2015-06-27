package com.shyam.lastminutelauncher.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.shyam.lastminutelauncher.model.AppDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shyam on 27/6/15.
 */
public class AppLoader {

    private Context mContext;

    public AppLoader(Context context) {
        mContext = context;
    }

    public ArrayList<AppDetail> getApps() {
        PackageManager pm = mContext.getPackageManager();
        ArrayList<AppDetail> apps = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = pm.queryIntentActivities(intent, 0);
        for(ResolveInfo ri: availableActivities) {
            AppDetail app = new AppDetail();
            app.setLabel(ri.loadLabel(pm));
            app.setName(ri.activityInfo.packageName);
            app.setIcon(ri.activityInfo.loadIcon(pm));
            apps.add(app);
        }

        return apps;
    }

}
