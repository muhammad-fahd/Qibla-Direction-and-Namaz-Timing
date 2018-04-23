package iiui.qibladirection;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button qiblaDirect , prayerTime;
    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qiblaDirect = (Button) findViewById(R.id.btnQiblaDirection);
        prayerTime = (Button) findViewById(R.id.btnPrayerTiming);



        qiblaDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext() , QiblaDirectionActivity.class);
                startActivity(i);
            }
        });

        //prayer timing activity
        prayerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if permission renot required then do this
                //if (!runTime_CheckingPermission()) {
                //    serviceIntent = new Intent(getApplicationContext() , GPS_Service.class);
                //    startService(serviceIntent);
                    Intent intent = new Intent(MainActivity.this , PrayerTimeActivity.class);
                    startActivity(intent);

                //}
                //else {
                    //if permission required then get permission
                //    runTime_CheckingPermission();
               // }
            }
        });

    }

    private boolean runTime_CheckingPermission() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this ,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,} , 100);
            return true;
        }
        return false;
    }
}
