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

import com.google.android.maps.MapActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SummeryView extends MapActivity implements OnClickListener, OnItemClickListener {
	Button btn;
	String token_id;
	String nextToken;
	ListView lview;
	Button add_new_btn;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	SimpleAdapter adapter;

	    @Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        setContentView(R.layout.summeryview);
	        token_id=String.valueOf(getIntent().getSerializableExtra("ticket1"));
	        //Toast.makeText(this,token_id,Toast.LENGTH_LONG).show();
	        nextToken=token_id;
	        lview=(ListView)findViewById(R.id.listView1);
	        
	        DisplayMetrics metrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        
	        LayoutParams params = new LayoutParams(metrics.widthPixels,metrics.heightPixels-100);
	        //ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(metrics.widthPixels,metrics.heightPixels-30);
	        LinearLayout tt = (LinearLayout)findViewById(R.id.linearLayout2);
	        tt.setLayoutParams(params);
	        
	        lview.setOnItemClickListener(this);
	        adapter = new SimpleAdapter(
        		this,
        		list,
        		R.layout.custom_row_view,
        		new String[] {"jobtitle","address","date","id"},
        		new int[] {R.id.text1,R.id.text2, R.id.text3,R.id.text4}
        		);
        
        /** POPULATING THE LIST WITH DATA RETRIVED FROM WEB SERVICE VALUES **/
         try {
           populateListFrom("http://www.sharedcabs.com/mobilepoc/workorder/GetAllWorkOrders");
			add_new_btn=(Button)findViewById(R.id.button1);
			add_new_btn.setOnClickListener(this);
			add_new_btn.setText(R.string.new_work_order_btn);
			//lview.addFooterView(add_new_btn,null,true);
			lview.setAdapter(adapter);		
	     	}catch (Exception e) {
			Toast.makeText(this,e.toString(),Toast.LENGTH_LONG);
	     	}
	    
	 	}
    
	public void populateListFrom(String url) {
    	
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpPost httppost = new HttpPost(url);
    	String parseStr="";
        try {
            // Adding Token before posting request
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("token",nextToken));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            /*******IF THE REQUEST HAS TIMED OUT **********/
           int status_code=response.getStatusLine().getStatusCode();
            if(status_code!=200)
                     	/********REQUEST ACTUALLY HAS TIMED OUT********/
            		populateListFrom(url);
            else {
            //Toast.makeText(this,String.valueOf(status_code),Toast.LENGTH_LONG).show();
            HttpEntity entity=response.getEntity();
            if(entity!=null) {
            	 parseStr=EntityUtils.toString(entity);
            	JSONObject jobj=new JSONObject(parseStr);
            	JSONArray list_array=jobj.getJSONArray("Item");
            	           	
            	for(int i=0;i<list_array.length();i++) {
            		JSONObject tmp_json=list_array.getJSONObject(i);
            		HashMap<String,String> temp = new HashMap<String,String>();
            		
      	    	  temp.put("jobtitle",tmp_json.getString("JobTitle"));
      	    	  temp.put("address",tmp_json.getString("Address"));
      	    	  String date_tmp=tmp_json.getString("ScheduleDate");
      	    	  temp.put("date",date_tmp);
      	    	  temp.put("id",tmp_json.getString("ID"));
      	    	  list.add(temp);
            	}  	
            }
            }//END ELSE
        	} catch (ClientProtocolException e) {
        		Toast.makeText(this,"Client Protocol Exception !!", Toast.LENGTH_LONG).show();
        	} catch (IOException e) {
        		Toast.makeText(this,"I/O Exception !! \n"+parseStr.toString(), Toast.LENGTH_LONG).show();
        	} catch(JSONException e) {
        		Toast.makeText(this,"Token Mismatch!! Please Clear Application Data and Login Again", Toast.LENGTH_LONG).show();
        		finish();
         }   	
    }
	
   
	public void onClick(View v) {
		//	if(v==findViewById(1710)) {
			Intent i=new Intent(this,WorkOrder.class);
			i.putExtra("ticket2",nextToken);
			startActivity(i);
			//}	
    	}

	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ListAdapter la=(ListAdapter)arg0.getAdapter();
		Object ob=la.getItem(arg2);
		String tmp=ob.toString();
		String id_fetcher=ob.toString().substring(1,ob.toString().indexOf(","));
		int id=Integer.parseInt(id_fetcher.substring(id_fetcher.indexOf("=")+1,id_fetcher.length()));
		Intent i=new Intent(this,DetailView.class);
		i.putExtra("ID",id);
		i.putExtra("pass",nextToken);
		startActivity(i);
		}

	@Override
	protected void onRestart() {
		super.onRestart();
		list.clear();
		populateListFrom("http://www.sharedcabs.com/mobilepoc/workorder/GetAllWorkOrders");
		lview.setAdapter(adapter);	
		}
	 
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
		 
		 
		 
		 
		 
		
    


	
	
	