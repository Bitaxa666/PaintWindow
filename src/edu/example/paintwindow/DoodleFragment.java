package edu.example.paintwindow;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class DoodleFragment extends Fragment 
{
	private DoodleView doodleView;
	private float acceleration;
	private float currentAcceleration;
	private float lastAcceleration;
	private boolean dialogOnScreen=false;
	
	private static final int ACCELERATION_THRESHOLD = 100000;
	private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE=1;
	private String folderToSave;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{		
		super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_doodle, container, false);
		setHasOptionsMenu(true);
		
		doodleView = (DoodleView) v.findViewById(R.id.doodleView);
		
		acceleration=0.00f;
		currentAcceleration = SensorManager.GRAVITY_EARTH;
		lastAcceleration=SensorManager.GRAVITY_EARTH;
		folderToSave = Environment.getExternalStorageDirectory().toString();
		return v;
	}

	@Override
	public void onStart()
	{	
		super.onStart();
		enableAccelerometrListening();
		
	}
	public void enableAccelerometrListening()
	{
		SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		
	}

	@Override
	public void onPause() 
	{
		super.onPause();
		disableAccelerometerListening();
	}
	private void disableAccelerometerListening()
	{
		SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
		sensorManager.unregisterListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
	}
	
	private SensorEventListener sensorEventListener = new SensorEventListener()
	{
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			if(!dialogOnScreen)
			{
				float x=event.values[0];
				float y=event.values[1];
				float z = event.values[2];
				
				lastAcceleration=currentAcceleration;
				currentAcceleration = x * x + y * y + z * z;
				acceleration = currentAcceleration * (currentAcceleration - lastAcceleration);
				if(acceleration > ACCELERATION_THRESHOLD)
					confirmErase();
				
			}
		}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy)
	{
		
	}
};
private void confirmErase()
{
	EraseImageDialogFragment fragment = new EraseImageDialogFragment();
	fragment.show(getFragmentManager(), "erase dialog");
}

@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) 
{
	super.onCreateOptionsMenu(menu, inflater);
	inflater.inflate(R.menu.doodle_fragment_menu, menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item)
{
	switch (item.getItemId())
	{
	case R.id.color:
		ColorDialogFragment colorDialog = new ColorDialogFragment();
		colorDialog.show(getFragmentManager(), "color dialog");
		return true;
	case R.id.lineWidth:
		LineWidthDialogFragment widthdialog= new LineWidthDialogFragment();
		widthdialog.show(getFragmentManager(), "line width dialog");
		return true;
	case R.id.delete_drawing:
		doodleView.setDrawingColor(Color.WHITE);
		return true;
	case R.id.clear:
		confirmErase();
		return true;
	case R.id.save:
		doodleView.saveImage(folderToSave);
		return true;
	case R.id.print:
		doodleView.printImage();
		return true;		
	}
	
	return super.onOptionsItemSelected(item);
	
}
public DoodleView getDoodleView()
{
	return doodleView;
}
public void setDialogOnScreen(boolean visible)
{
	dialogOnScreen = visible;
}
	

}
