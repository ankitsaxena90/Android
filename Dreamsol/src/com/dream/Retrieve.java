package com.dream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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



import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Retrieve extends Activity {
	
	
	int TIMEOUT_MILLISEC = 10000;	
	
	String getName = null;
	String getCell = null;
	String getLocf = null;
	String getLoct = null;
	String getAcc = null;
	String getTrans;
	String total;
	
	int accInt;
	int transInt;
	int totalamt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.get);
		
		final EditText name = (EditText)findViewById(R.id.getname);
		final EditText cell = (EditText)findViewById(R.id.editText1);
		final EditText from = (EditText)findViewById(R.id.editText2);
		final EditText to = (EditText)findViewById(R.id.editText3);
		final EditText acc = (EditText)findViewById(R.id.editText4);
		final EditText trans = (EditText)findViewById(R.id.editText5);
		final EditText amt = (EditText)findViewById(R.id.editText6);
		
		
		Button g = (Button)findViewById(R.id.get);
		
		g.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String sendName;
				sendName = name.getText().toString();
				Toast.makeText(Retrieve.this, sendName , Toast.LENGTH_LONG).show();
				
				try {
					
					
			         HttpParams httpParams = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParams,
								TIMEOUT_MILLISEC);
						HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
						
						HttpParams p = new BasicHttpParams();
						
						p.setParameter("user", "1");

						// Instantiate an HttpClient
						HttpClient httpclient = new DefaultHttpClient(p);
						String url = "http://bhavit.xtreemhost.com/Dreamsolgetdata.php?user=1&format=json";
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

			 					
			 					
			 					getCell = jObject.getString("cell_no");
			 					cell.setText(getCell);
			 					
			 					getLocf = jObject.getString("location_From");
			 					
			 					from.setText(getLocf);

			 					getLoct = jObject.getString("location_To");
			 					
			 					to.setText(getLoct);
			 					
			 					getAcc = jObject.getString("accomodation");
								acc.setText(getAcc);
								
								
							    
							     getTrans = jObject.getString("transportation");
							     trans.setText(getTrans);
							     
							     total = jObject.getString("amount");
							     //amt.setText(total);
							     
							     accInt = Integer.valueOf(getAcc);
							     transInt = Integer.valueOf(getTrans);
							     totalamt = accInt+transInt;
							     amt.setText(String.valueOf(totalamt));
							     
							    	
							
								
							
					}
							
							
			               
							
					        
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

					 
				  } catch (Throwable t) {
						Toast.makeText(Retrieve.this, "Request failed: " + t.toString(),
								Toast.LENGTH_LONG).show();

			}
			
			
			
			
			}
				
			
		});
		
		
		
		
	}}
	
	
	
	/*public void execute() {   
		   
	 	   
}
*/