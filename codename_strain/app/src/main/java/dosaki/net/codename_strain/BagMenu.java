package dosaki.net.codename_strain;

import java.util.ArrayList;
import java.util.Iterator;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class BagMenu extends Activity {
	
	private ArrayList<Button> btItemSlot; 
	
	private Button btDeleteItem;
	private Button btSendItem;
	private ImageButton btNextPage;
	
	private String username;
	private String playerId;
	private String inventory_id;
	private WebserviceHandler service;
	private int selectedSlot;

	private int itemCount;
	private int it;

	private ArrayList<String> getAllTheItems()
	{
		ArrayList<String> baggedItems = service.getAllTheBaggedItems(inventory_id);
		
		itemCount = baggedItems.size();
		
		return baggedItems;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag_menu);
        
        service = new WebserviceHandler(); 
        
        username = getIntent().getStringExtra("username").toString();
        playerId = service.getActivePlayer(username);
        inventory_id = service.getInventory(playerId);
        btItemSlot = new ArrayList<Button>();
        selectedSlot = -1;
        //String maxCapacity = service.getStorageMaxCapacity("1");
        
        btNextPage = (ImageButton)findViewById(R.id.bt_next);
        btSendItem = (Button)findViewById(R.id.bt_senditem);
        btDeleteItem = (Button)findViewById(R.id.bt_delitem);
        
        btDeleteItem.setEnabled(false);
        btSendItem.setEnabled(false);
        ArrayList<String> baggedItems = getAllTheItems();
        
        btItemSlot.add((Button)findViewById(R.id.item_slot1));
        btItemSlot.add((Button)findViewById(R.id.item_slot2));
        btItemSlot.add((Button)findViewById(R.id.item_slot3));
        btItemSlot.add((Button)findViewById(R.id.item_slot4));
        btItemSlot.add((Button)findViewById(R.id.item_slot5));
        btItemSlot.add((Button)findViewById(R.id.item_slot6));
        btItemSlot.add((Button)findViewById(R.id.item_slot7));
        btItemSlot.add((Button)findViewById(R.id.item_slot8));
        btItemSlot.add((Button)findViewById(R.id.item_slot9));
        btItemSlot.add((Button)findViewById(R.id.item_slot10));
        btItemSlot.add((Button)findViewById(R.id.item_slot11));
        btItemSlot.add((Button)findViewById(R.id.item_slot12));
        btItemSlot.add((Button)findViewById(R.id.item_slot13));
        btItemSlot.add((Button)findViewById(R.id.item_slot14));
        btItemSlot.add((Button)findViewById(R.id.item_slot15));
        btItemSlot.add((Button)findViewById(R.id.item_slot16));
        btItemSlot.add((Button)findViewById(R.id.item_slot17));
        btItemSlot.add((Button)findViewById(R.id.item_slot18));
        btItemSlot.add((Button)findViewById(R.id.item_slot19));
        btItemSlot.add((Button)findViewById(R.id.item_slot20));
        
        for(int i = 0; i < btItemSlot.size(); i++)
    	{
    		btItemSlot.get(i).setVisibility(Button.INVISIBLE);
    	}

        if(itemCount > 20)
        {
        	btNextPage.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
        }
        else
        {
        	btNextPage.setEnabled(false);
        	
        	for(it = 0; it < baggedItems.size(); it++)
        	{
        		if(it >= btItemSlot.size())
        			break;
        		btItemSlot.get(it).setText(baggedItems.get(it));
        		btItemSlot.get(it).setVisibility(0);
        		btItemSlot.get(it).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if(selectedSlot != -1)
						{
							btItemSlot.get(selectedSlot).setEnabled(true);
						}
						btItemSlot.get(it).setEnabled(false);
						selectedSlot = it;
						btDeleteItem.setEnabled(true);
						btSendItem.setEnabled(true);
					}
				});
        	}
        }
        
        btDeleteItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				service.deleteItem(btItemSlot.get(selectedSlot).getText().toString(), service.getInventory(playerId));
			}
		});
        
        btSendItem.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// custom dialog
    			final Dialog dialog = new Dialog(v.getContext());
    			dialog.setContentView(R.layout.dialog_notifications);
    			dialog.setTitle("Send Item");
    	
    			// set the custom dialog components
    			Button btConfirmSend = (Button)findViewById(R.id.bt_sendItem);		
    			btConfirmSend.setOnClickListener(new OnClickListener() {
    				public void onClick(View v) {
    					EditText tfGiftedUser = (EditText)findViewById(R.id.et_gifted);
    					
    					if(service.getUserId(tfGiftedUser.getText().toString()).equals(""))
    					{
    						TextView tvFeedback = (TextView)findViewById(R.id.tv_sending_feedback);
    						tvFeedback.setText(R.string.text_sendingerror_username);
    					}
    					else
    					{
    						if(service.putItemInStorage(btItemSlot.get(selectedSlot).getText().toString(), tfGiftedUser.getText().toString()).equals("ok"))
    						{
    							dialog.dismiss();
    						}
    						else
    						{
    							TextView tvFeedback = (TextView)findViewById(R.id.tv_sending_feedback);
        						tvFeedback.setText(R.string.text_sendingerror_mailbox);
    						}
        					
    					}
    				}
    			});
    			dialog.show();
			}
		});

        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_bag_menu, menu);
        return true;
    }

    
}
