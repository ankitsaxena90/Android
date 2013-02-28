package com.gps;






import android.content.*;

import android.os.Bundle;
import android.telephony.*;
import android.util.Log;
import android.widget.Toast;



public class SMSReceiver extends BroadcastReceiver {
	

	 String smsSender ="";
	 String smsBody = "";
      long timestamp = 0L;
      MyMenu m;
      
   
      
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
     
	 
	
 	   Toast.makeText(context, smsBody,
 			     Toast.LENGTH_LONG).show();
 		
 		
 	}
    	 


 
    }
 

 
  
		
		
		

	





