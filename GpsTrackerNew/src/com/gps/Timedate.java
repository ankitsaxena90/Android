package com.gps;

import java.util.Calendar;



import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class Timedate extends Activity {
	
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	 private String mYear;
	    private String mMonth;
	    private String mDay;
	    
	    
	    EditText date;
	    EditText month;
	    EditText year;
	    Button getHistory;
	    int hr;
	    int min;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timedate);
		
		date = (EditText) findViewById(R.id.editText1);
		 month = (EditText) findViewById(R.id.editText3);
		 year = (EditText) findViewById(R.id.editText2);
		 getHistory = (Button) findViewById (R.id.go);
		 
		 
		 
		  
	/*	date.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				

				final Calendar c = Calendar.getInstance();
			    mYear = c.get(Calendar.YEAR);
			    mMonth = c.get(Calendar.MONTH);
			    mDay = c.get(Calendar.DAY_OF_MONTH);
				
			
                
				showDialog(DATE_DIALOG_ID);
			}
		});
		
	
	time.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			showDialog(TIME_DIALOG_ID);
			
		}
	});
	*/
	getHistory.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			  mYear = year.getText().toString();
			    mMonth = month.getText().toString();
			    mDay = date.getText().toString();
			
		
			Intent i = new Intent(Timedate.this, History.class);
			
			Toast.makeText(Timedate.this, mMonth+":"+mYear, Toast.LENGTH_LONG).show();
			
			i.putExtra("day", mDay);
			i.putExtra("month", mMonth);
			i.putExtra("year", mYear);
			
			
			
			startActivity(i);
			
		}
	});
	
	}

	
	
	
	
	/*
	 @Override
		protected Dialog onCreateDialog(int id) {
		    switch (id) {
		    case DATE_DIALOG_ID:
		        
				return new DatePickerDialog(this,
		                    mDateSetListener,
		                    mYear, mMonth, mDay);
		    
		    
		    case TIME_DIALOG_ID:
		    	Calendar c=Calendar.getInstance();
				int hrs=c.getTime().getHours();
				int mins=c.getTime().getMinutes();
				int time_of_day=c.get(Calendar.AM_PM);
				String am_pm;
				if(time_of_day==0)
					am_pm="AM"; 
				else
					am_pm="PM";
				/******INIT TIME DIALOG ****/
		/*		TimePickerDialog timepicker=new TimePickerDialog(this,mTimeSetListener,hrs,mins,false);
				timepicker.setTitle("Select Time");
				return timepicker;
		    
		    }
		    return null;
		}
		
		
	    private DatePickerDialog.OnDateSetListener mDateSetListener =
	            new DatePickerDialog.OnDateSetListener() {

	                public void onDateSet(DatePicker view, int year, 
	                                      int monthOfYear, int dayOfMonth) {
	                    mYear = year;
	                    mMonth = monthOfYear;
	                    mDay = dayOfMonth;
	                    
	                  date.setText(String.valueOf(mDay)+"/"+String.valueOf(mMonth)+"/"+String.valueOf(mYear));
	                  Toast.makeText(Timedate.this, String.valueOf(mMonth)+":"+String.valueOf(mYear), Toast.LENGTH_LONG).show();
	                }
	            };
		
	
	            private TimePickerDialog.OnTimeSetListener mTimeSetListener=new TimePickerDialog.OnTimeSetListener() {

	        		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	        			
	        			hr = hourOfDay;
	        			min = minute;
	        			String data = new Utility().getTimeIn12HrFormat(hourOfDay, minute);		    
	        			time.setText(data);
	        			
	        			Toast.makeText(Timedate.this, String.valueOf(hr)+":"+String.valueOf(min), Toast.LENGTH_LONG).show();
	        			
	        			
	        		}
	        	};
	
	
	*/
	
	
}
