package com.shyam.lastminutelauncher;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.shyam.lastminutelauncher.adapter.GridArrayAdapter;
import com.shyam.lastminutelauncher.database.DBHelper;
import com.shyam.lastminutelauncher.model.AppDetail;
import com.shyam.lastminutelauncher.utils.AppLoader;
import com.shyam.lastminutelauncher.utils.AppSorter;
import com.shyam.lastminutelauncher.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private GridView gridView;
    private ArrayList<AppDetail> apps;
    private DBHelper dbHelper;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle(CalendarUtils.getFormattedDate());

        gridView = (GridView) findViewById(R.id.gridView);

        dbHelper = new DBHelper(this);

        apps = new AppLoader(this).getApps();
        apps = new AppSorter(this).getSortedApps(apps);
        ArrayAdapter<AppDetail> adapter = new GridArrayAdapter(this, apps).getAdapter();

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar.setTitle(CalendarUtils.getFormattedDate());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppDetail app = apps.get(position);
        dbHelper.updateAppHits(app);
        Intent i = getPackageManager().getLaunchIntentForPackage(app.getName().toString());
        startActivity(i);
    }
}
