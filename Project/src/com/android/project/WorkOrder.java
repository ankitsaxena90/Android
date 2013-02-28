package com.android.project;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

public class WorkOrder extends MapActivity implements OnClickListener {
	/** Called when the activity is first created. */

	Button saveInfo;
	EditText date,jobTitle,name,address,mobile,phone,notes,time;
	String healthyToken;
	String newToken;
	ListView myView;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID=1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workorder);
		saveInfo=(Button)findViewById(R.id.saveBtn);
		date=(EditText)findViewById(R.id.dateTxt);
		date.setOnClickListener(this);
		date.setText(getDate());
		time=(EditText)findViewById(R.id.time_txt);
		time.setOnClickListener(this);
		Calendar cal=Calendar.getInstance();
		String data = new Utility().getTimeIn12HrFormat(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		time.setText(data);

		jobTitle=(EditText)findViewById(R.id.jobTitleTxt);
		name=(EditText)findViewById(R.id.nameTxt);
		address=(EditText)findViewById(R.id.addressText);
		mobile=(EditText)findViewById(R.id.mobileTxt);
		phone=(EditText)findViewById(R.id.phoneTxt);
		notes=(EditText)findViewById(R.id.notesTxt);
		saveInfo.setOnClickListener(this);
		myView=(ListView)findViewById(R.id.listView1);
		healthyToken=String.valueOf(getIntent().getSerializableExtra("ticket2"));
		newToken=healthyToken;
		try {
			new Utility().echoServer();
		}
		catch(Exception ex) {
			Toast.makeText(this,"Exception Occured !!",Toast.LENGTH_LONG).show();
		}
	}

	public String getDate() {
		Calendar cal=Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int month=cal.get(Calendar.MONTH);
		int year=cal.get(Calendar.YEAR);
		String date=String.valueOf(month+1)+"/"+String.valueOf(day)+"/"+String.valueOf(year);
		return date;
	}

	public boolean isStoredCorrectly(String url)
	{
		AlertDialog.Builder dlg=new AlertDialog.Builder(this);

		/**********VALIDATING USER INPUT***********/
		if(validated()) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("token",healthyToken));
			pairs.add(new BasicNameValuePair("userid","1"));
			pairs.add(new BasicNameValuePair("name",name.getText().toString()));
			pairs.add(new BasicNameValuePair("address",address.getText().toString()));
			pairs.add(new BasicNameValuePair("jobtitle",jobTitle.getText().toString()));
			pairs.add(new BasicNameValuePair("mobileno",mobile.getText().toString()));
			pairs.add(new BasicNameValuePair("phoneno",phone.getText().toString()));
			pairs.add(new BasicNameValuePair("description",notes.getText().toString()));
			pairs.add(new BasicNameValuePair("status","PENDING...."));
			pairs.add(new BasicNameValuePair("date",date.getText().toString()+" "+time.getText().toString()));

			try {
				Utility uti=new Utility();
				uti.echoServer();
				String data = uti.getResponse(url, pairs);
				if(data.equals("")){
					//Toast.makeText(this,"Unable To Save Order..",Toast.LENGTH_LONG).show();
					return false;
				}
				else if(data.contains("html")) {
					//Toast.makeText(this,"Server is Busy Please Try Again",Toast.LENGTH_LONG).show();
					return false;
				}
				else 
				{
					Toast.makeText(this,"Saving WorkOrder...",Toast.LENGTH_LONG).show();
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
		}//END IF
		else 
			Toast.makeText(this,"All Fields Are Mandatory !!",Toast.LENGTH_LONG).show();
		return false;
	}

	public void onClick(View arg0) {
		if(arg0==findViewById(R.id.saveBtn)) {
			if(isStoredCorrectly("http://sharedcabs.com/mobilepoc/workorder/")) {
				Toast.makeText(this,"WorkOrder Saved Successfully !!" ,Toast.LENGTH_LONG).show();
				finish();
			}
			else {
				Toast.makeText(this,"Unable To Process Reqest !!" ,Toast.LENGTH_LONG).show();
			}
		}
		/****IMPLEMENTING DATE PICKER VIEW ON CLICK OF DATE EDIT TEXT VIEW ******/
		else if(arg0==findViewById(R.id.dateTxt)) {
			showDialog(DATE_DIALOG_ID);
		}
		else if(arg0==findViewById(R.id.time_txt)){
			showDialog(TIME_DIALOG_ID);
		}	
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		if(id==0){
			Calendar c = Calendar.getInstance();
			int cyear = c.get(Calendar.YEAR);
			int cmonth = c.get(Calendar.MONTH);
			int cday = c.get(Calendar.DAY_OF_MONTH);
			DatePickerDialog datepicker =new DatePickerDialog(this,  mDateSetListener,  cyear, cmonth, cday);
			datepicker.setTitle("Select Date");
			return datepicker; 
		}//END IF
		else if(id==1){
			Calendar c=Calendar.getInstance();
			int hrs=c.getTime().getHours();
			int mins=c.getTime().getMinutes();
			int time_of_day=c.get(c.AM_PM);
			String am_pm;
			if(time_of_day==0)
				am_pm="AM";
			else
				am_pm="PM";
			/******INIT TIME DIALOG ****/
			TimePickerDialog timepicker=new TimePickerDialog(this,mTimeSetListener,hrs,mins,false);
			timepicker.setTitle("Select Time");
			return timepicker;
		}//END ELSE IF
		return null;
	}//END DIALOG FUNCTION

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		// onDateSet method

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			String date_selected = String.valueOf(monthOfYear+1)+" /"+String.valueOf(dayOfMonth)+" /"+String.valueOf(year);
			date.setText(date_selected);
		}

	};
	private TimePickerDialog.OnTimeSetListener mTimeSetListener=new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			String data = new Utility().getTimeIn12HrFormat(hourOfDay, minute);		    
			time.setText(data);
		}
	};

	public boolean validated() {
		if(jobTitle.getText().toString().equals("")) {
			Toast.makeText(this,"Please Specify Description for Job Title",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(name.getText().toString().equals("")) {
			Toast.makeText(this,"Please Specify Name",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(address.getText().toString().equals("")) {
			Toast.makeText(this,"Please Specify Address",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(mobile.getText().toString().equals("")) {
			Toast.makeText(this,"Please Specify Mobile No",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(phone.getText().toString().equals("")) {
			Toast.makeText(this,"Please Specify Phone No",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(time.getText().toString().equals("")) {
			Toast.makeText(this,"Please Specify Time",Toast.LENGTH_LONG).show();
			return false;
		}
		else 
			return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
