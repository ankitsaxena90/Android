package com.dream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DreamsolActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button expense = (Button) findViewById(R.id.expenseClaim);
        expense.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent( DreamsolActivity.this, Expense.class);
				startActivity(i);
				
			}
		});
    
        Button track = (Button) findViewById(R.id.startTracking);
        track.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent j = new Intent( DreamsolActivity.this, Gpstracker.class);
				startActivity(j);
				
				
			}
		});
        
        Button ret = (Button)findViewById(R.id.retrieve);
        ret.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent j = new Intent(DreamsolActivity.this, Retrieve.class);
				startActivity(j);
			}
		});
    }
}