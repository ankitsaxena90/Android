package com.gps;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Intent;


import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import android.widget.Toast;

public class MyMenu extends Activity {
	public String phone;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	String value1;
	Intent i;
	
	//protected LocationManager myManager;
	
	MapView mapView2;
	
	

	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		
		//myManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		//myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,6000,100,(LocationListener)this);
		//final Location myLocation=myManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		
		
		Button Start_Tracking = (Button) findViewById(R.id.button1); 
		Start_Tracking.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(MyMenu.this, GpsTrackerNewActivity.class);
				startActivity(intent);
			}
		});
	
	    Button msg_Tracking = (Button) findViewById (R.id.button2);
	    msg_Tracking.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Bundle extras = getIntent().getExtras();
				phone = extras.getString("phone");
				
				sendSMS(phone, "givelocation");
						
			}
		});
	
	    Button get_Route = (Button) findViewById (R.id.button4);
	    get_Route.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				
				 
				// double lati = myLocation.getLatitude();
				// double longi = myLocation.getLongitude();
				 double lat= Double.valueOf(execute("latitude"));
     	    	 double lon=Double.valueOf(execute("longitude"));
     	    	 
     	    	 Toast.makeText(MyMenu.this, String.valueOf(lat)+String.valueOf(lon), Toast.LENGTH_LONG).show();
     	    	Intent i=new Intent(MyMenu.this,MyMapActivity.class);
     	    	 i.putExtra("dlon",lon);
     	    	 i.putExtra("dlat",lat);
     	    	// i.putExtra("slon", longi);
     	    //	i.putExtra("slat", lati);
     	    	 startActivity(i);
				
			}
		});
	
	
	    Button get_History = (Button) findViewById (R.id.button3);
	    get_History.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				i = new Intent(MyMenu.this, Timedate.class);
				startActivity(i);
				
				
				
			}
		});
	    
	    Button myLocation = (Button) findViewById(R.id.button6);
	    myLocation.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentNew = new Intent(MyMenu.this, myTracking.class);
				startActivity(intentNew);
			}
		});
	    
	    
	    
	    
	}
	
	
	 // updates the date we display in the TextView
   
    
   

	 private void sendSMS(String phoneNumber, String message)
	 {        
	     Log.v("phoneNumber",phoneNumber);
	     Log.v("MEssage",message);
	                 
	     SmsManager sms = SmsManager.getDefault();
	     sms.sendTextMessage(phoneNumber, null, message, null, null);        
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

					
					value1 = jObject.getString(data);
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
			Toast.makeText(MyMenu.this, "Request failed: " + t.toString(),
					Toast.LENGTH_LONG).show();

}
	  
	
	return value1;
     
}


}
