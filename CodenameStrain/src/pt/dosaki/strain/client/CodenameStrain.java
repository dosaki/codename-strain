package pt.dosaki.strain.client;

import android.os.*;
import android.app.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.support.v4.app.NavUtils;
import android.app.*;
import android.os.*;
import android.content.Context;
import android.content.Intent;

import org.apache.http.protocol.RequestUserAgent;
import org.ksoap2.*;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class CodenameStrain extends Activity {
    /** Called when the activity is first created. */
    public String username;
    private Button btLogin;
    private Button btRegister;
    private EditText tfEmail;
    private EditText tfPassword;
    private TextView tvFirstTime;

    WebserviceHandler service;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        service = new WebserviceHandler();
        
        btLogin=(Button)findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	tryLogin();
             }
        });
        btRegister = (Button)findViewById(R.id.bt_register);
        btRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				register();
			}
		});
    }

    public void register()
    {
    	Intent intent = new Intent(CodenameStrain.this, RegisterScreen.class);
		startActivityForResult(intent, 0);
    }
    
    public void tryLogin()
    {
    	TextView tvFeedback = (TextView)findViewById(R.id.tv_loginfeedback);
        try {
        	tfEmail = (EditText)findViewById(R.id.tf_login);
            tfPassword = (EditText)findViewById(R.id.tf_password);
            tvFirstTime = (TextView)findViewById(R.id.tv_firsttime);
            
            String result = service.auth(tfEmail.getText().toString(), tfPassword.getText().toString());

           if(result.toString().equals("false"))
           {
        	   tvFeedback.setText(R.string.error_login);
           }
           else if(result.toString().equals("true"))
           {
        	   try {
        		    

	   	            this.username = service.getUsernameFromEmail(tfEmail.getText().toString());
	   	            
	   	            tvFeedback.setText("Success! Welcome, " + username);
	   				Button btLogin = (Button)findViewById(R.id.bt_login);
	   				btLogin.setVisibility(Button.GONE);
	   				//tvFeedback.setText("");
	   				tfEmail.setVisibility(Button.GONE);
	   				tfPassword.setVisibility(Button.GONE);
	   				tvFirstTime.setVisibility(TextView.GONE);
	   				btRegister.setVisibility(Button.GONE);
	           	   // open Game UI
	   				Intent intent = new Intent(CodenameStrain.this, MapMenu.class);
	   				intent.putExtra("username", this.username);
	   				startActivityForResult(intent, 0);
	   		        finish();
        	   } catch (Exception e) {
                   tvFeedback.setText(".:: " + e.getMessage());
                   }
           }
           else
           {
        	   tvFeedback.setText("Failed: " + result.toString());
           }
        } catch (Exception e) {
            tvFeedback.setText(".: " + e.getMessage());
            }
    }
}