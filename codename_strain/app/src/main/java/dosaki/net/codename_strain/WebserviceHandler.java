package dosaki.net.codename_strain;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.renderscript.Int3;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class WebserviceHandler {

	private static final String NAMESPACE = "http://tempuri.org/";
    private static final String URL = "http://192.168.1.5/StrainServer/Service.asmx";

	private static final String ACTION_GETSTATS = "http://tempuri.org/getPlayerStatsById";
    private static final String WEBMETHOD_GETSTATS = "getPlayerStatsById";

    private static final String ACTION_GETCLASSNAME = "http://tempuri.org/getPlayerClassNameById";
    private static final String WEBMETHOD_GETCLASSNAME = "getPlayerClassNameById";

    private static final String SOAP_ACTION = "http://tempuri.org/authenticate";
    private static final String METHOD_NAME = "authenticate";

    private static final String ACTION_GETCLASSARMORS = "http://tempuri.org/getClassAllowedArmorById";
    private static final String WEBMETHOD_GETCLASSARMORS = "getClassAllowedArmorById";

    private static final String ACTION_GETCLASSWEAPONS = "http://tempuri.org/getClassAllowedWeaponTypesById";
    private static final String WEBMETHOD_GETCLASSWEAPONS = "getClassAllowedWeaponTypesById";

    private static final String ACTION_GETACTIVEPLAYER = "http://tempuri.org/getActiveCharacterIdByUsername";
    private static final String WEBMETHOD_GETACTIVEPLAYER = "getActiveCharacterIdByUsername";

    private static final String ACTION_GETALLTHESTOREDITEMS = "http://tempuri.org/getAllStoredItemInstancesById";
    private static final String WEBMETHOD_GETALLTHESTOREDITEMS= "getAllStoredItemInstancesById";

    private static final String ACTION_GETMAILBOX = "http://tempuri.org/getPlayerMailbox";
    private static final String WEBMETHOD_GETMAILBOX= "getPlayerMailbox";

    private static final String ACTION_GETMAILTEXT = "http://tempuri.org/getItemInstanceFlavorById";
    private static final String WEBMETHOD_GETMAILTEXT = "getItemInstanceFlavorById";

    private static final String ACTION_GETINVENTORY = "http://tempuri.org/getPlayerInventory";
    private static final String WEBMETHOD_GETINVENTORY = "getPlayerInventory";

    private static final String ACTION_CHANGESTORAGE = "http://tempuri.org/getItemInstanceFlavorById";
    private static final String WEBMETHOD_CHANGESTORAGE = "getItemInstanceFlavorById";

    private static final String ACTION_GETITEMNAME = "http://tempuri.org/getItemInstanceItemNameById";
    private static final String WEBMETHOD_GETITEMNAME = "getItemInstanceItemNameById";

    private static final String ACTION_GETHEALTH = "http://tempuri.org/getPlayerHealthById";
    private static final String WEBMETHOD_GETHEALTH = "getPlayerHealthById";

    private static final String ACTION_UPDATELOCATION = "http://tempuri.org/updateLocation";
    private static final String WEBMETHOD_UPDATELOCATION = "updateLocation";

    private static final String ACTION_GETNEARBYHUMANS = "http://tempuri.org/getNearbyHumanPlayersPostion";
    private static final String WEBMETHOD_GETNEARBYHUMANS= "getNearbyHumanPlayersPostion";

    private static final String ACTION_GETNEARBYINFECTED = "http://tempuri.org/getNearbyInfectedPlayersPostion";
    private static final String WEBMETHOD_GETNEARBYINFECTED= "getNearbyInfectedPlayersPostion";

    private static final String ACTION_GOONLINE = "http://tempuri.org/goOnline";
    private static final String WEBMETHOD_GOONLINE= "goOnline";

    private static final String ACTION_GOOFFLINE = "http://tempuri.org/goOffline";
    private static final String WEBMETHOD_GOOFFLINE= "goOffline";

    private static final String ACTION_GETPERCEPTION = "http://tempuri.org/getPlayerPerById";
    private static final String WEBMETHOD_GETPERCEPTION= "getPlayerPerById";

    private static final String ACTION_GETPLAYERUSERNAME = "http://tempuri.org/getUsernameByPlayerID";
    private static final String WEBMETHOD_GETPLAYERUSERNAME= "getUsernameByPlayerID";

    private static final String ACTION_GETMAXHEALTH = "http://tempuri.org/getPlayerMaxHealthById";
    private static final String WEBMETHOD_GETMAXHEALTH = "getPlayerMaxHealthById";

    private static final String ACTION_STRIKERANGED = "http://tempuri.org/strikeRanged";
    private static final String WEBMETHOD_STRIKERANGED = "strikeRanged";

    private static final String ACTION_ISINRANGE = "http://tempuri.org/isInRange";
    private static final String WEBMETHOD_ISINRANGE = "isInRange";

	private static final String ACTION_REGISTER = "http://tempuri.org/register";
    private static final String WEBMETHOD_REGISTER = "register";

    private static final String ACTION_GETBASESTATS = "http://tempuri.org/getClassBaseStatsById";
    private static final String WEBMETHOD_GETBASESTATS = "getClassBaseStatsById";

    private static final String ACTION_GETITEMCREATOR = "http://tempuri.org/getItemInstanceOwnerById";
    private static final String WEBMETHOD_GETITEMCREATOR = "getItemInstanceOwnerById";

    private static final String ACTION_GETUSERID = "http://tempuri.org/getUserIdByUsername";
    private static final String WEBMETHOD_GETUSERID= "getUserIdByUsername";

    private static final String ACTION_SENDMAIL = "http://tempuri.org/newMailItem";
    private static final String WEBMETHOD_SENDMAIL = "newMailItem";

    private static final String ACTION_GETALLTHENOTIFICATIONS = "http://tempuri.org/getAllTheNotificationsForUser";
    private static final String WEBMETHOD_GETALLTHENOTIFICATIONS = "getAllTheNotificationsForUser";

    private static final String ACTION_GETUSERNAMEBYEMAIL = "http://tempuri.org/getUsernameByEmail";
    private static final String WEBMETHOD_GETUSERNAMEBYEMAIL= "getUsernameByEmail";

    private static final String ACTION_DISMISSNOTIFICATION = "http://tempuri.org/getAllTheNotificationsForUser";
    private static final String WEBMETHOD_DISMISSNOTIFICATION = "getAllTheNotificationsForUser";

    private static final String ACTION_CLEARNOTIFICATIONS = "http://tempuri.org/clearNotificationsForUser";
    private static final String WEBMETHOD_CLEARNOTIFICATIONS = "clearNotificationsForUser";

    private static final String ACTION_GETSTORAGESIZE = "http://tempuri.org/getStorageMaxCapacityById";
    private static final String WEBMETHOD_GETSTORAGESIZE = "getStorageMaxCapacityById";

    private static final String ACTION_DELETEITEM = "http://tempuri.org/deleteItem";
    private static final String WEBMETHOD_DELETEITEM = "deleteItem";



    public String auth(String email, String password){
    	SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        
        PropertyInfo ppty_email = new PropertyInfo();
        ppty_email.setName("email");
        ppty_email.setValue(email);
        
        PropertyInfo ppty_password = new PropertyInfo();
        ppty_password.setName("password");
        ppty_password.setValue(password);

        request.addProperty(ppty_email);
        request.addProperty(ppty_password);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
			androidHttpTransport.call(SOAP_ACTION, envelope);
	        Object result = envelope.getResponse();
	        return result.toString();
	        
		}
        catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "false";
		}
    }
    	
    public String getUsernameFromEmail(String email){
    	SoapObject requestUsername = new SoapObject(NAMESPACE, WEBMETHOD_GETUSERNAMEBYEMAIL);
	    
			PropertyInfo ppty_email2 = new PropertyInfo();
			ppty_email2.setName("email");
			ppty_email2.setValue(email);

			requestUsername.addProperty(ppty_email2);

			SoapSerializationEnvelope envelopeUsername = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           envelopeUsername.dotNet=true;
           envelopeUsername.setOutputSoapObject(requestUsername);

           HttpTransportSE androidHttpTransportUsername = new HttpTransportSE(URL);
           try{
	           androidHttpTransportUsername.call(ACTION_GETUSERNAMEBYEMAIL, envelopeUsername );
	
	           Object responseUsername = envelopeUsername.getResponse();
	           return responseUsername.toString();
           }
           catch(Exception e){
        	   return "";
           }
    }
    
	public ArrayList<String> getStatList(String playerID){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETSTATS);
        
        PropertyInfo ppty_playerid = new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(playerID);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETSTATS, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
            int childCount = result.getPropertyCount();

            ArrayList<String> statList = new ArrayList<String>();
            
            int i;
            for (i = 0; i < childCount; i++) {
            	statList.add(result.getProperty(i).toString());
            }
            
            return statList;
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    }
	    return null;
	}

    public ArrayList<String> getAllTheMailFrom(String storage_id){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETALLTHESTOREDITEMS);
        
        PropertyInfo ppty_storageid = new PropertyInfo();
        ppty_storageid.setName("storage_id");
        ppty_storageid.setValue(storage_id);
        request.addProperty(ppty_storageid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETALLTHESTOREDITEMS, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
	        
	        if(result.toString().equals("")){
	        	return null;
	        }
	        else{
	            int childCount = result.getPropertyCount();
	            ArrayList<String> mailItemList = new ArrayList<String>();
	            
	            int i;
	            for (i = 0; i < childCount; i++) {
	            	mailItemList.add(result.getProperty(i).toString());
	            }
	            
	            return mailItemList;
	        }
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    }
	    return null;
    }
    
	public String getMailbox(String player_id){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETMAILBOX);
        
        PropertyInfo ppty_playerid = new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETMAILBOX, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
    }
	
	public String getInventory(String player_id){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETINVENTORY);
        
        PropertyInfo ppty_playerid = new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETINVENTORY, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
    }
		
	public String getMailText(String itemInstance_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETMAILTEXT);
        
        PropertyInfo ppty_itinst_id = new PropertyInfo();
        ppty_itinst_id.setName("itemInstance_id");
        ppty_itinst_id.setValue(itemInstance_id);
        request.addProperty(ppty_itinst_id);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETMAILTEXT, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
		
	public String putItemInStorage(String itemInstance_id, String storage_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_CHANGESTORAGE);
        
        PropertyInfo ppty_itinst_id = new PropertyInfo();
        ppty_itinst_id.setName("itemInstance_id");
        ppty_itinst_id.setValue(itemInstance_id);
        request.addProperty(ppty_itinst_id);
        
        PropertyInfo ppty_inv_id = new PropertyInfo();
        ppty_inv_id.setName("newStorage_id");
        ppty_inv_id.setValue(storage_id);
        request.addProperty(ppty_inv_id);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_CHANGESTORAGE, envelope);
	
	        Object result = envelope.getResponse();
	        
	        if(result.toString().equals("ok"))
	        {
	        	return result.toString();
	        }
	        else
	        	return "error";
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
	
	public String getItemName(String itemInstance_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETITEMNAME);
        
        PropertyInfo ppty_itinst_id = new PropertyInfo();
        ppty_itinst_id.setName("itemInstance_id");
        ppty_itinst_id.setValue(itemInstance_id);
        request.addProperty(ppty_itinst_id);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETITEMNAME, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
	
	public String getPlayerMaxHealth(String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETMAXHEALTH);
		
        PropertyInfo ppty_playerid= new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETMAXHEALTH, envelope);

	        System.out.println("Done");
	        Object result = envelope.getResponse();
	        return result.toString();
        }
        catch(Exception e){
        	System.err.println(":" + e);
        	return ":" + e.toString();
        }
	}
	
	public String getActivePlayer(String username){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETACTIVEPLAYER);
        
        PropertyInfo ppty_username = new PropertyInfo();
        ppty_username.setName("username");
        ppty_username.setValue(username);
        request.addProperty(ppty_username);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETACTIVEPLAYER, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
    }
	
	public String getUsername(String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETPLAYERUSERNAME);
        
        PropertyInfo ppty_playerid = new PropertyInfo();
        ppty_playerid.setName("pl_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETPLAYERUSERNAME, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
	
	public String getPlayerPer(String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETPERCEPTION);
        
		PropertyInfo ppty_playerid= new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETPERCEPTION, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
	
	public void updateLocation(String location, String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_UPDATELOCATION);
		
        PropertyInfo ppty_playerid= new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        PropertyInfo ppty_location = new PropertyInfo();
        ppty_location.setName("location");
        ppty_location.setValue(location);
        request.addProperty(ppty_location);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_UPDATELOCATION, envelope);

	        System.out.println("Done");
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        }
	}
	
	public ArrayList<String> getNearbyHumanPlayers(String coordinates, String baseRadius, String bonus){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETNEARBYHUMANS);
        
        PropertyInfo ppty_coordinates = new PropertyInfo();
        ppty_coordinates.setName("coordinates");
        ppty_coordinates.setValue(coordinates);
        request.addProperty(ppty_coordinates);
        
        PropertyInfo ppty_baseRadius = new PropertyInfo();
        ppty_baseRadius.setName("baseRadius");
        ppty_baseRadius.setValue(baseRadius);
        request.addProperty(ppty_baseRadius);
        
        PropertyInfo ppty_bonus = new PropertyInfo();
        ppty_bonus.setName("bonus");
        ppty_bonus.setValue(bonus);
        request.addProperty(ppty_bonus);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETNEARBYHUMANS, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
            int childCount = result.getPropertyCount();

            ArrayList<String> playerList = new ArrayList<String>();
            
            int i;
            for (i = 0; i < childCount; i++) {
            	playerList.add(result.getProperty(i).toString());
            }
            
            return playerList;
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    }
	    return null;
	}
	
	public ArrayList<String> getNearbyInfectedPlayers(String coordinates, String baseRadius, String bonus)
	{
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETNEARBYINFECTED);
        
        PropertyInfo ppty_coordinates = new PropertyInfo();
        ppty_coordinates.setName("coordinates");
        ppty_coordinates.setValue(coordinates);
        request.addProperty(ppty_coordinates);
        
        PropertyInfo ppty_baseRadius = new PropertyInfo();
        ppty_baseRadius.setName("baseRadius");
        ppty_baseRadius.setValue(baseRadius);
        request.addProperty(ppty_baseRadius);
        
        PropertyInfo ppty_bonus = new PropertyInfo();
        ppty_bonus.setName("bonus");
        ppty_bonus.setValue(bonus);
        request.addProperty(ppty_bonus);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETNEARBYINFECTED, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
            int childCount = result.getPropertyCount();

            ArrayList<String> playerList = new ArrayList<String>();
            
            int i;
            for (i = 0; i < childCount; i++) {
            	playerList.add(result.getProperty(i).toString());
            }
            
            return playerList;
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    }
	    return null;
	}
	
	public String getClassname(String playerid){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETCLASSNAME);
        
        PropertyInfo ppty_playerid = new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(playerid);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETCLASSNAME, envelope);
	
	        Object result = envelope.getResponse();
	        return result.toString();
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    	return e.toString();
	    }
	}
	
	public void goOnline(String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GOONLINE);
		
        PropertyInfo ppty_playerid= new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GOONLINE, envelope);

	        System.out.println("Done");
        }
        catch(Exception e){
        	System.err.println(":" + e);
        }
	}
	
	public void goOffline(String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GOOFFLINE);
		
        PropertyInfo ppty_playerid= new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GOOFFLINE, envelope);

	        System.out.println("Done");
        }
        catch(Exception e){
        	System.err.println(":" + e);
        }
	}
	
	public String getPlayerHealth(String player_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETHEALTH);
		
        PropertyInfo ppty_playerid= new PropertyInfo();
        ppty_playerid.setName("player_id");
        ppty_playerid.setValue(player_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETHEALTH, envelope);

	        System.out.println("Done");
	        Object result = envelope.getResponse();
	        return result.toString();
        }
        catch(Exception e){
        	System.err.println(":" + e);
        	return ":" + e.toString();
        }
	}
	
	public String isInRange(String victim_id, String attacker_id){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_ISINRANGE);
        
        PropertyInfo ppty_victim = new PropertyInfo();
        ppty_victim.setName("prey_id");
        ppty_victim.setValue(victim_id);
        request.addProperty(ppty_victim);
        
        PropertyInfo ppty_attacker = new PropertyInfo();
        ppty_attacker.setName("predator_id");
        ppty_attacker.setValue(attacker_id);
        request.addProperty(ppty_attacker);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_ISINRANGE, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
    }
	
	public String strikeRanged(String victim_user, String attacker_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_STRIKERANGED);		
		
		PropertyInfo ppty_preyid = new PropertyInfo();
		ppty_preyid.setName("prey_id");
		ppty_preyid.setValue(getActivePlayer(victim_user));
        request.addProperty(ppty_preyid);
        
        PropertyInfo ppty_predatorid = new PropertyInfo();
        ppty_predatorid.setName("predator_id");
        ppty_predatorid.setValue(attacker_id);
        request.addProperty(ppty_predatorid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_STRIKERANGED, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
	
	public WebserviceHandler(){
		
	}

	public String getUserId(String username){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETUSERID);
        
        PropertyInfo ppty_username = new PropertyInfo();
        ppty_username.setName("username");
        ppty_username.setValue(username);
        request.addProperty(ppty_username);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETUSERID, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
    }

	public String registerUser(String user, String email, String password, String faction, String playerClass, String position){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_REGISTER);
		
        PropertyInfo ppty_user= new PropertyInfo();
        ppty_user.setName("username");
        ppty_user.setValue(user);
        request.addProperty(ppty_user);
        
        PropertyInfo ppty_email= new PropertyInfo();
        ppty_email.setName("email");
        ppty_email.setValue(email);
        request.addProperty(ppty_email);
        
        PropertyInfo ppty_password= new PropertyInfo();
        ppty_password.setName("password");
        ppty_password.setValue(password);
        request.addProperty(ppty_password);
        
        PropertyInfo ppty_faction= new PropertyInfo();
        ppty_faction.setName("faction");
        ppty_faction.setValue(faction);
        request.addProperty(ppty_faction);
        
        PropertyInfo ppty_class= new PropertyInfo();
        ppty_class.setName("playerClass");
        ppty_class.setValue(playerClass);
        request.addProperty(ppty_class);
        
        PropertyInfo ppty_pos= new PropertyInfo();
        ppty_pos.setName("position");
        ppty_pos.setValue(position);
        request.addProperty(ppty_pos);
        
        
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_REGISTER, envelope);

	        return "true";
        }
        catch(Exception e){
        	System.err.println("RegisterError: " + e);
        	return "";
        }
	}

	public ArrayList<String> getBaseStatList(String class_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETBASESTATS);
        
        PropertyInfo ppty_playerid = new PropertyInfo();
        ppty_playerid.setName("class_id");
        ppty_playerid.setValue(class_id);
        request.addProperty(ppty_playerid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETBASESTATS, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
	        
            int childCount = result.getPropertyCount();
            ArrayList<String> statList = new ArrayList<String>();
            
            int i;
            for (i = 0; i < childCount; i++) {
            	statList.add(result.getProperty(i).toString());
            }
            
            return statList;
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    }
	    return new ArrayList();
	}

	public String getItemCreator(String itemInstance_id){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETITEMCREATOR);
        
        PropertyInfo ppty_itinst_id = new PropertyInfo();
        ppty_itinst_id.setName("itemInstance_id");
        ppty_itinst_id.setValue(itemInstance_id);
        request.addProperty(ppty_itinst_id);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETITEMCREATOR, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}
	
	public String getItemCreatorName(String itemInstance_id){
		String testing = getUsername(getItemCreator(itemInstance_id));
		return testing;
	}
	
	public String sendMail(String sender_id, String recipient_username, String message){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_SENDMAIL);
        
        PropertyInfo ppty_sender_id = new PropertyInfo();
        ppty_sender_id.setName("sender_id");
        ppty_sender_id.setValue(sender_id);
        request.addProperty(ppty_sender_id);
        
        PropertyInfo ppty_recipient_username = new PropertyInfo();
        ppty_recipient_username.setName("recipient_username");
        ppty_recipient_username.setValue(recipient_username);
        request.addProperty(ppty_recipient_username);
        
        PropertyInfo ppty_message = new PropertyInfo();
        ppty_message.setName("message");
        ppty_message.setValue(message);
        request.addProperty(ppty_message);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_SENDMAIL, envelope);
	
	        Object result = envelope.getResponse();

	        return result.toString();
        }
        catch(Exception e){
        	System.err.println("PlayerError: " + e);
        	return e.toString();
        }
	}

	public ArrayList<String> getAllTheNotifications(String username){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETALLTHENOTIFICATIONS);
        
        PropertyInfo ppty_username = new PropertyInfo();
        ppty_username.setName("username");
        ppty_username.setValue(username);
        request.addProperty(ppty_username);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETALLTHENOTIFICATIONS, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
	        
            int childCount = result.getPropertyCount();
            ArrayList<String> notifList = new ArrayList<String>();
            
            int i;
            for (i = 0; i < childCount; i++) {
            	notifList.add(result.getProperty(i).toString());
            }
            
            return notifList;
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    	return null;
	    }
	}

	public void dismissNotification(String username, String message){
		SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_DISMISSNOTIFICATION);
		
        PropertyInfo ppty_username= new PropertyInfo();
        ppty_username.setName("username");
        ppty_username.setValue(username);
        request.addProperty(ppty_username);
        
        PropertyInfo ppty_message = new PropertyInfo();
        ppty_message.setName("message");
        ppty_message.setValue(message);
        request.addProperty(ppty_message);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_DISMISSNOTIFICATION, envelope);

	        System.out.println("Done");
        }
        catch(Exception e){
        	System.err.println(":" + e);
        }
	}

	public void dismissAllTheNotifications(String username){
        SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_CLEARNOTIFICATIONS);
		
        PropertyInfo ppty_username= new PropertyInfo();
        ppty_username.setName("username");
        ppty_username.setValue(username);
        request.addProperty(ppty_username);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_CLEARNOTIFICATIONS, envelope);

	        System.out.println("Done");
        }
        catch(Exception e){
        	System.err.println(":" + e);
        }
	}

	public ArrayList<String> getAllTheBaggedItems(String storage_id){
    	SoapObject request = new SoapObject(NAMESPACE, WEBMETHOD_GETALLTHESTOREDITEMS);
        
        PropertyInfo ppty_storageid = new PropertyInfo();
        ppty_storageid.setName("storage_id");
        ppty_storageid.setValue(storage_id);
        request.addProperty(ppty_storageid);
        
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet=true;
        envelope.setOutputSoapObject(request);
        
        try{
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	        androidHttpTransport.call(ACTION_GETALLTHESTOREDITEMS, envelope);
	
	        SoapObject result = (SoapObject) envelope.getResponse(); 
	        
	        if(result.toString().equals("")){
	        	return null;
	        }
	        else{
	            int childCount = result.getPropertyCount();
	            ArrayList<String> mailItemList = new ArrayList<String>();
	            
	            int i;
	            for (i = 0; i < childCount; i++) {
	            	mailItemList.add(result.getProperty(i).toString());
	            }
	            
	            return mailItemList;
	        }
	    }
	    catch(Exception e){
	    	System.err.println("ClassError: " + e);
	    }
	    return null;
    }
    
	public String getStorageMaxCapacity(String storage_id){
		SoapObject requestUsername = new SoapObject(NAMESPACE, WEBMETHOD_GETSTORAGESIZE);
	    
		PropertyInfo ppty_storageid = new PropertyInfo();
		ppty_storageid.setName("storage_id");
		ppty_storageid.setValue(storage_id);
		requestUsername.addProperty(ppty_storageid);

		SoapSerializationEnvelope envelopeUsername = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelopeUsername.dotNet=true;
       envelopeUsername.setOutputSoapObject(requestUsername);

       HttpTransportSE androidHttpTransportUsername = new HttpTransportSE(URL);
       try{
           androidHttpTransportUsername.call(ACTION_GETSTORAGESIZE, envelopeUsername );

           Object cap = envelopeUsername.getResponse();

           return cap.toString();
           
       }
       catch(Exception e){
    	   return "";
       }
	}
	
	public String deleteItem(String itemInst_id, String storage_id){
		SoapObject requestUsername = new SoapObject(NAMESPACE, WEBMETHOD_DELETEITEM);
	    
		PropertyInfo ppty_itemInst = new PropertyInfo();
		ppty_itemInst.setName("itemInst_id");
		ppty_itemInst.setValue(itemInst_id);
		requestUsername.addProperty(ppty_itemInst);
		
		PropertyInfo ppty_storageId = new PropertyInfo();
		ppty_storageId.setName("storage_id");
		ppty_storageId.setValue(storage_id);
		requestUsername.addProperty(ppty_storageId);

		SoapSerializationEnvelope envelopeUsername = new SoapSerializationEnvelope(SoapEnvelope.VER11);
       envelopeUsername.dotNet=true;
       envelopeUsername.setOutputSoapObject(requestUsername);

       HttpTransportSE androidHttpTransportUsername = new HttpTransportSE(URL);
       try{
           androidHttpTransportUsername.call(ACTION_DELETEITEM, envelopeUsername );

           Object cap = envelopeUsername.getResponse();

           return cap.toString();
           
       }
       catch(Exception e){
    	   return "";
       }
	}
	
}
