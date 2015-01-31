package dosaki.net.codename_strain;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.*;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.location.*;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapMenu extends MapActivity {
	private String username;
	
	private Button btMail;
	private Button btNotif;
	private Button btCharInfo;
	private Button btBag;
	private ImageButton btPulse; 
	private TextView tvHealthDisplay;
	
	private LocationManager myLocationManager;
	private LocationListener myLocationListener;
	private MapView myMapView;
	private MapController myMapController;
	private MapOverlay itemizedoverlay;
	private MapOverlay infectedOverlay;
	private MapOverlay humanOverlay;
	private OverlayItem meMarker;
	private MapView mapView;
	private String currentHealth;
	
	private WebserviceHandler service;
	
	private Drawable selfMarker;
	private Drawable humanMarker;
	private Drawable infectedMarker;
	
	private ArrayList<String> notificationList;
	private int notificationCount;
	
	private void CenterLocatio(GeoPoint centerGeoPoint)
	{
	  myMapController.animateTo(centerGeoPoint);
	};
	
	private void findNearbyPlayers(String location, String player_id){
		ArrayList<String> nearbyHumans = new ArrayList<String>(service.getNearbyHumanPlayers(location, "100", service.getPlayerPer(player_id)));
		ArrayList<String> nearbyInfected = new ArrayList<String>(service.getNearbyInfectedPlayers(location, "100", service.getPlayerPer(player_id)));
		humanOverlay.clear();
		infectedOverlay.clear();
		
		for(int i = 0; i < nearbyInfected.size(); i++){
			String[] ipair = nearbyInfected.get(i).split("\\|");
			String iplayer_id = ipair[0];
			String ispoint = ipair[1];
			String[] ipoint = ispoint.split(",");
			String ilat = ipoint[0];
			String ilon = ipoint[1];
			
			GeoPoint pos = new GeoPoint(Integer.parseInt(ilat), Integer.parseInt(ilon));
			
			infectedOverlay.addOverlay(new OverlayItem(pos, service.getUsername(iplayer_id), service.getClassname(iplayer_id)));
		}
		
		for(int i = 0; i < nearbyHumans.size(); i++){
			String[] hpair = nearbyHumans.get(i).split("\\|");
			String hplayer_id = hpair[0];
			String hspoint = hpair[1];
			String[] hpoint = hspoint.split(",");
			String hlat = hpoint[0];
			String hlon = hpoint[1];
			
			GeoPoint pos = new GeoPoint(Integer.parseInt(hlat), Integer.parseInt(hlon));
			
			humanOverlay.addOverlay(new OverlayItem(pos, service.getUsername(hplayer_id), service.getClassname(hplayer_id)));
		}
		mapView.invalidate();
	}
	
	private void findNearbyItems(String location, String player_id){
		
	}
	
	private void checkHealth(String player_id){
		currentHealth = service.getPlayerHealth(player_id);
	}
	
	private void checkForUpdates(){
		/*notificationList = service.getAllTheNotifications(username);
		if(notificationList == null)
			notificationCount = 0;
		else
			notificationCount = notificationList.size();*/
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_menu);
        service = new WebserviceHandler();
        
        username = getIntent().getStringExtra("username").toString();
        final String playerId = service.getActivePlayer(this.username);
        
        currentHealth = "Loading...";
        checkHealth(service.getActivePlayer(username));
        tvHealthDisplay = (TextView)findViewById(R.id.tv_health);
        tvHealthDisplay.setText(currentHealth);
        btMail = (Button)findViewById(R.id.bt_menu);
        
        service.goOnline(playerId);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        myMapController = mapView.getController();
        myMapController.setZoom(18);
        
        myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        myLocationListener = new MyLocationListener();
        
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        selfMarker = this.getResources().getDrawable(R.drawable.map_marker_self);
        humanMarker = this.getResources().getDrawable(R.drawable.map_marker_human);
        infectedMarker = this.getResources().getDrawable(R.drawable.map_marker_infected);
        itemizedoverlay = new MapOverlay(selfMarker, this, username);
        
        GeoPoint initGeoPoint = new GeoPoint(
        		(int)(myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()*1000000),
        		(int)(myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()*1000000));
        CenterLocatio(initGeoPoint);
        service.updateLocation(initGeoPoint.toString(), playerId);
        
        meMarker = new OverlayItem(initGeoPoint, username, "This is you.");
        itemizedoverlay.addOverlay(meMarker);
        mapOverlays.add(itemizedoverlay);
        humanOverlay = new MapOverlay(humanMarker, this, username);
        mapOverlays.add(humanOverlay);
        infectedOverlay = new MapOverlay(infectedMarker, this, username);
        mapOverlays.add(infectedOverlay);
        
        btNotif=(Button)findViewById(R.id.bt_map);
        btNotif.setVisibility(Button.GONE);
        /*btNotif.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	// custom dialog
    			final Dialog dialog = new Dialog(v.getContext());
    			dialog.setContentView(R.layout.dialog_notifications);
    			dialog.setTitle("Notifications");
    	
    			// set the custom dialog components
    			Button btDismissAll = (Button)findViewById(R.id.bt_dismissallnotif);		
    			btDismissAll.setOnClickListener(new OnClickListener() {
    				public void onClick(View v) {
    					service.dismissAllTheNotifications(username);
    					dialog.dismiss();
    				}
    			});
    			dialog.show();
             }
        });*/

        btCharInfo=(Button)findViewById(R.id.bt_charinfo);
        btCharInfo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MapMenu.this, CharInfo.class);
            	intent.putExtra("username", username);
				startActivityForResult(intent, 0);
             }
        });
        
        btBag=(Button)findViewById(R.id.bt_bag);
        btBag.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MapMenu.this, BagMenu.class);
            	intent.putExtra("username", username);
            	//intent.putExtra("player_id", playerId);
				startActivityForResult(intent, 0);
             }
        });
        
        btMail.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MapMenu.this, MailMenu.class);
            	intent.putExtra("username", username);
				startActivityForResult(intent, 0);
             }
        });
        
        btPulse=(ImageButton)findViewById(R.id.bt_pulse);
        btPulse.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            	GeoPoint geoPoint = new GeoPoint(
                		(int)(myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()*1000000),
                		(int)(myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()*1000000));
            	service.updateLocation(geoPoint.toString(), playerId);
            	tvHealthDisplay.setText(currentHealth);
            	findNearbyPlayers(geoPoint.toString(), playerId);
            	findNearbyItems(geoPoint.toString(), playerId);
             }
        });
        
	    performOnBackgroundThread(tvHealthDisplay, new Runnable() {
			public void run() {
				while(true){
					checkHealth(service.getActivePlayer(username));
					checkForUpdates();
					try {
						Thread.sleep(10000);
					}
                    catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_map_menu, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected void onDestroy (){
		service.goOffline(service.getActivePlayer(this.username));
		super.onDestroy();
	}
	 
	 private class MyLocationListener implements LocationListener{
		  public void onLocationChanged(Location argLocation) {
		   GeoPoint myGeoPoint = new GeoPoint(
		    (int)(argLocation.getLatitude()*1000000),
		    (int)(argLocation.getLongitude()*1000000));
		   CenterLocatio(myGeoPoint);
		   tvHealthDisplay.setText(currentHealth);
		   btNotif.setText(notificationCount);
		   meMarker = new OverlayItem(myGeoPoint, username, "This is you.");
		   itemizedoverlay.clear();
		   itemizedoverlay.addOverlay(meMarker);
		   service.updateLocation(myGeoPoint.toString(), service.getActivePlayer(username));
		  }

		  public void onProviderDisabled(String provider) {
		   // TODO Auto-generated method stub
		  }

		  public void onProviderEnabled(String provider) {
		   // TODO Auto-generated method stub
		  }

		  public void onStatusChanged(String provider,
		    int status, Bundle extras) {
		   // TODO Auto-generated method stub
		  }
		 }
	 
	 public static Thread performOnBackgroundThread(TextView tv, final Runnable runnable) {
		    final Thread t = new Thread() {
		        @Override
		        public void run() {
		            try {
		                runnable.run();
		            } finally {

		            }
		        }
		    };
		    t.start();
		    return t;
	 }
}
