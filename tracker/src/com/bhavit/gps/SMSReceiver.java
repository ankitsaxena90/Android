package com.bhavit.gps;


import java.io.IOException;
import java.util.List;
import java.util.Locale;



import android.app.PendingIntent;
import android.content.*;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;



public class SMSReceiver extends BroadcastReceiver {
	

	 String smsSender ="";
	 String smsBody = "";
      long timestamp = 0L;
      Gpstracker abc;
      
      protected LocationManager locationManager;
      
    
	
      



 @Override
 public void onReceive(Context context, Intent intent) {
 
       
	// get the Bundle map from the Intent parameter to onReceive()
	 Bundle bundle = intent.getExtras();
	 
			 // get the SMS received
	 Object[] pdus = (Object[]) bundle.get("pdus");
	 SmsMessage[] msgs = new SmsMessage[pdus.length];
	 
	 
	
	 
	
	  
	 for (int i=0; i<msgs.length; i++){
	 msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
	 smsSender += msgs[i].getOriginatingAddress();
	 smsBody += msgs[i].getMessageBody().toString();
	 timestamp += msgs[i].getTimestampMillis();
	 }
 
	 Log.i("Message Received",  smsBody);
   
	
    if(smsBody.equals("givelocation")){
    locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);	
    
    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    double LATITUDE = location.getLatitude();
    double LONGITUDE = location.getLongitude();
   
    Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

    try {
   List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);

   if(addresses != null) {
   Address returnedAddress = addresses.get(0);
   StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
   for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
   strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");   
   }

   Toast.makeText(context, strReturnedAddress.toString(), Toast.LENGTH_LONG).show();
   sendSMS(smsSender, strReturnedAddress.toString());
   
   }
   else{
	Toast.makeText(context, "Adress not available", Toast.LENGTH_LONG).show();
  }
 } catch (IOException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
  Toast.makeText(context,"Can't excess adress", Toast.LENGTH_LONG).show();
 }
    	
    	
    	 
}
     
     
  // code to go at the end of our SMSBroadcastReceiver's onReceive() method
	
    // intent.setClass(context, Gpstracker.class);   
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 
	  //  intent.putExtra("sender", smsSender);
	   // intent.putExtra("body", smsBody);
	    //intent.putExtra("timestamp", timestamp);
	    
	   // context.startService(intent);
	    
 
    }
 

 

 private void sendSMS(String phoneNumber, String message)
 {        
     Log.v("phoneNumber",phoneNumber);
     Log.v("MEssage",message);
                 
     SmsManager sms = SmsManager.getDefault();
     sms.sendTextMessage(phoneNumber, null, message, null, null);        
 }    
		
		
		
	}	
	





