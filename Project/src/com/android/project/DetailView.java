package com.android.project;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailView extends MapActivity implements OnClickListener {
	TextView jobTitle,date,notes,address;
	Button get_direction,send_update,start_wrk;
	String fetcher,index; 
	String phone_no;
	private static final int MAX_SMS_MESSAGE_LENGTH = 160;
    private static final int SMS_PORT = 8091;
    private static final String SMS_DELIVERED = "SMS_DELIVERED";
    private static final String SMS_SENT = "SMS_SENT";
    private static final String MESSAGE_BODY_1="Your scheduled maintenance from";
    private static final String MESSAGE_BODY_2=
    "Company is on its way, and will arrive at your home within the next 30 minutes";
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_view);
		jobTitle=(TextView)findViewById(R.id.detail_view_job_title_lbl);
		date=(TextView)findViewById(R.id.detail_view_date_lbl);
		notes=(TextView)findViewById(R.id.detail_view_notes_txt);
		address=(TextView)findViewById(R.id.detail_view_addresslbl);
		get_direction=(Button)findViewById(R.id.get_direction_btn);
		get_direction.setOnClickListener(this);
		send_update=(Button)findViewById(R.id.send_update_btn);
		send_update.setOnClickListener(this);
		start_wrk=(Button)findViewById(R.id.complete_work_btn);
		start_wrk.setOnClickListener(this);		
		
		/**********VALUES FROM INTENTS ********************/
		fetcher=String.valueOf(getIntent().getSerializableExtra("pass"));
		index=String.valueOf(getIntent().getSerializableExtra("ID"));
		/**************************************************/
		
		String url="http://www.sharedcabs.com/mobilepoc/workorder/getworkorderbyid";
		getData(url,index,fetcher);
		/*** REGISTERING INTENTS FOR BRODCASTING EVENTS ***/
		registerReceiver(sendreceiver, new IntentFilter(SMS_SENT));
	    registerReceiver(deliveredreceiver, new IntentFilter(SMS_DELIVERED));
	    registerReceiver(smsreceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		
	}
	public void getData(String url,String index,String token) {
		HttpClient httpclient = new DefaultHttpClient();
    	HttpPost httppost = new HttpPost(url);
        try {
            // Adding Token before posting request
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("token",token));
            nameValuePairs.add(new BasicNameValuePair("ID",index));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            int status_code=response.getStatusLine().getStatusCode();
            if(status_code==408)
            	getData(url,index,token);
            else {
            HttpEntity entity=response.getEntity();
            String data=EntityUtils.toString(entity);
            if(data.equals("")) 
            	getData(url,index,token);
            	else {
            	//String parseStr=EntityUtils.toString(entity);
            	String parse=data.substring(data.indexOf("[")+1, data.indexOf("]"));
            	JSONObject jobj=new JSONObject(parse);
                jobTitle.setText(jobj.getString("JobTitle"));
      	    	 date.setText(jobj.getString("ScheduleDate"));
      	    	 notes.setText(jobj.getString("Description"));
      	    	 phone_no=jobj.getString("MobileNo").trim();
      	    	 address.setText("Address : "+jobj.getString("Address"));
            	}
            }
          
         } catch (ClientProtocolException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
        	Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        } catch(JSONException e) {
        	Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        	
        }		
	}
	
@Override
    protected void onDestroy()
    {
	/*** UNREGISTER ALL BROADCAST RECEIVERS UPON DESTROY ****/
    unregisterReceiver(sendreceiver);
    unregisterReceiver(deliveredreceiver);
    unregisterReceiver(smsreceiver);
    super.onDestroy();
    }

   private void sendSms(String phonenumber,String message, boolean isBinary)
   {
    SmsManager manager = SmsManager.getDefault();
    PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
    PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);
   
    int length = message.length();
    if(length > MAX_SMS_MESSAGE_LENGTH)
            {
             ArrayList<String> messagelist = manager.divideMessage(message);               
             manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
            }
    else
            {
             if(isValid(phonenumber))
            	 manager.sendTextMessage(phonenumber, null, message, piSend, piDelivered);
             else 
            	 Toast.makeText(this,"Unable To Send Message.. Destination is either empty or invalid !!",Toast.LENGTH_LONG).show();   
            }
    	}
    
        
	private boolean isValid(String phonenumber) {
		
            if(phonenumber.length()!=10) {
           	 Toast.makeText(this,"Unable To Send Message.. Destination is either empty or invalid !!",Toast.LENGTH_LONG).show();
           	 return false;
            }
            else {
            /**CHECKING FOR A VALID MOBILE NO **/
            char [] mob=phonenumber.toCharArray();
            for(int i=0;i<mob.length;i++) {
           	 if(Character.isDigit(mob[i]))
           			 continue;
           	 else
           		 return false;
           }
       }
       return true;
	}
		

    private BroadcastReceiver sendreceiver = new BroadcastReceiver()
    {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                    String info="";
                    
                    switch(getResultCode())
                    {
                            case Activity.RESULT_OK: info += "Message Sent Successfully !!"; break;
                            case SmsManager.RESULT_ERROR_GENERIC_FAILURE: info += "Send Failed: Generic Failure"; break;
                            case SmsManager.RESULT_ERROR_NO_SERVICE: info += "Send Failed: No Service"; break;
                            case SmsManager.RESULT_ERROR_NULL_PDU: info += "Send Failed: null pdu"; break;
                            case SmsManager.RESULT_ERROR_RADIO_OFF: info += "Send Failed: Radio Is Off"; break;
                    }
                    
                    Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();

            }
    };
    
    private BroadcastReceiver deliveredreceiver = new BroadcastReceiver()
    {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                    String info = "Delivery information: ";
                    
                    switch(getResultCode())
                    {
                            case Activity.RESULT_OK: info += "delivered"; break;
                            case Activity.RESULT_CANCELED: info += "not delivered"; break;
                    }
                    
                    Toast.makeText(getBaseContext(), info, Toast.LENGTH_SHORT).show();
            }
    };
    
    private BroadcastReceiver smsreceiver = new BroadcastReceiver()
    {
            @Override
            public void onReceive(Context context, Intent intent)
            {
            Bundle bundle = intent.getExtras();        
            SmsMessage[] msgs = null;
            
            if(null != bundle)
            {
                    String info = "Text SMS from ";
                Object[] pdus = (Object[]) bundle.get("pdus");
                msgs = new SmsMessage[pdus.length];
                
                for (int i=0; i<msgs.length; i++){
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
                    info += msgs[i].getOriginatingAddress();                     
                    info += "\n*****TEXT MESSAGE*****\n";
                    info += msgs[i].getMessageBody().toString();
                }

                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
            }                         
            }
    };
	public void onClick(View arg0) {
		switch(arg0.getId())
          {
           case R.id.send_update_btn:
               {
               String phonenumber =phone_no; 
               String message = MESSAGE_BODY_1+" "+jobTitle.getText().toString()+" "+MESSAGE_BODY_2;
               sendSms(phonenumber,message,false);
               break;
               }
           case R.id.get_direction_btn:
           {
        	   final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        	   if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER )) { 
        	   /************GETTING THE ADDRESS DETAILS ***********/
        	   String tmp=address.getText().toString();
        	   String addr=tmp.substring(tmp.indexOf(":")+2,tmp.length());
        	   String correct_addr=addr.replace(' ','+');
        	   String new_addr=correct_addr.replace('\n',',');
        	   String url="http://maps.googleapis.com/maps/api/geocode/json?address="+new_addr+"&sensor=false";
        	   if(getLatLonFromURI(url)){
        	   /***************IF VALID, THEN UPDATE WORK STATUS *****************/
        	   if(workStatusUpdated("http://www.sharedcabs.com/mobilepoc/workorder/updateworkorderstatus/","EnRoute")){
        		   Toast.makeText(this,"Work Order Status set to : En-Route",Toast.LENGTH_LONG).show();
        		   start_wrk.setVisibility(Button.VISIBLE);
           	   }
        	   else
        		   Toast.makeText(this,"Unable To Update WorkOrder Status...",Toast.LENGTH_LONG).show();
        	   }
        	   /**IF INVALID ADDRESS **/
        	   else
        		   Toast.makeText(this,"Unable To Fetch Direction, Check Address !!",Toast.LENGTH_LONG).show();
        		   }
        	   else {
        		   Toast.makeText(this,"GPS IS DISABLED PLEASE ENABLE YOUR GPS AND THEN TRY AGAIN !!",Toast.LENGTH_LONG).show();
        	   }
        	   break;
           }
           case R.id.complete_work_btn:
           {
        	   /*******IF START WORK BUTTON WAS CLICKED *****/
        	   if(start_wrk.getText().equals("Start Work")) {
             	   if(workStatusUpdated("http://www.sharedcabs.com/mobilepoc/workorder/updateworkorderstatus/","InProgress...")){
             		   Toast.makeText(this,"Work Order Status set to : In-Progress",Toast.LENGTH_LONG).show();
             		  start_wrk.setText("Complete Work");
             	   }
             	   else
             		   Toast.makeText(this,"Unable To Update WorkOrder Status...",Toast.LENGTH_LONG).show();
        	   }
        	   /*******IF COMPLETE WORK BUTTON WAS CLICKED *****/
        	   else 
        	   		{
        		   if(workStatusUpdated("http://www.sharedcabs.com/mobilepoc/workorder/updateworkorderstatus/","completed")){
            		   Toast.makeText(this,"Work Order Status set to : Completed",Toast.LENGTH_LONG).show();
            		   start_wrk.setVisibility(Button.VISIBLE);
            		   Intent i =new Intent(this,SummeryView.class);
            		   i.putExtra("ticket1",fetcher);
            		   startActivity(i);
            	   }
            	   else
            		   Toast.makeText(this,"Unable To Update WorkOrder Status...",Toast.LENGTH_LONG).show();
            	   }
               	   break;
           }
         }//END SWITCH CASE	
	}//END FUNCTION	
	
	public boolean getLatLonFromURI(String url) {
		HttpClient httpclient = new DefaultHttpClient();
    	HttpGet httppost = new HttpGet(url);

        try {
        	/*********GETTING DESTINATION GEOPOINTS ************/
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity=response.getEntity();
            if(entity!=null) {
            	String parseStr=EntityUtils.toString(entity);
            	JSONObject jobj=new JSONObject(parseStr);
            	String status=jobj.getString("status");
            	if(status.equals("ZERO_RESULTS")) {
            		   Toast.makeText(this,"Invalid Destination !!\n"+"Please Check Address Provided",Toast.LENGTH_LONG).show();
            		   return false;
               	}
            	else {
            	 JSONArray jobtemp=jobj.getJSONArray("results");          
            	 //Toast.makeText(this,jobtemp.toString(),Toast.LENGTH_LONG).show();
            	 JSONObject newob=jobtemp.getJSONObject(0);
            	 //Toast.makeText(this,newob.toString(),Toast.LENGTH_LONG).show();
            	 JSONObject anotherob=newob.getJSONObject("geometry");
            	 //Toast.makeText(this,anotherob.toString(),Toast.LENGTH_LONG).show();
            	 JSONObject anotherob1=anotherob.getJSONObject("location");
            	 //Toast.makeText(this,anotherob1.toString(),Toast.LENGTH_LONG).show();
      	    	 double lat=anotherob1.getDouble("lat");
      	    	 double lon=anotherob1.getDouble("lng");
      	    	 Intent i=new Intent(this,MyMapActivity.class);
      	    	 i.putExtra("dlon",lon);
      	    	 i.putExtra("dlat",lat);
      	    	 startActivity(i);
            	}
            }
        } catch (ClientProtocolException e) {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        } catch (IOException e) {
        	Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        } catch(JSONException e) {
        	Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        	
        }	
        return true;
}
	@Override
	protected boolean isRouteDisplayed() {
			return false;
	}
	
	public boolean workStatusUpdated(String url,String status) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("token",fetcher));
        nameValuePairs.add(new BasicNameValuePair("wid",index));
        nameValuePairs.add(new BasicNameValuePair("status",status));
        Utility utility = new Utility();
		String data;
		try {
			data = utility.getResponse(url, nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Toast.makeText(this,"Exception Occured While Updating...", Toast.LENGTH_LONG).show();
			return false;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Toast.makeText(this,"Exception Occured While Updating...", Toast.LENGTH_LONG).show();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this,"Exception Occured While Updating...", Toast.LENGTH_LONG).show();
			return false;
		} 
				if(data.contains("html") || data.equals("")){
					Toast.makeText(this,"Server Is Busy...", Toast.LENGTH_LONG).show();
					return false;
				}else{
					Toast.makeText(this,"Process Completed Successfully !!", Toast.LENGTH_LONG).show();
					return true;
				}
	}
	
}//END CLASS

	
	
	
