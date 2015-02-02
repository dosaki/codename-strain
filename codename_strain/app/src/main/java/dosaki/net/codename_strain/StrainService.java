package dosaki.net.codename_strain;

import java.util.ArrayList;
import org.json.JSONObject;

public class StrainService {
    HttpCommunicator comm;

    StrainService(){
        this.comm = new HttpCommunicator("http://192.168.1.68:8000/");
    }

    public ArrayList<String> getBaseStatList(String className){
        System.out.println("Trying to get...");
        try {
            String rawResponse = this.comm.postData("class/" + className).toString();

            System.out.println("JSONizing...");
            JSONObject response = new JSONObject();

            System.out.println("Got it...");
            JSONObject object = response.getJSONObject("fields");

            System.out.println("------------------------------------");
            System.out.println(object.toString());
            System.out.println("------------------------------------");
            return null;
        }
        catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}
