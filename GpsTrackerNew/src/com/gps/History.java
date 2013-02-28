package com.gps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;



import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import android.widget.TextView;
import android.widget.Toast;

import com.gps.Utility;
import com.gps.myTracking.MyOverlay;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class History extends MapActivity {
	
	Utility uti;
	static final int DATE_DIALOG_ID = 0;

	MapView mapView2;
	TextView mDateDisplay;
	int TIMEOUT_MILLISEC = 10000;
	Double longitude;
	Double latitude;
	private MapView map;
	private MapController controller;
	private GeoPoint New_geopoint;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.history);
		
		 mapView2 = (MapView) findViewById(R.id.mapview2);
		
		 Bundle extras1 = getIntent().getExtras();
			//String day1 = extras1.getString("day");
		
			
		    
		 
	
	 execute();
	
	}

	
	
	/** Method to get data from last row 
	 * @return **/
    public void execute() {   
	   
 	   try {
			
			
         HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					TIMEOUT_MILLISEC);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
			
			HttpParams p = new BasicHttpParams();
			
			p.setParameter("user", "1");

			// Instantiate an HttpClient
			HttpClient httpclient = new DefaultHttpClient(p);
			String url = "http://bhavit.xtreemhost.com/webservice4.php?user=1&format=json";
			HttpPost httppost = new HttpPost(url);

			// Instantiate a GET HTTP method
			try {
				Log.i(getClass().getSimpleName(), "send  task - start");
				
				Bundle extras = getIntent().getExtras();
				String day = extras.getString("day");
				
				String month = extras.getString("month");
				String year = extras.getString("year");
				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(7);
			   
				
			    nameValuePairs1.add(new BasicNameValuePair("date", day));
			    
			    nameValuePairs1.add(new BasicNameValuePair("month", month ));
			    nameValuePairs1.add(new BasicNameValuePair("year", year ));
				
				//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				//		7);
			
			  // httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			     httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs1));
			   
			   
			 
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				String responseBody = httpclient.execute(httppost,
						responseHandler);
				// Parse
				JSONObject json = new JSONObject(responseBody);
				JSONArray jArray = json.getJSONArray("posts");
				

				for (int i = 0; i < jArray.length(); i++) {
					
					JSONObject e = jArray.getJSONObject(i);
					String s = e.getString("post");
					JSONObject jObject = new JSONObject(s);

					
					latitude = Double.valueOf(jObject.getString("latitude"));
					 longitude = Double.valueOf(jObject.getString("longitude"));
					 
					Toast.makeText(History.this, latitude.toString()+longitude.toString() , Toast.LENGTH_LONG).show();
					//minute = jObject.getString("minute");
				//	date = jObject.getString("date");
				//	month = jObject.getString("month");
				//	latitude= jObject.getDouble("latitude");
				//	longitude = jObject.getDouble("longitude");
					 
					
					/*List<Overlay> MyOverlays = map.getOverlays();
		            MyOverlay MyOvlay = new MyOverlay(New_geopoint);
		            map.getOverlays().add(MyOvlay);*/
					
					
				
		}
				
			//	Toast.makeText(this, responseBody, Toast.LENGTH_LONG).show();	
				
				
				New_geopoint = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
				controller.animateTo(New_geopoint);
					
 				 List<Overlay> mapOverlays = map.getOverlays();
 			  Drawable drawable = History.this.getResources().getDrawable(R.drawable.marker);
 			  HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, History.this);
 			  
 			  OverlayItem overlayitem = new OverlayItem(New_geopoint, "Hello!", "I'm Here");
 			  
 			  itemizedoverlay.addOverlay(overlayitem);
 			  mapOverlays.add(itemizedoverlay);
 			controller.setZoom(18);
 			  controller.setCenter(New_geopoint);
				
               
				
		        
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		 
	  } catch (Throwable t) {
			Toast.makeText(History.this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();

}
	  
	

    
}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
