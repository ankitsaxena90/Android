package com.dream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import com.dream.Utility;


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

	
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 10;// in Meters
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
	String addresss;
	NotificationManager mNotificationManager;
	Notification notifyDetails ;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        retrieveLocationButton = (Button) findViewById(R.id.startTracking);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
    	
    	//REQUESTING LOCATION		
        locationManager.requestLocationUpdates(
    	        		LocationManager.GPS_PROVIDER, 
    	        		MINIMUM_TIME_BETWEEN_UPDATES, 
    	        		MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
    	        		new MyLocationListener()
    	        		
    	        		
    	        );
    		
        
     
		
		//CREATES NOTIFICATION
		mNotificationManager = 
	   (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        
		//Notification Details like its ICON ,its TEXT DISPLAYED when it starts , and CURRENT TIME
		notifyDetails = 
	    new Notification(R.drawable.icon,"GPS Tracker has Started!",System.currentTimeMillis());
		
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
    

    
    
	
	
	
	
	private class MyLocationListener implements LocationListener {
		/* Class My Location Listener */
		public void onLocationChanged(Location location) {
		
		
			
	
			String addr = showCurrentLocation1();
			
			
			
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
		    nameValuePairs.add(new BasicNameValuePair("address",addr));
		    
		    uti = new Utility(); 
		    uti.sendData3(nameValuePairs);
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


	String showCurrentLocation1() {
		
	

		
	        
	        //Toast.makeText(Expense.this,"FGDE" , Toast.LENGTH_LONG).show();
	        
		   
		   Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			   lat = location.getLatitude();
		      lon = location.getLongitude();
		     Toast.makeText(Gpstracker.this, String.valueOf(lat)+", "+String.valueOf(lon), Toast.LENGTH_LONG).show();
		       
		      
	        Geocoder geocoder = new Geocoder(Gpstracker.this, Locale.ENGLISH);

	        try {
	       List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

	       if(addresses != null) {
	       Address returnedAddress = addresses.get(0);
	       StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
	       for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	       strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");   
	        
	        addresss = strReturnedAddress.toString(); 
	       
	      
	      // Toast.makeText(Expense.this, "Location Tagged Successfully", Toast.LENGTH_SHORT).show();
	       
	       
	       }
	       return addresss;

	       //Toast.makeText(Expense.this, strReturnedAddress.toString(), Toast.LENGTH_LONG).show();
	       
	       
	       
	       }
	       else{
	    	Toast.makeText(Gpstracker.this, "Address not available", Toast.LENGTH_LONG).show();
	    	return "Address not available";
	    	
	      }
	     } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      Toast.makeText(Gpstracker.this,"Exception Occured, try again", Toast.LENGTH_LONG).show();
	      return "Address not available";
	      
	     }
		 	
	}
	
	
	
}

