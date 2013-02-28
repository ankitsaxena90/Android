package com.gps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class GpsTrackerNewActivity extends MapActivity implements LocationListener {

	LocationManager locman;
	LocationListener loclis;
	android.location.Location Location;
	private MapView map;

	List<GeoPoint> geoPointsArray = new ArrayList<GeoPoint>();
	private MapController controller;
	String provider = LocationManager.GPS_PROVIDER;
	double lat;
	double lon; 
	Utility uti;
	String data; 
	String hour;
	String minute;
	String date;
	String month;
	Double latitude;
	Double longitude;
	Double prevLatitude;
	Double prevLongitude;
	String get;
	String value;
	String value1;
	public GeoPoint New_geopoint;
	public GeoPoint Prev_geopoint;
    HelloItemizedOverlay itemizedoverlay;
    Handler handler = new Handler();
    TextView t;
    Thread thre;
   // GeoPoint point1 = new GeoPoint(19240000,-99120000);
   // GeoPoint point2 = new GeoPoint(19241000,-99121000);
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds

	@Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.main);
	         initMapView();
	        //initMyLocation();
	         TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
	         
	         locman = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	      
	         final MyLocationOverlay overlay = new MyLocationOverlay(this, map);
		     
			    overlay.enableCompass(); // does not work in emulator
			      
			  map.getOverlays().add(overlay);
			  
			  t=(TextView) findViewById(R.id.distance);
	         
	    
 	
}	       
 
	
	/** Find and initialize the map view. */
	   private void initMapView() {
	      map = (MapView) findViewById(R.id.mapView);
	      controller = map.getController();
	      map.setSatellite(false);
	      map.setBuiltInZoomControls(true);
	   }

	
	   
	   
	   /** Method to get data from last row **/
       public String execute(String data) {   
	   
    	   try {
 			
 			
            HttpParams httpParams = new BasicHttpParams();
 			HttpConnectionParams.setConnectionTimeout(httpParams,
 					TIMEOUT_MILLISEC);
 			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
 			
 			HttpParams p = new BasicHttpParams();
 			
 			p.setParameter("user", "1");

 			// Instantiate an HttpClient
 			HttpClient httpclient = new DefaultHttpClient(p);
 			String url = "http://bhavit.xtreemhost.com/webservice1.php?user=1&format=json";
 			HttpPost httppost = new HttpPost(url);

 			// Instantiate a GET HTTP method
 			try {
 				Log.i(getClass().getSimpleName(), "send  task - start");
 				
 				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
 						7);
 			
 			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

 					
 					value = jObject.getString(data);
 					//minute = jObject.getString("minute");
 				//	date = jObject.getString("date");
 				//	month = jObject.getString("month");
 				//	latitude= jObject.getDouble("latitude");
 				//	longitude = jObject.getDouble("longitude");
 					
 					
 					
 				
 		}
 				
 				
 	  //      Toast.makeText(GpsTrackerNewActivity.this, hour+minute+date+month+latitude+longitude , Toast.LENGTH_LONG).show();
                  
 				
		        
 			} catch (ClientProtocolException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (IOException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			

 		 
	  } catch (Throwable t) {
 			Toast.makeText(GpsTrackerNewActivity.this, "Request failed: " + t.toString(),
 					Toast.LENGTH_LONG).show();

}
	  
	
	return value;
       
}
	   
	   
	   
       public class MapOverlay extends com.google.android.maps.Overlay {

    	      private GeoPoint mGpt1;
    	      private GeoPoint mGpt2;

    	      protected MapOverlay(GeoPoint gp1, GeoPoint gp2 ) {
    	         mGpt1 = gp1;
    	         mGpt2 = gp2;
    	      }
    	      @Override
    	      public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
    	            long when) {
    	         super.draw(canvas, mapView, shadow);
    	         Paint paint;
    	         paint = new Paint();
    	         paint.setColor(Color.GREEN);
    	        
    	         paint.setAntiAlias(true);
    	         paint.setStyle(Style.STROKE);
    	         paint.setStrokeWidth(3);
    	         Point pt1 = new Point();
    	         Point pt2 = new Point();
    	         Projection projection = mapView.getProjection();
    	        projection.toPixels(mGpt1, pt1);
    	         projection.toPixels(mGpt2, pt2);
    	         canvas.drawLine(pt1.x, pt1.y, pt2.x, pt2.y, paint);
    	         return true;
    	      }
    	   }
    	   protected boolean isRouteDisplayed1() {
    	      // TODO Auto-generated method stub
    	      return false;
    	   }
    	
	   
    	   
    	   
    	   /** Method to get data from ssecond last row **/
    	   public String execute2(String data) {   
    		   
        	   try {
     			
     			
                HttpParams httpParams = new BasicHttpParams();
     			HttpConnectionParams.setConnectionTimeout(httpParams,
     					TIMEOUT_MILLISEC);
     			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
     			
     			HttpParams p = new BasicHttpParams();
     			
     			p.setParameter("user", "1");

     			// Instantiate an HttpClient
     			HttpClient httpclient = new DefaultHttpClient(p);
     			String url = "http://bhavit.xtreemhost.com/webservice2.php?user=1&format=json";
     			HttpPost httppost = new HttpPost(url);

     			// Instantiate a GET HTTP method
     			try {
     				Log.i(getClass().getSimpleName(), "send  task - start");
     				
     				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
     						7);
     			
     			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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

     					
     					value1 = jObject.getString(data);
     					//minute = jObject.getString("minute");
     				//	date = jObject.getString("date");
     				//	month = jObject.getString("month");
     				//	latitude= jObject.getDouble("latitude");
     				//	longitude = jObject.getDouble("longitude");
     					
     		
     		}
     			
     	      //  Toast.makeText(GpsTrackerNewActivity.this, hour+minute+date+month+prevLatitude+prevLongitude , Toast.LENGTH_LONG).show();
                      
     				
    		        
     			} catch (ClientProtocolException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			} catch (IOException e) {
     				// TODO Auto-generated catch block
     				e.printStackTrace();
     			}
     			

     		 
    	  } catch (Throwable t) {
     			Toast.makeText(GpsTrackerNewActivity.this, "Request failed: " + t.toString(),
     					Toast.LENGTH_LONG).show();

    }
    	  
    	
    	return value1;
           
  }
    	   
    	   
    	
    	   String getDeviceID(){
    			  TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    			  String id = telephonyManager.getDeviceId();
    			  return id;
    			  }
  
	   
	   
	   
	   
	   
	   
	   
	   @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			getMenuInflater().inflate(R.menu.mymenu, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			 
			switch(item.getItemId()) {
				
				case R.id.item_start_service:
					 thre = new Thread() {
						 public void run() {
					 try {
						
						 
						 latitude = Double.valueOf(execute("latitude"));
		  					longitude = Double.valueOf(execute("longitude"));
		  					
		  					prevLatitude = Double.valueOf(execute2("latitude"));
		  					prevLongitude = Double.valueOf(execute2("longitude"));
		  					
		  					
		  					New_geopoint = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
		  					Prev_geopoint = new GeoPoint((int)(prevLatitude*1e6),(int)(prevLongitude*1e6));
		  				 
		  					controller.animateTo(New_geopoint);
		  					
		  				 List<Overlay> mapOverlays = map.getOverlays();
		  			  Drawable drawable = GpsTrackerNewActivity.this.getResources().getDrawable(R.drawable.marker);
		  			  HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, GpsTrackerNewActivity.this);
		  			  
		  			  OverlayItem overlayitem = new OverlayItem(New_geopoint, "Hello!", "I'm Here");
		  			  
		  			  itemizedoverlay.addOverlay(overlayitem);
		  			  mapOverlays.add(itemizedoverlay);
		  			controller.setZoom(18);
		  			  controller.setCenter(New_geopoint);
		  			  MapOverlay mapOvlay = new MapOverlay(Prev_geopoint, New_geopoint);
		  		        map.getOverlays().add(mapOvlay);
						 
		  		      Prev_geopoint = New_geopoint;
						 
						 
						 final Runnable r = new Runnable()
			    		  {
			    		      public void run() 
			    		      {
			    		    	  latitude = Double.valueOf(execute("latitude"));
			  					longitude = Double.valueOf(execute("longitude"));
			  					
			  				//	prevLatitude = Double.valueOf(execute2("latitude"));
			  				//	prevLongitude = Double.valueOf(execute2("longitude"));
			  					
			  					
			  					New_geopoint = new GeoPoint((int)(latitude*1e6),(int)(longitude*1e6));
			  					//Prev_geopoint = new GeoPoint((int)(prevLatitude*1e6),(int)(prevLongitude*1e6));
			  				 
			  					controller.animateTo(New_geopoint);
			  					
			  				 List<Overlay> mapOverlays = map.getOverlays();
			  			  Drawable drawable = GpsTrackerNewActivity.this.getResources().getDrawable(R.drawable.marker);
			  			  HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, GpsTrackerNewActivity.this);
			  			  
			  			  OverlayItem overlayitem = new OverlayItem(New_geopoint, "Hello!", "I'm Here");
			  			  
			  			  itemizedoverlay.addOverlay(overlayitem);
			  			  mapOverlays.add(itemizedoverlay);
			  			controller.setZoom(18);
			  			  controller.setCenter(New_geopoint);
			  			  MapOverlay mapOvlay = new MapOverlay(Prev_geopoint, New_geopoint);
			  		        map.getOverlays().add(mapOvlay);
			  		        
			  		        
			  		        Prev_geopoint = New_geopoint;
			    		          
			  		        handler.postDelayed(this, 5000);
			    		      }
			    		  };

			    		  handler.postDelayed(r, 5000);
					 } 
					catch (Exception e){
					 e.printStackTrace();
					    }
					 
					 
					  }
					 };
					 thre.run();
			      
					return true;
					
				case R.id.item_stop_service:
					
					
					
					
					
				default:	
					return false;
		}	
			}
			




		




	public void onLocationChanged(android.location.Location location) {
	      	// TODO Auto-generated method stub
		
		}
	
	   
	   class MyOverlay extends Overlay{
	    public MyOverlay(){
	    }   
	    public void draw(Canvas canvas, MapView mapv, boolean shadow){
	    super.draw(canvas, mapv, shadow);

	    Projection projection = map.getProjection();
	    Path p = new Path();
	    for (int i = 0; i < geoPointsArray.size(); i++) {
	    if (i == geoPointsArray.size() - 1) {
	        break;
	    }
	    Point from = new Point();
	    Point to = new Point();
	    projection.toPixels(geoPointsArray.get(i), from);
	    projection.toPixels(geoPointsArray.get(i + 1), to);
	    p.moveTo(from.x, from.y);
	    p.lineTo(to.x, to.y);
	    }
	    Paint mPaint = new Paint();
	    mPaint.setStyle(Style.STROKE);
	    mPaint.setColor(0xFFFF0000);
	    mPaint.setAntiAlias(true);
	    canvas.drawPath(p, mPaint);
	    super.draw(canvas, map, shadow);
	}   

	}

	public void onProviderDisabled(String provider) {
	    // TODO Auto-generated method stub

	}
	public void onProviderEnabled(String provider) {
	    // TODO Auto-generated method stub

	}
	public void onStatusChanged(String provider, int status, Bundle extras) {
	    // TODO Auto-generated method stub

	}
	@Override
	protected boolean isRouteDisplayed() {
	    // TODO Auto-generated method stub
	    return false;
	}

	}



