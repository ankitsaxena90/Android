package com.gps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import org.json.JSONObject;

import android.content.Context;
import android.net.ParseException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;




public class Utility {

	       InputStream is = null;
	
	        StringBuilder sb=null;
	
	        String result=null;
	        
	        String hour;
	    	String minute;
	    	String date;
	    	String month;
	    	Double latitude;
	    	Double longitude;
	    	
	    	
	    	
	    	int TIMEOUT_MILLISEC = 10000;

	
	public String getResponse(String url, List<NameValuePair> pairs) throws UnsupportedEncodingException,ClientProtocolException,Exception {
		
		String data = "";
		UrlEncodedFormEntity urlEncodedFormEntity = null;
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params,0);
		HttpConnectionParams.setSoTimeout(params,0);
		HttpPost post = new HttpPost(url);
		HttpResponse response;
		urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
		post.setEntity(urlEncodedFormEntity);
		response = client.execute(post);
		/**IF REQUEST HAS TIMED OUT *****/
		int status_code=response.getStatusLine().getStatusCode();
		if(status_code==408)
			getResponse(url,pairs);
		else 
			data = EntityUtils.toString(response.getEntity());
			
		return data;
	}

	 public void sendData(ArrayList<NameValuePair> data)
	    {
	         // 1) Connect via HTTP. 2) Encode data. 3) Send data.
	        try
	        {
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost = new      
	            HttpPost("http://bhavit.xtreemhost.com/webservice4.php?user=1&format=json");
	            httppost.setEntity(new UrlEncodedFormEntity(data));
	            HttpResponse response = httpclient.execute(httppost);
	            Log.i("postData", response.getStatusLine().toString());
	                //Could do something better with response.
	        }
	        catch(Exception e)
	        {
	            Log.e("log_tag", "Error:  "+e.toString());
	        }  
	    }
	 

	
	
	public String  getTimeIn12HrFormat(int hourOfDay, int minute) {
		
		String strMin = "";
		String data = "";
		
		if(minute<10) {
			strMin = "0"+minute;
		}else {
			strMin = String.valueOf(minute);
		}

		 if(hourOfDay>12)
		    {
		        data = (hourOfDay-12) + ":"+strMin+" PM";
		    }
		    if(hourOfDay==12)
		    {
		    	data = "12"+ ":"+strMin+" PM";
		    }
		    if(hourOfDay<12)
		    {
		        data = hourOfDay+ ":"+strMin+" AM";
		    }	
		    if(hourOfDay==0)
		    {
		        data = "12:"+strMin+" AM";
		    }
		    
		    return data;

	}
	
	
  public Double getData(String data) throws Exception  {
	
	  
	  //http post

	       try{

	           HttpClient httpclient = new DefaultHttpClient();

	           HttpPost httppost = new HttpPost("http://bhavit.xtreemhost.com/fetch.php");

	           List<? extends NameValuePair> nameValuePairs = null;
	           
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	          HttpResponse response = httpclient.execute(httppost);

	         HttpEntity entity = response.getEntity();

	           is = entity.getContent();

	       }catch(Exception ex){

	           Log.e("log_tag", "Error in http connection"+ex.toString());

	       }

	    

	       //convert response to string

	       try{

	           BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);

	           sb = new StringBuilder();

	           sb.append(reader.readLine() + "\n");

	           String line="0";

	         

	           while ((line = reader.readLine()) != null) {

	               sb.append(line + "\n");

	           }

	            

	          is.close();
	           result=sb.toString();

	            

	     }catch(Exception e){

	         Log.e("log_tag", "Error converting result "+e.toString());

	       }



	       //paring data

	      

	     Double send = null;

	       try{

	       JSONArray jArray = new JSONArray(result);

	       JSONObject json_data=null;

	        

	       for(int i=0;i<jArray.length();i++){

	               json_data = jArray.getJSONObject(i);

	               send=json_data.getDouble(data);

	               

	       }

	        

	       }catch (ParseException e1){

	           e1.printStackTrace();

	       }
		return send;

	}



 
}












    
   
 

 

 




