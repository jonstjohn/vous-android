package net.newriverclimbing.vous;

import android.content.Context;

/**
 * Handle ClimbingWeather.com api requests
 */
public class VousApi {
    
    /**
     * Android activity context
     */
    private Context mContext;
    
    /**
     * Base URL
     */
    private String mBaseUrl = "http://www.newriverclimbing.net";
    
    /**
     * Constructor
     * @param context
     */
    public VousApi(Context context)
    {
        mContext = context;
    }
    
    /**
     * Get JSON from API
     * @param url
     * @return
     */
    public String getJson(String url)
    {
        String absoluteUrl = mBaseUrl + url;
        
        HttpToJson toJson = new HttpToJson();
        return toJson.getJsonFromUrl(absoluteUrl);
        
    }
    

}
