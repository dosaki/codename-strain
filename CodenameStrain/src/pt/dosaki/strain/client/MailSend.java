package pt.dosaki.strain.client;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.NavUtils;

public class MailSend extends Activity {
	
	private Button btSend;
	private Button btCancel;
	private Button btAttach;
	
	private EditText recipient;
	private EditText mailBody;
	
	private String username;
	private String activePlayer_id;
	
	private WebserviceHandler service;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_send);
        
        service = new WebserviceHandler();
        
        username = getIntent().getStringExtra("username").toString();
        activePlayer_id = service.getActivePlayer(this.username);
        
        btSend = (Button)findViewById(R.id.bt_mailsend);
    	btCancel = (Button)findViewById(R.id.bt_mailcancel);
    	recipient = (EditText)findViewById(R.id.et_recipient);
    	mailBody = (EditText)findViewById(R.id.et_messagebody);
    	
    	btSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String result = service.sendMail(activePlayer_id, recipient.getText().toString(), mailBody.getText().toString());
				AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
				dialog.setTitle("");
				if(result.equals("ok"))
				{
					dialog.setMessage(getText(R.string.text_sendingok));
					recipient.setText("");
					mailBody.setText("");
				}
				else if(result.equals("error:full"))
					dialog.setMessage(getText(R.string.text_sendingerror_mailbox));
				else if(result.equals("error:username"))
					dialog.setMessage(getText(R.string.text_sendingerror_username));
				else
					dialog.setMessage("Not sure what went wrong.");
				
				dialog.show();
			}
		});
    	
    	btCancel.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mail_send, menu);
        return true;
    }

    
}
