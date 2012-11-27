package org.imaginationforpeople.android.shake;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.FloatMath;

public class ShakeEventListener implements SensorEventListener {
	public final static int SHAKE_LIMIT = 15;
	public final static int LITTLE_SHAKE_LIMIT = 5;
	
	private SensorManager mSensorManager;
	private float mAccel = 0.00f;
	private float mAccelCurrent = SensorManager.GRAVITY_EARTH;
	private float mAccelLast = SensorManager.GRAVITY_EARTH;
	
	private ShakeListener listener;
	
	public interface ShakeListener {
		public void onShake();
		public void onLittleShake();
	}
	
	public ShakeEventListener(ShakeListener l) {
		Activity a = (Activity) l;
		mSensorManager = (SensorManager) a.getSystemService(Context.SENSOR_SERVICE);
		listener = l;
		registerListener();
	}
	
	public ShakeEventListener(Activity a, ShakeListener l) {
		mSensorManager = (SensorManager) a.getSystemService(Context.SENSOR_SERVICE);
		listener = l;
		registerListener();
	}
	
	public void registerListener() {
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void unregisterListener() {
		mSensorManager.unregisterListener(this);
	}

	public void onSensorChanged(SensorEvent se) {
		float x = se.values[0];
		float y = se.values[1];
		float z = se.values[2];
		mAccelLast = mAccelCurrent;
		mAccelCurrent = (float) FloatMath.sqrt(x*x + y*y + z*z);
		float delta = mAccelCurrent - mAccelLast;
		mAccel = mAccel * 0.9f + delta;
		if(mAccel > SHAKE_LIMIT)
			listener.onShake();
		else if(mAccel > LITTLE_SHAKE_LIMIT)
			listener.onLittleShake();
	}
	
	public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
