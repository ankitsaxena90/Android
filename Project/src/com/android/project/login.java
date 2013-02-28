package com.android.project;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends MapActivity implements OnClickListener {
	Button login_btn;
	EditText user_txt;
	EditText pass_txt;
	String token="";
	 
	SharedPreferences preferences;
	static final String url="http://sharedcabs.com/mobilepoc/authenticate";
	private static final int MODE_WORLD_WRITABLE = 0;
	Utility utility = new Utility();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		 
		login_btn=(Button)findViewById(R.id.login_btn);
		login_btn.setOnClickListener(this);
		user_txt=(EditText)findViewById(R.id.user_name_txt);
		pass_txt=(EditText)findViewById(R.id.user_pass_txt);

		if(isConnected())
			checkLoginStatus();
		else {
			Toast.makeText(this,"Please Check If Internet Is Accessible, then try again !!",Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
	public void onClick(View arg0) {
		AlertDialog.Builder dlg=new AlertDialog.Builder(this);
		
		try {
			if(arg0==findViewById(R.id.login_btn)) {
			if(user_txt.getText().toString().equals("")) {
				Toast.makeText(this,"Please Enter Login ID!!",Toast.LENGTH_LONG).show();
				user_txt.requestFocus();
			}
			else if(pass_txt.getText().toString().equals("")) {
				Toast.makeText(this,"Please Enter Password!!",Toast.LENGTH_LONG).show();
					pass_txt.requestFocus();
			}
			else {
				if(getauthenticate("http://sharedcabs.com/mobilepoc/authenticate")){
				Intent i=new Intent(this,SummeryView.class);
				i.putExtra("ticket1",token);
				startActivity(i);
				}	
				else
					Toast.makeText(this,"Authentication Failed!!",Toast.LENGTH_LONG).show();
					
			}		
		}
	}
		catch(Exception e) {
			dlg.setTitle("Login Window !!");
			dlg.setMessage(e.toString());
			dlg.show();
		}
		
		
	}
	public void checkLoginStatus() {
		SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
        String prefToken = myPrefs.getString("token_value", "nothing");
		if(prefToken.equals("nothing")){
			
		} 
		else{ 
			Intent i=new Intent(this,SummeryView.class);
			i.putExtra("ticket1",prefToken);
			/** FINISH LOGIN ACTIVITY **/
			finish();
			startActivity(i);
		}
	}
	public boolean getauthenticate(String url)
	{
		AlertDialog.Builder dlg=new AlertDialog.Builder(this);
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		 
		pairs.add(new BasicNameValuePair("username",user_txt.getText().toString()));
		pairs.add(new BasicNameValuePair("password",pass_txt.getText().toString()));
	
		try {
			
			utility.echoServer();
			token = utility.getResponse(url, pairs);
			dlg.setTitle("Login !!");
			dlg.setMessage(token.toString());
			if (token.equals("")) { 
				getauthenticate(url);
				return true;
			}
			else if(token.contains("html")){
				Toast.makeText(this, "Unknown Error Please Try Again !! \n"+token.toString(), Toast.LENGTH_LONG).show();
				return false;
			}
			else {
				Toast.makeText(this, "Login Successfull !!", Toast.LENGTH_LONG).show();
				saveApplicationState();
				return true;
			}
		}
		
		catch(UnsupportedEncodingException uex) {
			dlg.setMessage("The Encoding Is not Supported !! Contact Admin");
			dlg.show();
		} 
		catch(ClientProtocolException cpe){
			dlg.setMessage("There is an Error in the Client HTTP PROTOCOL..\n"+"Check Settings and Try Again!!");
			dlg.show();
	}catch(Exception e) {
			e.printStackTrace();
			dlg.setMessage(e.toString());
			dlg.show();
}
	return false;
	}
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	public void saveApplicationState() {
		SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_WRITABLE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        
        prefsEditor.putString("token_value",token);
        prefsEditor.putString("user_name",user_txt.getText().toString());
        prefsEditor.putString("user_pass",pass_txt.getText().toString());
        prefsEditor.commit();
	}

	/* (non-Javadoc)
	 * @see com.google.android.maps.MapActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isConnected())
			checkLoginStatus();
		else {
			Toast.makeText(this,"Please Check If Internet Is Accessible, then try again !!",Toast.LENGTH_LONG).show();
			finish();
		}	
	}	
	
	public boolean isConnected() {
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
  NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
  return activeNetworkInfo != null;
		
	}
	
}
