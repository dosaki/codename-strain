package dosaki.net.codename_strain;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import com.google.android.maps.GeoPoint;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class RegisterScreen extends Activity {

	private EditText username;
	private EditText password;
	private EditText email;
	private Spinner spClassChooser;
	private Spinner spFactionChooser;
	private TextView tvFactionDescription;
	private TextView tvClassDescription;
	private TextView tvRestrictions;
	private TextView tvSubclass;
	private TextView tvSubclassesDescript;
	private TextView tvStrVal;
	private TextView tvAccVal;
	private TextView tvStaVal;
	private TextView tvPerVal;
	private TextView tvFeedbackErrors;
	private ArrayList<String> baseStatList;
	private Button btRegister;
	private ImageView imgUsernameError;
	private ImageView imgEmailError;
	
	private String classChoice;
	private String factionChoice;
	private LocationManager myLocationManager;
	private LocationListener myLocationListener;
	
	private WebserviceHandler service;

    StrainService strainService;
	
    
    
    private boolean isUsernameUnique(String username)
    {
    	if((service.getUserId(username)).equals(""))
    		return true;
    	else
    		return false;
    }
    
    private boolean isEmailUnique(String email)
    {
    	if((service.getUsernameFromEmail(email)).equals(""))
    		return true;
    	else
    		return false;
    }
    
    private void registerUser(String user, String email, String password, String faction, String playerClass)
    {
    	myLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        myLocationListener = new MyLocationListener();
        
        myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
        
        GeoPoint initGeoPoint = new GeoPoint(
        		(int)(myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()*1000000),
        		(int)(myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()*1000000));
        
        service.registerUser(user, email, password, faction, playerClass, initGeoPoint.toString());
    	
    }
    
    private ArrayList<String> getBaseStatList(String className){
		return strainService.getBaseStatList(className);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        service = new WebserviceHandler();
        strainService = new StrainService();
        
        username = (EditText)findViewById(R.id.tf_regusername);
        password = (EditText)findViewById(R.id.tf_regpassword);
        email = (EditText)findViewById(R.id.tf_regemail);
        spFactionChooser = (Spinner)findViewById(R.id.sp_faction_chooser);
        spClassChooser = (Spinner)findViewById(R.id.sp_class_chooser);
        tvFactionDescription = (TextView)findViewById(R.id.tv_faction_description);
        tvClassDescription = (TextView)findViewById(R.id.tv_class_description);
        tvRestrictions = (TextView)findViewById(R.id.tv_restriction_description);
        tvSubclass = (TextView)findViewById(R.id.tv_subclass);
        tvSubclassesDescript = (TextView)findViewById(R.id.tv_subclasses);
        tvStrVal = (TextView)findViewById(R.id.tv_base_str_val);
        tvAccVal = (TextView)findViewById(R.id.tv_base_acc_val);
        tvStaVal = (TextView)findViewById(R.id.tv_base_sta_val);
        tvPerVal = (TextView)findViewById(R.id.tv_base_per_val);
        tvFeedbackErrors = (TextView)findViewById(R.id.tv_feedback_reg_errors);
        btRegister = (Button)findViewById(R.id.bt_do_register);
        imgEmailError = (ImageView)findViewById(R.id.img_email_error);
        imgUsernameError = (ImageView)findViewById(R.id.img_user_error);
        
        imgEmailError.setVisibility(ImageView.GONE);
        imgUsernameError.setVisibility(ImageView.GONE);
        

        spClassChooser.setEnabled(false);
        final ArrayAdapter<CharSequence> humanClassAdapter = ArrayAdapter.createFromResource(this, R.array.humanclass_array, android.R.layout.simple_spinner_item);
        humanClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        final ArrayAdapter<CharSequence> infectedClassAdapter = ArrayAdapter.createFromResource(this, R.array.infectedclass_array, android.R.layout.simple_spinner_item);
        infectedClassAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        ArrayAdapter<CharSequence> factionAdapter = ArrayAdapter.createFromResource(this, R.array.fact_array, android.R.layout.simple_spinner_item);
        factionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFactionChooser.setAdapter(factionAdapter);
        
        spFactionChooser.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    			if(pos == 0){
    		        spClassChooser.setAdapter(humanClassAdapter);
    		        spClassChooser.setEnabled(true);
    		        tvFactionDescription.setText(R.string.text_fact_human_description);
    		        tvSubclass.setText(R.string.text_subclass);
    		        factionChoice = "fact_hum";
    		        spClassChooser.setOnItemSelectedListener(new OnItemSelectedListener() {
    		        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    		        		if(pos == 0) {
                                classChoice = "Soldier";
                                tvClassDescription.setText(R.string.text_class_hum_soldier);
                                tvSubclassesDescript.setText(R.string.text_soldier_subclass);
                                tvRestrictions.setText(R.string.text_soldier_ppty);
    		        		}
    		        		else if(pos == 1){
                                classChoice = "Scientist";
    		        			tvClassDescription.setText(R.string.text_class_hum_scientist);
    		        			tvSubclassesDescript.setText(R.string.text_scientist_subclass);
    		        			tvRestrictions.setText(R.string.text_scientist_ppty);
    		        		}

                            baseStatList = getBaseStatList(classChoice);
                            if (baseStatList != null && !baseStatList.isEmpty()){
                                tvStrVal.setText(baseStatList.get(0));
                                tvAccVal.setText(baseStatList.get(1));
                                tvStaVal.setText(baseStatList.get(2));
                                tvPerVal.setText(baseStatList.get(3));
                            }
                            else{
                                tvStrVal.setText("0");
                                tvAccVal.setText("0");
                                tvStaVal.setText("0");
                                tvPerVal.setText("0");
                            }
    		        	}

						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
    		        
    			}
    			else if(pos == 1){
    		        spClassChooser.setAdapter(infectedClassAdapter);
    		        spClassChooser.setEnabled(true);
    		        tvFactionDescription.setText(R.string.text_fact_infected_description);
    		        tvSubclass.setText(R.string.text_substrain);
    		        factionChoice = "fact_inf";
    		        spClassChooser.setOnItemSelectedListener(new OnItemSelectedListener() {
    		        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    		        		if(pos == 0){
                                classChoice = "Drone";
    		        			tvClassDescription.setText(R.string.text_class_inf_drone);
    		        			tvSubclassesDescript.setText(R.string.text_drone_subclass);
    		        			tvRestrictions.setText(R.string.text_drone_ppty);
    		        		}
    		        		else if(pos == 1){
                                classChoice = "Spitter";
    		        			tvClassDescription.setText(R.string.text_class_inf_spitter);
    		        			tvSubclassesDescript.setText(R.string.text_spitter_subclass);
    		        			tvRestrictions.setText(R.string.text_spitter_ppty);
    		        		}

                            baseStatList = getBaseStatList(classChoice);
                            if (baseStatList != null && !baseStatList.isEmpty()){
                                tvStrVal.setText(baseStatList.get(0));
                                tvAccVal.setText(baseStatList.get(1));
                                tvStaVal.setText(baseStatList.get(2));
                                tvPerVal.setText(baseStatList.get(3));
                            }
                            else{
                                tvStrVal.setText("0");
                                tvAccVal.setText("0");
                                tvStaVal.setText("0");
                                tvPerVal.setText("0");
                            }
    		        	}

						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
    			}
            }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        
        btRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(username.getText().toString().equals("") || password.getText().toString().equals("") || email.getText().toString().equals(""))
				{
					imgUsernameError.setVisibility(ImageView.VISIBLE);
					imgEmailError.setVisibility(ImageView.VISIBLE);
					tvFeedbackErrors.setText("Fill the fields above.");
				}
				else if(isUsernameUnique(username.getText().toString()))
				{
					imgUsernameError.setVisibility(ImageView.VISIBLE);
					tvFeedbackErrors.setText("Username not unique.");
					if(isEmailUnique(email.getText().toString()))
					{
						imgEmailError.setVisibility(ImageView.VISIBLE);
						tvFeedbackErrors.setText("Username and Email are not unique.");
					}
				}
				else if(isEmailUnique(email.getText().toString()))
				{
					imgEmailError.setVisibility(ImageView.VISIBLE);
					tvFeedbackErrors.setText("Email not unique.");
				}
				else
				{
					registerUser(username.getText().toString(), email.getText().toString(), password.getText().toString(), factionChoice, classChoice);
					Intent intent = new Intent(RegisterScreen.this, MapMenu.class);
	   				intent.putExtra("username", username.getText().toString());
	   				startActivityForResult(intent, 0);
				}
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register_screen, menu);
        return true;
    }
    
    private class MyLocationListener implements LocationListener{

		  public void onLocationChanged(Location argLocation) {
		   // TODO Auto-generated method stub
			  GeoPoint myGeoPoint = new GeoPoint(
			    (int)(argLocation.getLatitude()),
			    (int)(argLocation.getLongitude()));
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
}
