package com.android.project;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MyMapActivity extends MapActivity implements LocationListener {

	        LinearLayout linearLayout;
	        MapView mapView;
	        TextView textview;
	        private Road mRoad;
	        MapController myController;
	        GeoPoint p;
	        Context context;
	        SharedPreferences myPrefs;
	        String prefToken="";
	        	        
	        @Override
	        public void onCreate(Bundle savedInstanceState) {
	                super.onCreate(savedInstanceState);
	                setContentView(R.layout.main);
	                LocationManager myManager=(LocationManager)this.getSystemService(this.LOCATION_SERVICE);
	        		Criteria criteria=new Criteria();
	        		criteria.setAltitudeRequired(false);
	        		criteria.setBearingRequired(false);
	        		criteria.setPowerRequirement(Criteria.POWER_LOW);
	        		criteria.setAccuracy(Criteria.ACCURACY_FINE);
	        		String provider=myManager.getBestProvider(criteria, true);
	        		mapView = (MapView) findViewById(R.id.mapview);
	        		myController=mapView.getController();
	        		myManager.requestLocationUpdates(provider,6000,100,this);
	        		Location myLocation=myManager.getLastKnownLocation(provider);
	        		context=this.getApplicationContext();
	        		myPrefs = this.getSharedPreferences("myPrefs", MODE_WORLD_READABLE);
	        		prefToken = myPrefs.getString("token_value", "nothing");
	        		drawPath(myLocation);
	        }//END ON CREATE

	        Handler mHandler = new Handler() {
	                public void handleMessage(android.os.Message msg) {
	                	   if(mRoad.mName==null && mRoad.mDescription==null) {
	                		Toast.makeText(context,"Unable To Get Directions !!",Toast.LENGTH_LONG).show();
                           	finish();
                           	goBackToScreen();
	                	   }
	                	   else {
	                        textview.setText(mRoad.mName + " " + mRoad.mDescription);
	                        MapOverlay mapOverlay = new MapOverlay(mRoad, mapView);
	                        List<Overlay> listOfOverlays = mapView.getOverlays();
	                        listOfOverlays.add(mapOverlay);
	                        mapView.invalidate();
	                	   }
	                };
	        };
	    
	        private InputStream getConnection(String url) {
	                InputStream is = null;
	                try {
	                        URLConnection conn = new URL(url).openConnection();
	                        is = conn.getInputStream();
	                } catch (MalformedURLException e) {
	                        e.printStackTrace();
	                } catch (IOException e) {
	                        e.printStackTrace();
	                }
	                return is;
	        }

	        @Override
	        protected boolean isRouteDisplayed() {
	                return false;
	        }

			public void onLocationChanged(Location location) {
				if (location != null) {
					double lat = location.getLatitude();
					double lng = location.getLongitude();
					int la=(int) lat * 1000000;
					int lo=(int) lng * 1000000;
					p = new GeoPoint(la,lo);
					myController.animateTo(p);
					mapView.invalidate();
				}

		}
						
public void onProviderDisabled(String provider) {
				Toast.makeText(this,"GPS is Disabled !!",Toast.LENGTH_LONG).show();
			}

			public void onProviderEnabled(String provider) {
				Toast.makeText(this,"GPS is Enabled !!",Toast.LENGTH_LONG).show();
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {				
			}
			public void goBackToScreen() {
				Intent i=new Intent(context,SummeryView.class);
				i.putExtra("ticket1",prefToken);
				startActivity(i);
			}
			public void drawPath(Location myLocation) {
				try {
        			
        		final double sla=myLocation.getLatitude();
                final double slng=myLocation.getLongitude();
        		final double dtla=(Double)getIntent().getSerializableExtra("dlat");
    	        final double dtlon=(Double)getIntent().getSerializableExtra("dlon");
    	        mapView.setBuiltInZoomControls(true);
                textview=(TextView)findViewById(R.id.description);
                new Thread() {
                    @Override
                    public void run() {
                             String url = RoadProvider.getUrl(sla,slng,dtla,dtlon);
                                InputStream is = getConnection(url);
                                mRoad = RoadProvider.getRoute(is);     
                                mHandler.sendEmptyMessage(0);
                        		}
                	}.start();
                /******VERY FISHY CODE **********/
        		List<Overlay> mapOverlays = mapView.getOverlays();
        		Drawable drawable = this.getResources().getDrawable(R.drawable.marker);
        		MyItemizedOverlay itemizedoverlay = new MyItemizedOverlay(drawable);
        		GeoPoint spt=new GeoPoint(
        				(int)(sla*1E6),
        				(int)(slng*1E6)
        				);
        		OverlayItem myItem=new OverlayItem(spt,"This is Source","");
        		itemizedoverlay.addOverlay(myItem);
        		mapOverlays.add(itemizedoverlay);  		
        		GeoPoint dpt=new GeoPoint(
        				(int)(dtla*1E6),
        				(int)(dtlon*1E6)
        				);
        		OverlayItem myNewItem=new OverlayItem(dpt,"This is a Destination","");
        		itemizedoverlay.addOverlay(myNewItem);
        		mapOverlays.add(itemizedoverlay);
        		}//END TRY
        		catch(NullPointerException ex) {
        			Toast.makeText(this,"Unable To Resolve Location Please Try Again !!",Toast.LENGTH_LONG).show();
        			this.finish();
        		}//END CATCH
			}
	}

	class MapOverlay extends com.google.android.maps.Overlay {
	        Road mRoad;
	        ArrayList<GeoPoint> mPoints;
	        
	        public MapOverlay(Road road, MapView mv) {
	                mRoad = road;
	                if (road.mRoute.length > 0) {
	                        mPoints = new ArrayList<GeoPoint>();
	                        for (int i = 0; i < road.mRoute.length; i++) {
	                                mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000),
	                                                (int) (road.mRoute[i][0] * 1000000)));
	                        }
	                        int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(
	                                        mPoints.size() - 1).getLatitudeE6() - mPoints.get(0)
	                                        .getLatitudeE6()) / 2);
	                        int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(
	                                        mPoints.size() - 1).getLongitudeE6() - mPoints.get(0)
	                                        .getLongitudeE6()) / 2);
	                        GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);

	                        MapController mapController = mv.getController();
	                        mapController.animateTo(moveTo);
	                        mapController.setZoom(12);
	                        mv.invalidate();
	                }
	        }

	        @Override
	        public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
	                super.draw(canvas, mv, shadow);
	                drawPath(mv, canvas);
	        
	                return true;
	        }

	        public void drawPath(MapView mv, Canvas canvas) {
	                int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
	                Paint paint = new Paint();
	                paint.setColor(Color.BLUE);
	                paint.setStyle(Paint.Style.STROKE);
	                paint.setStrokeWidth(3);
	                for (int i = 0; i < mPoints.size(); i++) {
	                        Point point = new Point();
	                        mv.getProjection().toPixels(mPoints.get(i), point);
	                        
	                        x2 = point.x;
	                        y2 = point.y;
	                        if (i > 0) {
	                                canvas.drawLine(x1, y1, x2, y2, paint);
	                        }
	                        x1 = x2;
	                        y1 = y2;
	                }
	        }
	        
	}

	

