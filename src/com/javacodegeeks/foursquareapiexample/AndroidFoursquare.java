package com.javacodegeeks.foursquareapiexample;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.example.coffe.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AndroidFoursquare extends ListActivity {
	ArrayList<FoursquareVenue> venuesList = new ArrayList<FoursquareVenue>();

	Button btn;
	// the foursquare client_id and the client_secret

	// ============== YOU SHOULD MAKE NEW KEYS ====================//
	final String CLIENT_ID = "ACAO2JPKM1MXHQJCK45IIFKRFR2ZVL0QASMCBCG5NPJQWF2G";
	final String CLIENT_SECRET = "YZCKUYJ1WHUV2QICBXUBEILZI1DMPUIDP5SHV043O04FKBHL";

	// we will need to take the latitude and the logntitude from a certain point
	// this is the center of New York
	double latitude;
	double longitude;
	static double distanceKM;
	GPSTracker gps;

	static FoursquareVenue[] venu;

	public static MarkerOptions[] marker;

	ArrayAdapter<String> myAdapter;
	static int[] distance;
	private Dialog progressDialog;

	static ArrayList<FoursquareVenue> venuArray = new ArrayList<FoursquareVenue>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AndroidFoursquare.this, MapView.class);
				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

			}
		});

		gps = new GPSTracker(AndroidFoursquare.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			//latitude = -34.8766;
			//longitude = 138.6343;
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
		// start the AsyncTask that makes the call for the venus search.
		new fourquare().execute();
	}

	private class fourquare extends AsyncTask<View, Void, String> {

		String url;

		@Override
		protected String doInBackground(View... urls) {
			// make Call to the url
			url = makeCall("https://api.foursquare.com/v2/venues/search?client_id="
					+ CLIENT_ID
					+ "&client_secret="
					+ CLIENT_SECRET
					+ "&v=20150710&ll="
					+ latitude
					+ ","
					+ longitude
					+ "&query=coffee");
			return "";

		}

		@Override
		protected void onPreExecute() {
			AndroidFoursquare.this.progressDialog = ProgressDialog.show(
					AndroidFoursquare.this, "", "Please Wait...", true);
		}

		@Override
		protected void onPostExecute(String result) {
			if (url == null) {
				// we have an error to the call
				// we can also stop the progress bar
			} else {
				AndroidFoursquare.this.progressDialog.dismiss();
				// all things went right

				// parseFoursquare venues search result
				// venuesList =
				// (ArrayList<FoursquareVenue>)
				parseFoursquare(url);
				for (int i = 0; i < venu.length; i++) {
					venuesList.add(venu[i]);
				}

				Collections.sort(venuesList);

				List<String> listTitle = new ArrayList<String>();

				for (int i = 0; i < venuesList.size(); i++) {
					// make a list of the venus that are loaded in the list.
					// show the name, the category and the city

					if (venuesList.get(i).getDistance() != 0) {
						listTitle.add(venuesList.get(i).getName() + "\n "
								+ venuesList.get(i).getAddress() + ","
								+ venuesList.get(i).getCity() + "\n"
								+ venuesList.get(i).getDistance());
					}
				}

				// set the results to the list
				// and show them in the xml
				myAdapter = new ArrayAdapter<String>(AndroidFoursquare.this,
						R.layout.row_layout, R.id.listText, listTitle);
				setListAdapter(myAdapter);
			}
		}
	}

	public static String makeCall(String url) {

		// string buffers the url
		StringBuffer buffer_string = new StringBuffer(url);
		String replyString = "";

		// instanciate an HttpClient
		HttpClient httpclient = new DefaultHttpClient();
		// instanciate an HttpGet
		HttpGet httpget = new HttpGet(buffer_string.toString());

		try {
			// get the responce of the httpclient execution of the url
			HttpResponse response = httpclient.execute(httpget);
			InputStream is = response.getEntity().getContent();

			// buffer input stream the result
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(20);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			// the result as a string is ready for parsing
			replyString = new String(baf.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// trim the whitespaces
		return replyString.trim();
	}

	private static ArrayList<FoursquareVenue> parseFoursquare(
			final String response) {

		ArrayList<FoursquareVenue> temp = new ArrayList<FoursquareVenue>();
		try {

			int index = 0;
			// make an jsonObject in order to parse the response
			JSONObject jsonObject = new JSONObject(response);

			// make an jsonObject in order to parse the response
			if (jsonObject.has("response")) {
				if (jsonObject.getJSONObject("response").has("venues")) {
					JSONArray jsonArray = jsonObject.getJSONObject("response")
							.getJSONArray("venues");

					marker = new MarkerOptions[jsonArray.length()];

					venu = new FoursquareVenue[jsonArray.length()];

					for (int i = 0; i < jsonArray.length(); i++) {

						FoursquareVenue poi = new FoursquareVenue();

						marker[i] = new MarkerOptions();

						venu[i] = new FoursquareVenue();

						if (jsonArray.getJSONObject(i).has("name")) {

							if (jsonArray.getJSONObject(i)
									.getJSONObject("location").has("address")) {

								if (jsonArray.getJSONObject(i)
										.getJSONObject("location")
										.has("distance")) {
									if (jsonArray.getJSONObject(i)
											.getJSONObject("location")
											.has("city")) {
										poi.setName(jsonArray.getJSONObject(i)
												.getString("name"));

										LatLng latLng = new LatLng(
												Double.parseDouble(jsonArray
														.getJSONObject(i)
														.getJSONObject(
																"location")
														.getString("lat")),
												Double.parseDouble(jsonArray
														.getJSONObject(i)
														.getJSONObject(
																"location")
														.getString("lng")));

										poi.setAddress(jsonArray
												.getJSONObject(i)
												.getJSONObject("location")
												.getString("address"));

										poi.setCity(jsonArray.getJSONObject(i)
												.getJSONObject("location")
												.getString("city"));

										poi.setDistance(Integer
												.valueOf(jsonArray
														.getJSONObject(i)
														.getJSONObject(
																"location")
														.getString("distance")));
										
										venu[index] = poi;
										index++;
										
										marker[i].position(latLng);
										marker[i].title(jsonArray
												.getJSONObject(i).getString(
														"name"));

									}
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<FoursquareVenue>();
		}
		return temp;

	}
}
