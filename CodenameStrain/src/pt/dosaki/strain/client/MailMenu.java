package pt.dosaki.strain.client;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class MailMenu extends Activity {
	private Spinner spMailList;
	private ImageButton btRefresh;
	private Button btDelete;
	private Button btNewMail;
	private Button btGetItem;
	private TextView tvEmailBody;
	
	private String username;
	private String mailbox;
	private String activePlayer_id;
	
	private WebserviceHandler service;	
	

	private ArrayList<String> populateSpinner(String mailbox_id)
	{
		ArrayList<String> allTheMailItems = service.getAllTheMailFrom(mailbox_id);
		
		if(allTheMailItems.isEmpty())
			return null;

		return allTheMailItems;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_menu);
        
        service = new WebserviceHandler();
        
        username = getIntent().getStringExtra("username").toString();
        activePlayer_id = service.getActivePlayer(this.username);
        mailbox = service.getMailbox(activePlayer_id);
        		
        spMailList = (Spinner)findViewById(R.id.sp_mailselect);
    	btRefresh = (ImageButton)findViewById(R.id.imgbt_refresh);
    	btDelete = (Button)findViewById(R.id.bt_maildelete);
    	btNewMail = (Button)findViewById(R.id.bt_mailarchive);
    	btGetItem = (Button)findViewById(R.id.bt_mailitems);
    	tvEmailBody = (TextView)findViewById(R.id.tv_mailtext);
    	
    	btNewMail.setEnabled(false);
		btDelete.setEnabled(false);
		btGetItem.setEnabled(false);
		
		final ArrayList<String> mailItems = populateSpinner(mailbox);
		
        if(mailItems == null)
        {
        	tvEmailBody.setText(R.string.text_mailempty);
			btNewMail.setEnabled(false);
			btDelete.setEnabled(false);
			btGetItem.setEnabled(false);
        }
        else
        {
    		ArrayList<String> formatedMailItems = new ArrayList<String>();
    		
    		for(int i = 0; i<mailItems.size(); i++)
    		{
    			formatedMailItems.add((i+1) + ". From: " + service.getItemCreatorName(mailItems.get(i).toString()));
    		}
    		
        	ArrayAdapter<String> mailList = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, formatedMailItems);
    		mailList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		
        	spMailList.setAdapter(mailList);
        }
    	
        btRefresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ArrayList<String> mailItems = populateSpinner(mailbox);
				
		        if(mailItems == null)
		        {
		        	tvEmailBody.setText(R.string.text_mailempty);
					btNewMail.setEnabled(false);
					btDelete.setEnabled(false);
					btGetItem.setEnabled(false);
		        }
		        else
		        {
		    		ArrayList<String> formatedMailItems = new ArrayList<String>();
		    		
		    		for(int i = 0; i<mailItems.size(); i++)
		    		{
		    			formatedMailItems.add((i+1) + ". From: " + service.getItemCreatorName(mailItems.get(i).toString()));
		    		}
		    		
		        	ArrayAdapter<String> mailList = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, formatedMailItems);
		    		mailList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    		
		        	spMailList.setAdapter(mailList);
		        }
			}
		});
        
        btNewMail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MailMenu.this, MailSend.class);
            	intent.putExtra("username", username);
				startActivityForResult(intent, 0);
			}
		});
        
        spMailList.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        		if(parent.getItemAtPosition(pos).toString().equals(""))
        		{
        			
        		}
        		else
        		{
        			final String itemInst_id = mailItems.get(pos);
        			tvEmailBody.setText(service.getMailText(itemInst_id));
        			btNewMail.setEnabled(true);
        			btDelete.setEnabled(true);
        			btGetItem.setEnabled(true);
        			
        	        btGetItem.setOnClickListener(new OnClickListener() {
        				public void onClick(View v) {
        					String aftermath = service.putItemInStorage(itemInst_id, service.getInventory(activePlayer_id));
        					if(aftermath.equals("ok"))
        					{
        						AlertDialog.Builder dialog = new AlertDialog.Builder(MailMenu.this);
        						  dialog.setTitle(service.getItemName(itemInst_id));
        						  dialog.setMessage("Item obtained");
        						  dialog.show(); 
        					}
        					else
        					{
        						AlertDialog.Builder dialog = new AlertDialog.Builder(MailMenu.this);
        						  dialog.setTitle(itemInst_id);
        						  dialog.setMessage(getText(R.string.text_fullinventory));
        						  dialog.show(); 
        					}
        					
        					ArrayList<String> mailItems = populateSpinner(mailbox);
        					
        			        if(mailItems == null)
        			        {
        			        	tvEmailBody.setText(R.string.text_mailempty);
        						btNewMail.setEnabled(false);
        						btDelete.setEnabled(false);
        						btGetItem.setEnabled(false);
        			        }
        			        else
        			        {
        			    		ArrayList<String> formatedMailItems = new ArrayList<String>();
        			    		
        			    		for(int i = 0; i<mailItems.size(); i++)
        			    		{
        			    			formatedMailItems.add(i+1 + ". From: " + service.getItemCreatorName(mailItems.get(i).toString()));
        			    		}
        			    		
        			        	ArrayAdapter<String> mailList = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_spinner_item, formatedMailItems);
        			    		mailList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        			    		
        			        	spMailList.setAdapter(mailList);
        			        }
        				}
        			});
        		}
        	}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
        });
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mail_menu, menu);
        return true;
    }

    
}
