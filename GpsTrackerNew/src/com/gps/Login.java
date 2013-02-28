package com.gps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class Login extends Activity {
	
	EditText email;
    String mail;
    EditText phone;
    String ph;
    Intent i;
    private ProgressDialog progDailog;
    private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		
		 email = (EditText) findViewById(R.id.editText1);
		phone = (EditText) findViewById(R.id.editText2);
		Button login = (Button) findViewById(R.id.btn_login);
		
		
		
		login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				 mail = email.getText().toString();
   				ph = phone.getText().toString();
   				
   				if(email.getText().toString().equals("")) {
   					Toast.makeText(Login.this,"Please Enter Email!!",Toast.LENGTH_LONG).show();
   					email.requestFocus();
   				}
   				else if(phone.getText().toString().equals("")) {
   					Toast.makeText(Login.this,"Please Enter Password!!",Toast.LENGTH_LONG).show();
   						phone.requestFocus();
   				}
   				else {
   				
   					progDailog = ProgressDialog.show(Login.this,
  		                  "VEHICLE TRACKER ", "Authenticating.. please wait....",
  		                  true);
  		                  new Thread() {
  		                  public void run() {
  		                                      try{
  		                                    	  
  		                                    	 
  		                          				
  		                       // just doing some long operation
  		                       sleep(5000);
  		                  } catch (Exception e) {  }
  		               handler.sendEmptyMessage(0);
  		             progDailog.dismiss();                                   }
  		         }.start();
  				
   				
   				}
				
				
				
				
			}
		});
		
		handler = new Handler() {
			 @Override
			 public void handleMessage(Message msg) {
			   
				 
				 i = new Intent(Login.this, MyMenu.class);
   				i.putExtra("email", mail);
   				i.putExtra("phone", ph);
				 startActivity(i);
			 
			 
			 
			 
			 
			 
			 }
			};
		
	}

}
