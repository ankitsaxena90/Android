package com.android.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class UpdateListAdapter extends Activity {
	String data;
	ListView lview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		data=String.valueOf(getIntent().getSerializableExtra("fetchID"));
		SimpleAdapter adapter = new SimpleAdapter(
        		this,
        		list,
        		R.layout.custom_row_view,
        		new String[] {"jobtitle","address","date"},
        		new int[] {R.id.text1,R.id.text2, R.id.text3}
        		);
		lview=(ListView)findViewById(R.id.listView1);
		ProgressDialog pDlg=new ProgressDialog(this,ProgressDialog.STYLE_SPINNER);
		String msg="Updating Application Data and Preferences...";
		pDlg.show(this,msg,"");
		updateList("www.sharedcabs.com/mobilepoc/workorder/GetAllWorkOrders");
		lview.setAdapter(adapter);
		pDlg.dismiss();
	}
	
	static final ArrayList<HashMap<String,String>> list = 
    	new ArrayList<HashMap<String,String>>();
	
	public void updateList(String url) {	    	
	    	HttpClient httpclient = new DefaultHttpClient();
	    	HttpPost httppost = new HttpPost(url);

	        try {
	            // Adding Token before posting request
	            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	            nameValuePairs.add(new BasicNameValuePair("token",data));
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	            // Execute HTTP Post Request
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity=response.getEntity();
	            if(entity!=null) {
	            	String parseStr=EntityUtils.toString(entity);
	            	JSONObject jobj=new JSONObject(parseStr);
	            	JSONArray list_array=jobj.getJSONArray("Item");
	              	for(int i=0;i<list_array.length();i++) {
	            		JSONObject tmp_json=list_array.getJSONObject(i);
	            		HashMap<String,String> temp = new HashMap<String,String>();  		
	      	    	  	temp.put("jobtitle",tmp_json.getString("JobTitle"));
	      	    	  	temp.put("address",tmp_json.getString("Address"));
	      	    	  	String date_tmp=tmp_json.getString("Created_at");
	      	    	  	temp.put("date",date_tmp);
	      	    	  	list.add(temp);
	              		}
	             }
	       } catch (ClientProtocolException e) {
	       } catch (IOException e) {
	       } catch(JSONException e) {
	 }
   }
}
