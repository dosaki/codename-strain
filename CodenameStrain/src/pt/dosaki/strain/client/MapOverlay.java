package pt.dosaki.strain.client;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapOverlay extends ItemizedOverlay {
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private String user;
	private Context mContext;
	
	public WebserviceHandler service;
	
	public MapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		service = new WebserviceHandler();
		populate();
	}
	public MapOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		  service = new WebserviceHandler();
		  populate();
		}
	public MapOverlay(Drawable defaultMarker, Context context, String username) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
		  user = username;
		  service = new WebserviceHandler();
		  populate();
		}
	
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	public void clear() {
	    mOverlays.clear();
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
	
	@Override
	protected boolean onTap(final int index) {		
		if(mOverlays.get(index).getTitle().trim() == user)
		{
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			  dialog.setTitle(item.getTitle());
			  dialog.setMessage(item.getSnippet());
			  dialog.show();
		}
		else
		{
			// custom dialog
			final Dialog dialog = new Dialog(mContext);
			dialog.setContentView(R.layout.dialog_friendly_popup);
			dialog.setTitle(mOverlays.get(index).getTitle());
	
			// set the custom dialog components
			TextView username = (TextView) dialog.findViewById(R.id.tv_name);
			TextView factionClassPair = (TextView) dialog.findViewById(R.id.tv_fact_class);
			final TextView attackFeedback = (TextView) dialog.findViewById(R.id.tv_attack_feedback);
			username.setText(mOverlays.get(index).getTitle());
			factionClassPair.setText(mOverlays.get(index).getSnippet());
	
			Button btAttack = (Button) dialog.findViewById(R.id.bt_attack);
			btAttack.setText(R.string.button_attack);
			Button btOk = (Button) dialog.findViewById(R.id.bt_ok);
			btOk.setText(R.string.button_ok);
			
			btOk.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			// if button is clicked
			btAttack.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String victim = mOverlays.get(index).getTitle().trim();
					String[] sitrep = service.strikeRanged(victim, service.getActivePlayer(user)).split(",");
					
					if(service.isInRange(service.getActivePlayer(victim), service.getActivePlayer(user)).equals("true"))
					{
						if(sitrep[0].equals("enemy"))
						{
							if(sitrep[1].equals("dead"))
							{
								attackFeedback.setText(mContext.getString(R.string.feedback_positive_kill, sitrep[2]));
							}
							else
							{
								attackFeedback.setText(mContext.getString(R.string.feedback_positive, sitrep[2]));
							}
						}
						else if(sitrep[0].equals("user"))
						{
							if(sitrep[1].equals("dead"))
							{
								attackFeedback.setText(mContext.getString(R.string.feedback_negative_kill, sitrep[2]));
							}
							else
							{
								attackFeedback.setText(mContext.getString(R.string.feedback_negative, sitrep[2]));
							}
						}
						else
						{
							attackFeedback.setText(R.string.feedback_tie);
						}
					}
					else
					{
						attackFeedback.setText(R.string.feedback_toofar);
					}
				}
			});
	
			dialog.show();
		}
	  return true;
	}

}
