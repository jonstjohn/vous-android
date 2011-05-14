package net.newriverclimbing.vous;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class SponsorDetailActivity extends Activity {
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
      Bundle extras = getIntent().getExtras();
      String eventId = extras.getString("id");
      
      // Determine JSON request URL
      String url = "/vous_sponsor.php?id=" + eventId;
      setContentView(R.layout.sponsor);
      
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
            loadSchedule(result);
        }
    }
    
    /**
     * Load areas from JSON string result
     */
    public void loadSchedule(String result) {
    
        Log.i("vous", result);
        
        //TableLayout table = (TableLayout) findViewById(R.id.event);
        TextView nameView = (TextView) findViewById(R.id.name);
        TextView locationView = (TextView) findViewById(R.id.location);
        TextView descriptionView = (TextView) findViewById(R.id.description);
        
        try {
          
            // Convert result into JSONArray
            JSONObject json = new JSONObject(result);
            JSONObject resultData = json.getJSONObject("result");
            
            nameView.setText(resultData.getString("n"));
            locationView.setText(resultData.getString("t") + " @ " + resultData.getString("l"));
            descriptionView.setText(resultData.getString("d").replace("\r\n", "\n"));
          
        } catch (JSONException e) {
          
            Log.i("vous", e.getMessage());
            Toast.makeText(mContext, "An error occurred while retrieving event data", Toast.LENGTH_SHORT).show();
          
        }
    
      dialog.hide();
      
    }

}
