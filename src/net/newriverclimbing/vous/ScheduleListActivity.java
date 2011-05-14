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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleListActivity extends Activity {

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
      String url = "/vous_event.php";
      setContentView(R.layout.schedule_table);
      
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
    	
        TableLayout table = (TableLayout) findViewById(R.id.schedule_table);
        LayoutInflater inflater = getLayoutInflater();
        
        try {
          
            // Convert result into JSONArray
            JSONObject json = new JSONObject(result);
            JSONArray resultData = json.getJSONArray("result");
          
            // Loop over JSONarray
            for (int i = 0; i < resultData.length(); i++) {
            
                // Get JSONObject from current array element
                JSONObject dayData = resultData.getJSONObject(i);
                
                TableRow sectionRow = (TableRow)inflater.inflate(R.layout.schedule_section_row, table, false);
                ((TextView) sectionRow.findViewById(R.id.day)).setText(dayData.getString("n"));
                table.addView(sectionRow);
                
                
                JSONArray events = dayData.getJSONArray("e");
                for (int j = 0; j < events.length(); j++) {
                    
                    JSONObject eventData = events.getJSONObject(j);
                    TableRow eventRow = (TableRow)inflater.inflate(R.layout.schedule_event_row, table, false);
                    ((TextView) eventRow.findViewById(R.id.name)).setText(eventData.getString("n"));
                    ((TextView) eventRow.findViewById(R.id.location)).setText(eventData.getString("t") + " @ " + eventData.getString("l"));
                    eventRow.setId(j);
                    eventRow.setTag(eventData.getString("id"));
                    
                    eventRow.setOnClickListener(new OnClickListener() {
                        
                        public void onClick(View v) {
                            Log.i("vous", "click");
                            Log.i("vous", (String) v.getTag());
                            //String str = hashMap.get("areaId"); // id
                            //String areaName = hashMap.get("name");
                      
                            Intent i = new Intent(getApplicationContext(), ScheduleDetailActivity.class);
                            i.putExtra("id", (String) v.getTag());
                            //i.putExtra("name", areaName);
                            startActivity(i);
                        }
                        
                    });
                    table.addView(eventRow);
                    
                }
                
            }
          
        } catch (JSONException e) {
          
            Log.i("vous", e.getMessage());
        	Toast.makeText(mContext, "An error occurred while retrieving schedule data", Toast.LENGTH_SHORT).show();
          
        }
    
      dialog.hide();
      
    }
    
    /*
    public void rowClick()
    {
        Log.i("vous", "click");
    }
    */

}
