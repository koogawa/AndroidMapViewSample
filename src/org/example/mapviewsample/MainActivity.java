package org.example.mapviewsample;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import android.os.Bundle;

public class MainActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MapView mapView = (MapView)findViewById(R.id.MapView01);
		
		mapView.setClickable(true);
		
		mapView.setBuiltInZoomControls(true);
	}

	protected boolean isRouteDisplayed()
	{
		return false;
	}
}
