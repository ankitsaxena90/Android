package com.dream;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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




import com.dream.Utility;







import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Expense extends Activity{
	
	EditText gp = null;
	EditText name = null;
	EditText cell = null;
	
	
	EditText amt = null;
	EditText spin1 = null;
	EditText spin2 = null;
	Button b = null;
	protected Button locFrom;
	Button locTo;
	Location location;
	Button client = null;
	
	
	String name1 = null;
	public String cell1 = null;
	String locFrom1 = null;
	String locTo1 = null;
	String spinner1val = null;
	String spinner2val = null;
	String amt1 = null;
	int total = 0;
	int TIMEOUT_MILLISEC = 10000; // = 10 seconds
	
	
	String valSel1 = null;
	String valSel2 = null;
	String SpinText1 = null;
	String SpinText2 = null;
	LocationManager locationManager;
	double lat;
	double lon;
	String add;
	String getName = null;
	String addFrom = "default";
	String addTo = "default";
	
	Spinner spinner1 = null;
	String[] items = new String[] {"Religare", "Citibank", "Fortis","Dreamsol"};
	String valSel = null;
	
	String clientloc = null;
	String client_lon = null;
	String client_lat = null;
	Double latTo = null;
	Double lonTo = null;
	String hr ;
	String min;
	@Override
	
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense);
		
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener ll = new mylocationlistener();
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000 , 10, ll);
		
		
		
		name = (EditText) findViewById(R.id.name);
		cell = (EditText) findViewById(R.id.cell);
		//locFrom = (Button) findViewById(R.id.locFrom);
//		locTo = (Button) findViewById(R.id.locTo);
		
		spin1 = (EditText) findViewById(R.id.spin1);
		spin2 = (EditText) findViewById(R.id.spin2);
		spinner1 = (Spinner) findViewById(R.id.spinner1);


    locFrom = (Button)findViewById(R.id.locFrom);
	locFrom.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			 addFrom = showCurrentLocation();	
			 if(!addFrom.equals("default"))
			      Toast.makeText(Expense.this, addFrom, Toast.LENGTH_LONG).show();
		  
		}
	});
	
	locTo = (Button)findViewById(R.id.locTo);
	locTo.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
					   
				
						
			           locationGet();
					   Toast.makeText(Expense.this, " Location fetched Successfully = "+String.valueOf(latTo)+", "+String.valueOf(lonTo), Toast.LENGTH_SHORT).show();
					  
					     
					     
				
			
	
			   
			  
						try{
							
							location();
							
					//	    Toast.makeText(Expense.this, "Client location fetched successfully"+client_lat+", "+client_lon, Toast.LENGTH_LONG).show();
						}catch (Exception e) {
							
							e.printStackTrace();
						}
						
				
			     
						
						
						Location locationA = new Location("point A");

						locationA.setLatitude(latTo);
						locationA.setLongitude(lonTo);

						Location locationB = new Location("point B");

						locationB.setLatitude(Double.valueOf(client_lat));
						locationB.setLongitude(Double.valueOf(client_lon));

						double distance = locationA.distanceTo(locationB);
						
						Toast.makeText(Expense.this, String.valueOf(distance), Toast.LENGTH_SHORT).show();
						
						if(distance<=100){
							
							addTo = showCurrentLocation();
							 if(!addTo.equals("default")){
								 
								 Toast.makeText(Expense.this, "Address Fetched Successfully = "+addTo, Toast.LENGTH_LONG).show();
								 hr = String.valueOf(new Date().getHours());
									 min = String.valueOf(new Date().getMinutes());
							 }
							     
			
						}
						else
							Toast.makeText(Expense.this, "You're not at Destination", Toast.LENGTH_LONG).show();
			 
			
		
		}
	});
	
	Button submit = (Button) findViewById(R.id.submit);
	submit.setOnClickListener(new View.OnClickListener() {
		
			
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
				
			
			if(addFrom.equals("default")||addTo.equals("default")){
				 Toast.makeText(Expense.this,"Please tag To and From locations", Toast.LENGTH_LONG).show();
				 
			} else {
			
			name1 = name.getText().toString();
			cell1 = cell.getText().toString();
	
			spinner1val = spin1.getText().toString();
			spinner2val = spin2.getText().toString();
			String chk_hr = String.valueOf(new Date().getHours());
			String chk_min = String.valueOf(new Date().getMinutes());
			
			
			total = Integer.valueOf(spinner1val) + Integer.valueOf(spinner2val);
		   
			
			/*//Toast.makeText(Expense.this, name1+":"+spinner1val+":"+spinner2val,
					///Toast.LENGTH_LONG).show();
					 * 
					 */
			ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("name",name1));
			pairs.add(new BasicNameValuePair("cell_no",cell1));
			pairs.add(new BasicNameValuePair("locf",addFrom));
			pairs.add(new BasicNameValuePair("loct",addTo));
			pairs.add(new BasicNameValuePair("trans",spinner1val));
			pairs.add(new BasicNameValuePair("acc",spinner2val));
			pairs.add(new BasicNameValuePair("hour",hr));
			pairs.add(new BasicNameValuePair("minute",min));
			pairs.add(new BasicNameValuePair("chk_min",chk_min));
			pairs.add(new BasicNameValuePair("chk_hr",chk_hr));
			
			
			
			
	     
			
			Utility uti = new Utility();
	       try {
	        	
	    	  uti.sendData1(pairs);
	    	   
				
			//	Toast.makeText(this, a1[1], Toast.LENGTH_LONG).show();
			} catch (Exception e) {
			//	 TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
	        Toast.makeText(Expense.this, "Details Saved Successfully!!", Toast.LENGTH_LONG).show();
	      
	        Intent i = new Intent(Expense.this, DreamsolActivity.class);
	        startActivity(i);

			}
			
			
		}
	});
	
	// Spinner
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, items);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spinner1.setAdapter(adapter);

	spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int x = spinner1.getSelectedItemPosition();
			valSel = items[x];
			//Toast.makeText(Expense.this, valSel, Toast.LENGTH_LONG).show();
			/*addTo = showCurrentLocation();	
			 if(addTo!=null)
			      Toast.makeText(Expense.this, addTo, Toast.LENGTH_LONG).show();
			*/
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    
	});
	
	

		
	
	} //end of onCreate
	
	
	
	
	void locationGet(){
		
		  Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		   latTo = location.getLatitude();
	     lonTo = location.getLongitude();
	     
		
	}
	
	
	
	String showCurrentLocation() {
			
		/* Class My Location Listener */

		
	        
	        //Toast.makeText(Expense.this,"FGDE" , Toast.LENGTH_LONG).show();
	        
		   
		   Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

			   lat = location.getLatitude();
		      lon = location.getLongitude();
		     Toast.makeText(Expense.this, String.valueOf(lat)+", "+String.valueOf(lon), Toast.LENGTH_LONG).show();
		       
		      
	        Geocoder geocoder = new Geocoder(Expense.this, Locale.ENGLISH);

	        try {
	       List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

	       if(addresses != null) {
	       Address returnedAddress = addresses.get(0);
	       StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
	       for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
	       strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");   
	        
	        add = strReturnedAddress.toString(); 
	       
	      
	      // Toast.makeText(Expense.this, "Location Tagged Successfully", Toast.LENGTH_SHORT).show();
	       
	       
	       }
	       return add;

	       //Toast.makeText(Expense.this, strReturnedAddress.toString(), Toast.LENGTH_LONG).show();
	       
	       
	       
	       }
	       else{
	    	Toast.makeText(Expense.this, "Address not available", Toast.LENGTH_LONG).show();
	    	return "default";
	    	
	      }
	     } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      Toast.makeText(Expense.this,"Exception Occured, try again", Toast.LENGTH_LONG).show();
	      return "default";
	      
	     }
		 	
	}
	
	
        void location() {
	
		
		
		String sendName;
		sendName = valSel;
		Toast.makeText(Expense.this, sendName , Toast.LENGTH_LONG).show();
		
		try {
			
			
	         HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams,
						TIMEOUT_MILLISEC);
				HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
				
				HttpParams p = new BasicHttpParams();
				
				p.setParameter("user", "1");

				// Instantiate an HttpClient
				HttpClient httpclient = new DefaultHttpClient(p);
				String url = "http://bhavit.xtreemhost.com/clientgetdata.php?user=1&format=json";
				HttpPost httppost = new HttpPost(url);

				// Instantiate a GET HTTP method
				try {
					Log.i(getClass().getSimpleName(), "send  task - start");
					
					
					ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(7);
					
					
				    nameValuePairs1.add(new BasicNameValuePair("name", sendName));
				    
					//List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					//		7);
				
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

	 					
	 					
	 					client_lat = jObject.getString("latitude");
	 					client_lon = jObject.getString("longitude");
	 					
	 					//cell.setText(getCell);
	 					
	 			//		client_lon = jObject.getString("longitude");
					    // amt.setText(String.valueOf(totalamt));
	 					
	 					
					     
						
					
			}
					
						
	               
					
			        
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			 
		  } catch (Throwable t) {
				Toast.makeText(Expense.this, "Request failed: " + t.toString(),
						Toast.LENGTH_LONG).show();

	}
	
	
		
		
		
		
		
		
		
		
		
		
		
	}
	
	
/* Class My Location Listener */

	 private class mylocationlistener implements LocationListener {
		   
		    public void onLocationChanged(Location location) {
		        if (location != null) {
		        /*Log.d("LOCATION CHANGED", location.getLatitude() + "");
		        Log.d("LOCATION CHANGED", location.getLongitude() + "");
		        Toast.makeText(Expense.this,
		            location.getLatitude() + "" + location.getLongitude(),
		            Toast.LENGTH_LONG).show();*/
		        }
		    }
		   
		    public void onProviderDisabled(String provider) {
		    	Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
		    }
		    
		    public void onProviderEnabled(String provider) {
		    	Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
		    }
		    
		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    }
		    }
} 
    

