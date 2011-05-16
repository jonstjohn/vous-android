package net.newriverclimbing.vous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpToJson {
	
    public String getJsonFromUrl(String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        
        HttpGet httpget = new HttpGet(url);
        
        HttpResponse response;
        
        String result = "";
        try {
        
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            
            InputStream instream = entity.getContent();
            result = convertStreamToString(instream);
            instream.close();
            
        } catch (Exception e) {
        	Log.i("vous", e.getMessage());
        	result = "{}";
        	
        }
        
        return result;
        
    }
    
    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            return "";
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	return "";
            }
        }
        return sb.toString();
    }

}
