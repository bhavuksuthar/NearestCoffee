package com.javacodegeeks.foursquareapiexample;

import com.example.coffe.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Button;

public class MapView extends FragmentActivity {

	// Google Map

	GPSTracker gps;

	MarkerOptions markerOptions;
	LatLng latLng;
	Button map, sat, ter, earth;
	double latitude;
	double longitude;
	GoogleMap supportMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);

		FragmentManager fmanager = getSupportFragmentManager();
		Fragment fragment = fmanager.findFragmentById(R.id.map);
		SupportMapFragment supportmapfragment = (SupportMapFragment) fragment;
		supportMap = supportmapfragment.getMap();

		supportMap.setBuildingsEnabled(true);
		supportMap.setIndoorEnabled(true);
		supportMap.setMyLocationEnabled(true);
		supportMap.setTrafficEnabled(true);

		supportMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
				AndroidFoursquare.marker[0].getPosition(), 12));

		for (int i = 0; i < AndroidFoursquare.marker.length; i++) {
			AndroidFoursquare.marker[i].icon(BitmapDescriptorFactory
					.fromResource(R.drawable.coffee));
			supportMap.addMarker(AndroidFoursquare.marker[i]);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

}
