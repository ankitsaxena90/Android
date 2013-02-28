package com.gps;



import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		final Intent intent;
		intent = new Intent(Splash.this, Login.class);
		NotificationManager mNotificationManager;
		Notification notifyDetails ;
		
		//CREATES NOTIFICATION
				mNotificationManager = 
			   (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		        
				//Notification Details like its ICON ,its TEXT DISPLAYED when it starts , and CURRENT TIME
				notifyDetails = 
			    new Notification(R.drawable.icon,"GPS Tracker has Started!",System.currentTimeMillis());
				
				Context context = getApplicationContext();
			    
				
				CharSequence contentTitle = "GPS Tracker";  //Constant Name displayed on Notification
				      
				CharSequence contentText = "Track Your Registered Device..";  //Additional Details about Notification
				
				Intent notificationIntent = new Intent(context,
					    Splash.class);
		
		Thread LogoTimer = new Thread() {
			public void run() {
				
				try{
					int logoTimer = 0;
					while(logoTimer<3000){
						sleep(100);
						logoTimer= logoTimer + 100;
					 }
					startActivity(intent);
				  } 
				  catch (Exception e) {
					
					e.printStackTrace();
				}
				
				
				finally{
					finish();
				}
			}
		};
		LogoTimer.start();
	}
	

}
