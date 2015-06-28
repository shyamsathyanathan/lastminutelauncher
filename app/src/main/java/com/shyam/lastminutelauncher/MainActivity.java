package com.shyam.lastminutelauncher;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.shyam.lastminutelauncher.adapter.GridArrayAdapter;
import com.shyam.lastminutelauncher.database.DBHelper;
import com.shyam.lastminutelauncher.model.AppDetail;
import com.shyam.lastminutelauncher.utils.AppLoader;
import com.shyam.lastminutelauncher.utils.AppSorter;
import com.shyam.lastminutelauncher.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener, SensorEventListener {

    /* Here we store the current values of acceleration, one for each axis */
    private float xAccel;
    private float yAccel;
    private float zAccel;

    /* And here the previous ones */
    private float xPreviousAccel;
    private float yPreviousAccel;
    private float zPreviousAccel;

    /* Used to suppress the first shaking */
    private boolean firstUpdate = true;

    /*What acceleration difference would we assume as a rapid movement? */
    private final float shakeThreshold = 1.5f;

    /* Has a shaking motion been started (one direction) */
    private boolean shakeInitiated = false;


    private GridView gridView;
    private ArrayList<AppDetail> apps;
    private DBHelper dbHelper;
    private ActionBar actionBar;
    private ArrayAdapter<AppDetail> adapter;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle(CalendarUtils.getFormattedDate());

        gridView = (GridView) findViewById(R.id.gridView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        dbHelper = new DBHelper(this);

        apps = new AppLoader(this).getApps();
        for(AppDetail app: apps) {
            new DBHelper(this).insertAppHits(app);
        }

        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateUI();

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        updateAccelParameters(sensorEvent.values[0], sensorEvent.values[1], sensorEvent.values[2]);
        if ((!shakeInitiated) && isAccelerationChanged()) {
            shakeInitiated = true;
        } else if ((shakeInitiated) && isAccelerationChanged()) {
            executeShakeAction();
        } else if ((shakeInitiated) && (!isAccelerationChanged())) {
            shakeInitiated = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void updateAccelParameters(float xNewAccel, float yNewAccel,
                                       float zNewAccel) {
                /* we have to suppress the first change of acceleration, it results from first values being initialized with 0 */
        if (firstUpdate) {
            xPreviousAccel = xNewAccel;
            yPreviousAccel = yNewAccel;
            zPreviousAccel = zNewAccel;
            firstUpdate = false;
        } else {
            xPreviousAccel = xAccel;
            yPreviousAccel = yAccel;
            zPreviousAccel = zAccel;
        }
        xAccel = xNewAccel;
        yAccel = yNewAccel;
        zAccel = zNewAccel;
    }

    private boolean isAccelerationChanged() {
        float deltaX = Math.abs(xPreviousAccel - xAccel);
        float deltaY = Math.abs(yPreviousAccel - yAccel);
        float deltaZ = Math.abs(zPreviousAccel - zAccel);
        return (deltaX > shakeThreshold && deltaY > shakeThreshold)
                || (deltaX > shakeThreshold && deltaZ > shakeThreshold)
                || (deltaY > shakeThreshold && deltaZ > shakeThreshold);
    }

    private void executeShakeAction() {
        dbHelper.resetTimesOpened();
        updateUI();
    }

    private void updateUI() {
        actionBar.setTitle(CalendarUtils.getFormattedDate());

        apps = new AppLoader(this).getApps();
        apps = new AppSorter(this).getSortedApps(apps);
        adapter = new GridArrayAdapter(this, apps).getAdapter();

        gridView.setAdapter(adapter);
    }
}
