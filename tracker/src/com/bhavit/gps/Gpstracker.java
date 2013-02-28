package com.bhavit.gps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.bhavit.gps.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

    
public class Gpstracker extends Activity {

	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 50; // in Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 5000; // in Milliseconds
	private static final int SIMPLE_NOTFICATION_ID = 100;
	
	protected LocationManager locationManager;
	
	protected Button retrieveLocationButton;
	
	double lat;
	double lon;
	String la;
	String lo;
	String hr;
	String min;
	String date;
	String month;
	Utility uti;
	NotificationManager mNotificationManager;
	Notification notifyDetails ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
    	
    	//REQUESTING LOCATION		
        locationManager.requestLocationUpdates(
    	        		LocationManager.GPS_PROVIDER, 
    	        		MINIMUM_TIME_BETWEEN_UPDATES, 
    	        		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
    	        		new MyLocationListener()
    	        		
    	        		
    	        );
    		
        
        
		retrieveLocationButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showCurrentLocation();
			}
		}); 
		
		
		//CREATES NOTIFICATION
		mNotificationManager = 
	   (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
		//Notification Details like its ICON ,its TEXT DISPLAYED when it starts , and CURRENT TIME
		notifyDetails = 
	    new Notification(R.drawable.android,"GPS Tracker has Started!",System.currentTimeMillis());
		
		Context context = getApplicationContext();
	    
		
		CharSequence contentTitle = "GPS Tracker";  //Constant Name displayed on Notification
		      
		CharSequence contentText = "Your Device is being Tracked..";  //Additional Details about Notification
		
		Intent notificationIntent = new Intent(context,
			    Gpstracker.class);
		
		//Making PendingIntent
		final PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
			    notificationIntent, 0);
		notifyDetails.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		
		  mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails); //Starting Notification

    
    
    
		
	        
	     //  String sms_sender = getIntent().getStringExtra("sender");
	     // String sms_body = getIntent().getStringExtra("body");
	     //  long timestamp =  getIntent().getLongExtra("timestamp", 0L);
          //String Timestamp = Long.toString(timestamp);
	       
	      // showDialog(sms_sender, sms_body, Timestamp );
	    }
    
    
    
    
    
    
    

    
    
    
    
private void showDialog(String sender, String body, String timestamp ){

	

	final String display = sender + "\n"
            + timestamp+ "\n"
            + body + "\n";

	if(sender.equals(null)||timestamp.equals(null)||body.equals(null)){
		Toast.makeText(Gpstracker.this, "Nothing to display", Toast.LENGTH_SHORT).show();
	}
    else {
    // Display in Alert Dialog
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage(display)
	.setCancelable(false)
	.setPositiveButton("Reply", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
                  // reply by calling SMS program
		      
		}
	})
	.setNegativeButton("Close", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int id) {
                  // go back to the phone home screen
	             
		}
	});
	AlertDialog alert = builder.create();
	alert.show();

	}
	}
    
	
    
    
    
    
    protected void showCurrentLocation() {
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		

	    lat = location.getLatitude();
        lon = location.getLongitude();
		
        
	if (location != null) {
			String message = String.format("Current Location \n Longitude: %1$s \n Latitude: %2$s",lat,lon);
			
			
			Toast.makeText(Gpstracker.this, message,
					Toast.LENGTH_SHORT).show();
			
	 		
	   }
	
        la=String.valueOf(lat);
		lo=String.valueOf(lon);
		hr=String.valueOf(new Date().getHours());
		min=String.valueOf(new Date().getMinutes());
		date=String.valueOf(new Date().getDate());
		month=String.valueOf(new Date().getMonth());
		
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
		   
		
	    nameValuePairs.add(new BasicNameValuePair("latitude", la));
	    nameValuePairs.add(new BasicNameValuePair("longitude",lo));
	    nameValuePairs.add(new BasicNameValuePair("hour",hr));
	    nameValuePairs.add(new BasicNameValuePair("minute",min));
	    nameValuePairs.add(new BasicNameValuePair("date",date));
	    nameValuePairs.add(new BasicNameValuePair("month",month));
	    
	    uti = new Utility(); 
	    uti.sendData(nameValuePairs);
	   
		
	}   



	
	
	
	
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location location) {
			
			lat = location.getLatitude();
	        lon = location.getLongitude();
	        
	        
			//String message = String.format(
				//	"New Location \n Longitude: %1$s \n Latitude: %2$s",
					//location.getLongitude(), location.getLatitude()
		//	);
			
			
			la=String.valueOf(lat);
			lo=String.valueOf(lon);
			hr=String.valueOf(new Date().getHours());
			min=String.valueOf(new Date().getMinutes());
			date=String.valueOf(new Date().getDate());
			month=String.valueOf(new Date().getMonth());
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);
			   
			
			nameValuePairs.add(new BasicNameValuePair("latitude", la));
		    nameValuePairs.add(new BasicNameValuePair("longitude",lo));
		    nameValuePairs.add(new BasicNameValuePair("hour",hr));
		    nameValuePairs.add(new BasicNameValuePair("minute",min));
		    nameValuePairs.add(new BasicNameValuePair("date",date));
		    nameValuePairs.add(new BasicNameValuePair("month",month));
		    
		    uti = new Utility(); 
		    uti.sendData(nameValuePairs);
			// Toast.makeText(Gpstracker.this, message, Toast.LENGTH_SHORT).show();
		}

		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(Gpstracker.this, "Provider status changed",
					Toast.LENGTH_SHORT).show();
		}

		public void onProviderDisabled(String s) {
			Toast.makeText(Gpstracker.this,
					"Provider disabled by the user. GPS turned off",
					Toast.LENGTH_LONG).show();
		}

		public void onProviderEnabled(String s) {
			Toast.makeText(Gpstracker.this,
					"Provider enabled by the user. GPS turned on",
					Toast.LENGTH_LONG).show();
		}

	}


}

