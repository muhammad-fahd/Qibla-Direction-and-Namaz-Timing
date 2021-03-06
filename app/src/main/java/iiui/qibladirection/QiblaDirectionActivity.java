package iiui.qibladirection;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class QiblaDirectionActivity extends AppCompatActivity implements SensorEventListener {

    ImageView img_compass;
    TextView txt_degree;
    int mydegree;
    private SensorManager mSensorManager;
    private Sensor mRotationVector, mAccelerometer, mMagnetometer;
    float [] rMat = new float[9];
    float [] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean haveSensor = false, haveSensor2 = false;
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qibla_direction);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        img_compass = (ImageView) findViewById(R.id.img_compass);
        txt_degree = (TextView) findViewById(R.id.txt_digree);

        start();
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR){
            SensorManager.getRotationMatrixFromVector(rMat,event.values);
            mydegree = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0])+360)%360);
        }
        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(event.values,0,mLastAccelerometer,0,event.values.length);
            mLastAccelerometerSet = true;
        }
        else
        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(event.values,0,mLastMagnetometer,0,event.values.length);
            mLastMagnetometerSet = true;
        }

        if(mLastMagnetometerSet && mLastAccelerometerSet){
            SensorManager.getRotationMatrix(rMat,null,mLastAccelerometer,mLastMagnetometer);
            SensorManager.getOrientation(rMat,orientation);
            mydegree = (int) ((Math.toDegrees(SensorManager.getOrientation(rMat,orientation)[0])+360)%360);

        }

        mydegree = Math.round(mydegree);
        img_compass.setRotation(-mydegree);

        String where = "NO";
        if(mydegree>=350 || mydegree<=10)
            where = "N";
        if(mydegree <350 && mydegree >280)
            where = "NW";
        if (mydegree <=256 && mydegree>253)
            where = "Qibla";
        if(mydegree<=280 && mydegree>260)
            where = "W";
        if(mydegree<=260 && mydegree>190)
            where="SW";
        if(mydegree<=190 && mydegree>170)
            where ="S";
        if(mydegree<=170 && mydegree>100)
            where = "SE";
        if(mydegree <=100 && mydegree>80)
            where = "E";
        if(mydegree<=80 && mydegree>10)
            where = "NE";

        txt_degree.setText(mydegree + "° "+where);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    public void start(){
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null){
            if(mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)==null || mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)==null){
                noSensorAlert();
            }
            else{
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this,mMagnetometer,SensorManager.SENSOR_DELAY_UI);

            }
        }
        else{
            mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this,mRotationVector,SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device does not Support Compass")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }
    public void stop(){
        if(haveSensor){
            mSensorManager.unregisterListener(this,mRotationVector);
        }
        else{
            mSensorManager.unregisterListener(this,mAccelerometer);
            mSensorManager.unregisterListener(this,mMagnetometer);
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        stop();
    }
    @Override
    protected void onResume(){
        super.onResume();
        start();
    }
}
