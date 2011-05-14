package net.newriverclimbing.vous;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SponsorListActivity extends Activity {

    /**
     * Progress dialog for loading
     */
    private ProgressDialog dialog;
    
    /**
     * Context
     */
    private Context mContext;
    
    /**
     * On create
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
      super.onCreate(savedInstanceState);
      
      // Grab passed in info
      //Bundle extras = getIntent().getExtras();
      
      // Determine JSON request URL
      String url = "/vous_sponsor.php";
      setContentView(R.layout.sponsor_table);
      
      // Show loading dialog
      dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
      mContext = this;
      
      // async task
      new GetJsonTask().execute(url);
      
    }
    
    /**
     * Asynchronous get JSON task
     */
    private class GetJsonTask extends AsyncTask<String, Void, String> {
        
        /**
         * Execute in background
         */
        protected String doInBackground(String... args) {
            
              VousApi api = new VousApi(mContext);
              return api.getJson(args[0]);

        }
        
        /**
         * After execute (in UI thread context)
         */
        protected void onPostExecute(String result)
        {
            loadSponsors(result);
        }
    }
    
    /**
     * Load areas from JSON string result
     */
    public void loadSponsors(String result) {
    
        Log.i("vous", result);
        
        TableLayout table = (TableLayout) findViewById(R.id.sponsor_table);
        LayoutInflater inflater = getLayoutInflater();
        
        try {
          
            // Convert result into JSONArray
            JSONObject json = new JSONObject(result);
            JSONArray resultData = json.getJSONArray("result");
          
            // Loop over JSONarray
            for (int i = 0; i < resultData.length(); i++) {
            
                // Get JSONObject from current array element
                JSONObject sponsorData = resultData.getJSONObject(i);
                
                TableRow sponsorRow = (TableRow)inflater.inflate(R.layout.sponsor_row, table, false);
                ((TextView) sponsorRow.findViewById(R.id.name)).setText(sponsorData.getString("n"));
                ((TextView) sponsorRow.findViewById(R.id.website)).setText(sponsorData.getString("w"));
                String logoStr = sponsorData.getString("l").replace(".jpg", "");
                if (logoStr.length() > 0) {
                    logoStr = logoStr.replace("100_", "sp_");
                    logoStr = logoStr.replace("150_", "sp_");
                    logoStr = logoStr.replace("-", "_");
                    logoStr = logoStr.replace(".", "_");
                    logoStr = logoStr.toLowerCase();
                    Log.i("vous", logoStr);
                    //logoStr = "sp_dpm_logo";
                    ((ImageView)sponsorRow.findViewById(R.id.logo)).setImageResource(
                            getResources().getIdentifier(logoStr, "drawable", "net.newriverclimbing.vous")
                    );
                }
                
                sponsorRow.setId(i);
                sponsorRow.setTag(sponsorData.getString("id"));
                
                sponsorRow.setOnClickListener(new OnClickListener() {
                    
                    public void onClick(View v) {
                        //Log.i("vous", "click");
                        //Log.i("vous", (String) v.getTag());
                        //String str = hashMap.get("areaId"); // id
                        //String areaName = hashMap.get("name");
                  
                        Intent i = new Intent(getApplicationContext(), SponsorDetailActivity.class);
                        i.putExtra("id", (String) v.getTag());
                        //i.putExtra("name", areaName);
                        startActivity(i);
                    }
                    
                });
                
                table.addView(sponsorRow);
                
            }
          
        } catch (JSONException e) {
          
            Log.i("vous", e.getMessage());
            Toast.makeText(mContext, "An error occurred while retrieving sponsor data", Toast.LENGTH_SHORT).show();
          
        }
    
      dialog.hide();
      
    }

}
