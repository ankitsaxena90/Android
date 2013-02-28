package com.gps;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class myTracking extends MapActivity {
    /** Called when the activity is first created. */
   
    LocationManager locman;
    LocationListener loclis;
    Location location;
    private MapView map;
    LocationManager mgr = null;
	
    List<GeoPoint> geoPointsArray = new ArrayList<GeoPoint>();
    private MapController controller;
    String provider =  LocationManager.GPS_PROVIDER;
    double lat;
    double lon;


    @Override
       public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.main);
          initMapView();
          initMyLocation();
          locman = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
          //locman.requestLocationUpdates(provider,60000, 100,loclis);
          //location = locman.getLastKnownLocation(provider);
          
                 
       }
    /** Find and initialize the map view. */
       private void initMapView() {
          map = (MapView) findViewById(R.id.mapView);
          controller = map.getController();
          map.setSatellite(false);
          map.setBuiltInZoomControls(true);
       }

       /** Find Current Position on Map. */
       private void initMyLocation() {
          final MyLocationOverlay overlay = new MyLocationOverlay(this, map);
          overlay.enableMyLocation();
          overlay.enableCompass(); // does not work in emulator
          overlay.runOnFirstFix(new Runnable() {
             public void run() {
                // Zoom in to current location
                controller.setZoom(8);
                controller.animateTo(overlay.getMyLocation());
             }
          });
          map.getOverlays().add(overlay);
       }

    public void onLocationChanged(Location location) {
        if (location != null){
            lat = location.getLatitude();
            lon = location.getLongitude();
            GeoPoint New_geopoint = new GeoPoint((int)(lat*1e6),(int)(lon*1e6));
            controller.animateTo(New_geopoint);
            MyOverlay MyOvlay = new MyOverlay(New_geopoint);
            map.getOverlays().add(MyOvlay);

        }

    }
    class MyOverlay extends Overlay{
    	private GeoPoint mGpt1;
    	
        public MyOverlay(GeoPoint gp1){
        	mGpt1 = gp1;
        }   
        public void draw(Canvas canvas, MapView mapv, boolean shadow){
        super.draw(canvas, mapv, shadow);
        Paint paint;
        paint = new Paint();
        paint.setColor(Color.GREEN);
       
        paint.setAntiAlias(true);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        
        Point pt1 = new Point();
        	Projection projection = map.getProjection();
        	canvas.drawPoint(pt1.x, pt1.y, paint);
        	
        	/*projection.toPixels(mGpt1, pt1);
        	Path p = new Path();
        	
        	for (int i = 0; i < geoPointsArray.size(); i++) {
        		if (i == geoPointsArray.size() - 1) {
        			break;
        		}
        	Point from = new Point();
        	Point to = new Point();
        	projection.toPixels(geoPointsArray.get(i), from);
        	projection.toPixels(geoPointsArray.get(i + 1), to);
        	p.moveTo(from.x, from.y);
        	
        	canvas.drawLine(from.x, from.y, to.x, to.y, paint);
        	super.draw(canvas, map, shadow);*/
        	//p.lineTo(to.x, to.y);
        	}
        	
        	
    /*    Paint mPaint = new Paint();
        mPaint.setStyle(Style.STROKE);
        paint.setStrokeWidth(3);
        mPaint.setColor(0xFFFF0000);
        mPaint.setAntiAlias(true);
        canvas.drawPath(p, mPaint);
        super.draw(canvas, map, shadow);
    }*/   
        
    }
    @Override
    protected void onStart() {
        super.onStart();

        // register your listener with the location manager
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unregister your listener from the location manager
    }
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }
       
       public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    protected boolean isRouteDisplayed() {
        // TODO Auto-generated method stub
        return false;
    }}