package com.gps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class HelloItemizedOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	Context mContext; 
	
	public HelloItemizedOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		
	}
		
	public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		}
	
	
	public void addOverlay(OverlayItem overlay) {
		    mOverlays.add(overlay);
		    populate();
		}
	
		
		
	@Override
	protected boolean onTap(int index){
		
		final OverlayItem oi = mOverlays.get(index);
	      AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	      dialog.setTitle("your title");
	      dialog.setMessage("youmessage");
	      dialog.setNegativeButton("Cancel", null);
	      dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

	        public void onClick(DialogInterface dialog, int id) {

	            }
	      });
	      dialog.show();
	      return true;

	      }


	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

}
