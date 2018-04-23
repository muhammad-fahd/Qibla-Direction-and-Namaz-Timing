package iiui.qibladirection;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PrayerTimeActivity extends AppCompatActivity {

    private double latitude, longitude, timezone;
    PrayTime prayTime;
    Button refresh;
    private LocationManager locationManager;
    private LocationListener locationListener;

    /*private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    latitude = (double) intent.getExtras().get("Latitude_from_Service");
                    longitude = (double) intent.getExtras().get("Longitude_from_Service");
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("Location_Update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    } */

    TextView fajarTitel, fajarTime, sunriseTitle, sunriseTime, dhuhurTitle, dhuhurTime;
    TextView asarTitle, asarTime, sunsetTitle, sunsetTime, maghribTitle, maghribTime;
    TextView ishaTitle, ishaTime, lat, lon;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_time);
        refresh = (Button) findViewById(R.id.btnRefresh);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                timezone = Calendar.getInstance().getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis())/(1000*60*60);

                prayTime = new PrayTime();
                prayTime.setTimeFormat(prayTime.Time12);
                prayTime.setCalcMethod(prayTime.Makkah);
                prayTime.setAsrJuristic(prayTime.Shafii);
                prayTime.setAdjustHighLats(prayTime.AngleBased);

                int[] offset = { 0, 0 , 0, 0, 0, 0, 0 };
                prayTime.tune(offset);

                Date now = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(now);

                ArrayList prayerTimes = prayTime.getPrayerTimes(calendar , latitude , longitude , timezone);
                ArrayList pryerNames = prayTime.getTimeNames();

                //Latitude and Longitude
                lat = (TextView) findViewById(R.id.tvLatitude);
                lat.setText(Double.toString(latitude));

                lon = (TextView) findViewById(R.id.tvLongitude);
                lon.setText(Double.toString(longitude));
                ////////////////////////////////////////////



                //prayers name textview
                fajarTitel = (TextView) findViewById(R.id.tvFajarTitle);
                sunriseTitle = (TextView) findViewById(R.id.tvSunriseTitle);
                dhuhurTitle = (TextView) findViewById(R.id.tvDhuhurTitle);
                asarTitle = (TextView) findViewById(R.id.tvAsarTitle);
                sunsetTitle = (TextView) findViewById(R.id.tvSunsetTitle);
                maghribTitle = (TextView) findViewById(R.id.tvMaghribTitle);
                ishaTitle = (TextView) findViewById(R.id.tvIshaTitle);

                //Prayers Names
                fajarTitel.setText(pryerNames.get(0).toString());
                sunriseTitle.setText(pryerNames.get(1).toString());
                dhuhurTitle.setText(pryerNames.get(2).toString());
                asarTitle.setText(pryerNames.get(3).toString());
                sunsetTitle.setText(pryerNames.get(4).toString());
                maghribTitle.setText(pryerNames.get(5).toString());
                ishaTitle.setText(pryerNames.get(6).toString());


                //Prayers Time textview
                fajarTime = (TextView) findViewById(R.id.tvFajarTime);
                sunriseTime = (TextView) findViewById(R.id.tvSunriseTime);
                dhuhurTime = (TextView) findViewById(R.id.tvDhuhurTime);
                asarTime = (TextView) findViewById(R.id.tvAsarTime);
                sunsetTime = (TextView) findViewById(R.id.tvSunsetTime);
                maghribTime = (TextView) findViewById(R.id.tvMaghribTime);
                ishaTime = (TextView) findViewById(R.id.tvIshaTime);

                //Prayers Times
                fajarTime.setText(prayerTimes.get(0).toString());
                sunriseTime.setText(prayerTimes.get(1).toString());
                dhuhurTime.setText(prayerTimes.get(2).toString());
                asarTime.setText(prayerTimes.get(3).toString());
                sunsetTime.setText(prayerTimes.get(4).toString());
                maghribTime.setText(prayerTimes.get(5).toString());
                ishaTime.setText(prayerTimes.get(6).toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                } , 10 );
                return;
            }
        }else {

            configureButton();
        }



        //islamabad lat long
        //latitude = 33.659384;
        //longitude = 73.024782;

        //##############################################


        timezone = Calendar.getInstance().getTimeZone().getOffset(Calendar.getInstance().getTimeInMillis())/(1000*60*60);

        prayTime = new PrayTime();
        prayTime.setTimeFormat(prayTime.Time12);
        prayTime.setCalcMethod(prayTime.Makkah);
        prayTime.setAsrJuristic(prayTime.Shafii);
        prayTime.setAdjustHighLats(prayTime.AngleBased);

        int[] offset = { 0, 0 , 0, 0, 0, 0, 0 };
        prayTime.tune(offset);

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        ArrayList prayerTimes = prayTime.getPrayerTimes(calendar , latitude , longitude , timezone);
        ArrayList pryerNames = prayTime.getTimeNames();

        //Latitude and Longitude
        lat = (TextView) findViewById(R.id.tvLatitude);
        lat.setText(Double.toString(latitude));

        lon = (TextView) findViewById(R.id.tvLongitude);
        lon.setText(Double.toString(longitude));
        ////////////////////////////////////////////



        //prayers name textview
        fajarTitel = (TextView) findViewById(R.id.tvFajarTitle);
        sunriseTitle = (TextView) findViewById(R.id.tvSunriseTitle);
        dhuhurTitle = (TextView) findViewById(R.id.tvDhuhurTitle);
        asarTitle = (TextView) findViewById(R.id.tvAsarTitle);
        sunsetTitle = (TextView) findViewById(R.id.tvSunsetTitle);
        maghribTitle = (TextView) findViewById(R.id.tvMaghribTitle);
        ishaTitle = (TextView) findViewById(R.id.tvIshaTitle);

        //Prayers Names
        fajarTitel.setText(pryerNames.get(0).toString());
        sunriseTitle.setText(pryerNames.get(1).toString());
        dhuhurTitle.setText(pryerNames.get(2).toString());
        asarTitle.setText(pryerNames.get(3).toString());
        sunsetTitle.setText(pryerNames.get(4).toString());
        maghribTitle.setText(pryerNames.get(5).toString());
        ishaTitle.setText(pryerNames.get(6).toString());


        //Prayers Time textview
        fajarTime = (TextView) findViewById(R.id.tvFajarTime);
        sunriseTime = (TextView) findViewById(R.id.tvSunriseTime);
        dhuhurTime = (TextView) findViewById(R.id.tvDhuhurTime);
        asarTime = (TextView) findViewById(R.id.tvAsarTime);
        sunsetTime = (TextView) findViewById(R.id.tvSunsetTime);
        maghribTime = (TextView) findViewById(R.id.tvMaghribTime);
        ishaTime = (TextView) findViewById(R.id.tvIshaTime);

        //Prayers Times
        fajarTime.setText(prayerTimes.get(0).toString());
        sunriseTime.setText(prayerTimes.get(1).toString());
        dhuhurTime.setText(prayerTimes.get(2).toString());
        asarTime.setText(prayerTimes.get(3).toString());
        sunsetTime.setText(prayerTimes.get(4).toString());
        maghribTime.setText(prayerTimes.get(5).toString());
        ishaTime.setText(prayerTimes.get(6).toString());
    }

    private void configureButton() {
        refresh.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET
                        }, 10);
                    }
                }
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();
                }
                return;
        }
    }

}

