package com.android.project;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class Utility {
	
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

	public String echoServer() throws Exception{
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("msg","Hi"));
		String data = "";
		data = getResponse("http://sharedcabs.com/mobilepoc/authenticate", pairs);
		
		return data;
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
	

}
