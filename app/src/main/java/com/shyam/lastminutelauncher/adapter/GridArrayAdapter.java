package com.shyam.lastminutelauncher.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shyam.lastminutelauncher.R;
import com.shyam.lastminutelauncher.model.AppDetail;

import java.util.List;

/**
 * Created by shyam on 28/6/15.
 */
public class GridArrayAdapter {

    private Activity mContext;
    private ArrayAdapter<AppDetail> adapter;
    private List<AppDetail> apps;

    public GridArrayAdapter(Context context, List<AppDetail> apps) {
        mContext = (Activity) context;
        this.apps = apps;
    }

    public ArrayAdapter<AppDetail> getAdapter() {
        return new ArrayAdapter<AppDetail>(mContext, R.layout.list_item, apps) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = mContext.getLayoutInflater().inflate(R.layout.list_item, null);
                }

                ImageView appIcon = (ImageView) convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).getIcon());

                TextView appLabel = (TextView) convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).getLabel());

                return convertView;
            }
        };
    }

}
