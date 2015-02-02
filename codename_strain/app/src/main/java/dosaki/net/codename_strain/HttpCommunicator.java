package dosaki.net.codename_strain;

import java.util.*;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;


public class HttpCommunicator{
    String serverBaseUrl = "";

    HttpCommunicator(String baseUrl){
        this.serverBaseUrl = baseUrl;
    }

    public HttpResponse postData(String relativeLink) {
        return async.execute(this.serverBaseUrl + relativeLink);
    }

    public HttpResponse postData(String relativeLink, String message) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.serverBaseUrl + relativeLink);

        try {
            ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
            data.add(new BasicNameValuePair("data", message));
            httppost.setEntity(new UrlEncodedFormEntity(data));

            // Execute HTTP Post Request
            return httpclient.execute(httppost);
        }
        catch (ClientProtocolException e) {
            System.out.println(e);
            return null;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public HttpResponse postData(String relativeLink, ArrayList<NameValuePair> data) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(this.serverBaseUrl + relativeLink);

        try {
            httppost.setEntity(new UrlEncodedFormEntity(data));

            // Execute HTTP Post Request
            return httpclient.execute(httppost);
        }
        catch (ClientProtocolException e) {
            System.out.println(e);
            return null;
        }
        catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static class async extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String[] params) {
            String url = params[0];
            String message = params[1];
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]); //Url

            System.out.println("About to try to send stuff...");
            try {
                ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("data", message));
                httppost.setEntity(new UrlEncodedFormEntity(data));

                // Execute HTTP Post Request
                System.out.println("Executing");
                return httpclient.execute(httppost);
            }
            catch (ClientProtocolException e) {
                System.out.println(e);
                return null;
            }
            catch (IOException e) {
                System.out.println(e);
                return null;
            }
        }
    }
}

