package pt.dosaki.strain.client;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class CharInfo extends Activity {
	private TextView tvUsername;
	private TextView tvClassname;
	private TextView tvStrVal;
	private TextView tvAccVal;
	private TextView tvStaVal;
	private TextView tvPerVal;
	private TextView tvStatDescript;
	private TextView tvHealth;
	
	private Button btnStrInfo;
	private Button btnAccInfo;
	private Button btnStaInfo;
	private Button btnPerInfo;
	
	private String userName;
	private String playerID;
	
    private WebserviceHandler service;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_info);
        
        service = new WebserviceHandler();
        
        userName = getIntent().getStringExtra("username").toString();
        playerID = service.getActivePlayer(userName.trim());
        
        tvUsername = (TextView)findViewById(R.id.tv_username);
        tvUsername.setText(userName);
        
        tvClassname = (TextView)findViewById(R.id.tv_classname);
        tvClassname.setText(service.getClassname(playerID));
        
        tvHealth = (TextView)findViewById(R.id.tv_health);
        tvHealth.setText(service.getPlayerHealth(playerID) + "/" + service.getPlayerMaxHealth(playerID));
        		
        ArrayList<String> statList = new ArrayList<String>();
        
        statList = service.getStatList(playerID);
        
        if(statList.isEmpty())
        {
        	System.err.println("Something went wrong with the stat list.");
        }
        else
        {
        	tvStrVal = (TextView)findViewById(R.id.tv_str_val);
        	tvAccVal = (TextView)findViewById(R.id.tv_acc_val);
        	tvStaVal = (TextView)findViewById(R.id.tv_sta_val);
        	tvPerVal = (TextView)findViewById(R.id.tv_per_val);
        	
        	btnStrInfo = (Button)findViewById(R.id.bt_str_info);
        	btnAccInfo = (Button)findViewById(R.id.bt_acc_info);
        	btnStaInfo = (Button)findViewById(R.id.bt_sta_info);
        	btnPerInfo = (Button)findViewById(R.id.bt_per_info);
        	
        	btnStrInfo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					tvStatDescript = (TextView)findViewById(R.id.text_stat_description);
					tvStatDescript.setText(R.string.text_str_descript);
				}
			});
        	
        	btnAccInfo.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					tvStatDescript = (TextView)findViewById(R.id.text_stat_description);
					tvStatDescript.setText(R.string.text_acc_descript);
				}
			});
        	
        	btnStaInfo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					tvStatDescript = (TextView)findViewById(R.id.text_stat_description);
					tvStatDescript.setText(R.string.text_sta_descript);
				}
			});
        	
        	btnPerInfo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					tvStatDescript = (TextView)findViewById(R.id.text_stat_description);
					tvStatDescript.setText(R.string.text_per_descript);
				}
			});
        	
	        tvStrVal.setText(statList.get(0));
	        tvAccVal.setText(statList.get(1));
	        tvStaVal.setText(statList.get(2));
	        tvPerVal.setText(statList.get(3));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_char_info, menu);
        return true;
    }

    
}
