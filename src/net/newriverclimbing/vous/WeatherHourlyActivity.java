package net.newriverclimbing.vous;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Area/forecast page activity
 */
public class WeatherHourlyActivity extends Activity {
    
    /**
     * Area id
     */
    private String areaId;
    
    /**
     * Name
     */
    private String name;
    
    /**
     * Progress dialog
     */
    private ProgressDialog dialog;
    
    /**
     * Context
     */
    private Context mContext;
    
    /**
     * Temp unit preference setting when activity is started
     */
    private String tempUnit;
    
    /** 
     * On create 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras(); 
        areaId = extras.getString("areaId");
        
        setContentView(R.layout.hourly_table);
        
        mContext = this;
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnit = prefs.getString("tempUnit", "f");
        
        loadContent();
    }
    
    /**
     * Load forecast content
     */
    public void loadContent()
    {
        String url = "/api/area/hourly/" + areaId;
        
        // Show loading dialog
        dialog = ProgressDialog.show(this, "", "Loading. Please wait...", true);
        
        // async task
        new GetJsonTask().execute(url);
    }
    
    /**
     * On pause activity
     */
    public void onPause()
    {
        super.onPause();
        dialog.dismiss();
    }
    
    public void onStop()
    {
        super.onStop();
        dialog.dismiss();
    }
    
    public void onDestroy()
    {
        super.onDestroy();
        dialog.dismiss();
    }
    
    public void onResume()
    {
        super.onResume();
        
        // If tempUnit has changed, refresh
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (!prefs.getString("tempUnit", "f").equals(tempUnit)) {
            refresh();
        }
    }
    
    /**
     * Refresh activity
     */
    private void refresh()
    {
        Intent refreshIntent = new Intent(getApplicationContext(), WeatherActivity.class);
        refreshIntent.putExtra("areaId", areaId);
        refreshIntent.putExtra("name", name);
        refreshIntent.putExtra("tabSelected", 1);
        startActivity(refreshIntent);
        
        this.getParent().finish();
    }
    
//    /**
//     * Create menu options
//     */
//    public boolean onCreateOptionsMenu(Menu menu) {
//        
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.area_menu, menu);
//        MenuItem fav = menu.findItem(R.id.favorite);
//        
//        if (isFavorite()) {
//            
//            fav.setTitle("Remove Favorite");
//            fav.setIcon(R.drawable.btn_star_big_off);
//
//        } else {
//            
//            fav.setTitle("Add Favorite");
//            fav.setIcon(R.drawable.btn_star_big_on);
//
//        }
//        return true;
//    }
//    
//    /**
//     * Handle menu clicks
//     */
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//        case R.id.favorite:
//            if (isFavorite()) {
//                removeFavorite();
//            } else {
//                saveFavorite();
//            }
//            return true;
//        case R.id.refresh:
//            refresh();
//            return true;
//        case R.id.home:
//            Intent homeIntent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(homeIntent);
//            return true;
//        case R.id.settings:
//            Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
//            startActivity(settingsIntent);
//            return true;
//        }
//        return false;
//
//    }
//    
//    /**
//     * Setup menu dynamically
//     */
//    public boolean onPrepareOptionsMenu(Menu menu) {
//
//        MenuItem fav = menu.findItem(R.id.favorite);
//        
//        if (isFavorite()) {
//            fav.setTitle("Remove Favorite");
//            fav.setIcon(R.drawable.btn_star_big_off);
//        } else {
//            fav.setTitle("Add Favorite");
//            fav.setIcon(R.drawable.btn_star_big_on);
//        }
//        return true;
//    }
//    
//    /**
//     * Save favorite area
//     */
//    public void saveFavorite()
//    {
//        FavoriteDbAdapter dbAdapter = new FavoriteDbAdapter(this);
//        dbAdapter.open();
//        dbAdapter.addFavorite(Integer.parseInt(areaId), name);
//        
//    }
//    
//    /**
//     * Remove favorite area
//     */
//    public void removeFavorite() {
//        
//        FavoriteDbAdapter dbAdapter = new FavoriteDbAdapter(this);
//        dbAdapter.removeFavorite(Integer.parseInt(areaId));
//        
//    }
//    
//    /**
//     * Check to see if area is already a favorite
//     */
//    public boolean isFavorite() {
//
//        FavoriteDbAdapter dbAdapter = new FavoriteDbAdapter(this);
//        boolean isFavorite = dbAdapter.isFavorite(Integer.parseInt(areaId));
//        return isFavorite;
//
//    }
    
    /**
     * Asynchronous task to retrieve JSON
     */
    private class GetJsonTask extends AsyncTask<String, Void, String> {
        
        /**
         * Execute in background
         */
        protected String doInBackground(String... args) {
            CwApi api = new CwApi(mContext);
            return api.getJson(args[0]);
        }
        
        /**
         * After execute, handle in UI thread
         */
        protected void onPostExecute(String result) {
            
            loadForecast(result);
            
        }
    }
    
    /**
     * Load forecast from JSON
     */
    private void loadForecast(String result) {

        TableLayout table = (TableLayout) findViewById(R.id.forecast_table);
        
        LayoutInflater inflater = getLayoutInflater();
        
        try {
            
            JSONObject jsonObj = new JSONObject(result);
            
            name = jsonObj.getString("n");
            //((TextView)findViewById(R.id.areaName)).setText(name);
            JSONArray forecastData = jsonObj.getJSONArray("f");
            
            String lastDay = "";
            String day = "";
            String displayDay = "";
            
            // Loop over forecast data
            for (int i = 0; i < forecastData.length(); i++)
            {
                JSONObject dayData = forecastData.getJSONObject(i);
                
                // Set values of table row
                TableRow row = (TableRow)inflater.inflate(R.layout.hourly_row, table, false);
                row.setId(i);
                
                day = dayData.getString("dy");
                
                if (day.equals(lastDay)) {
                    displayDay = day;
                    ((TextView)row.findViewById(R.id.day)).setVisibility(android.view.View.GONE);
                } else {
                    displayDay = day;
                }
                ((TextView)row.findViewById(R.id.day)).setText(displayDay);
                lastDay = day;
                ((TextView)row.findViewById(R.id.temp)).setText(dayData.getString("t") + "\u00b0");
                ((TextView)row.findViewById(R.id.sky)).setText(dayData.getString("sk") + "% cloudy");
                ((TextView)row.findViewById(R.id.time)).setText(dayData.getString("ti"));
                ((TextView)row.findViewById(R.id.humidity)).setText(dayData.getString("h") + "%");
                ((TextView)row.findViewById(R.id.wind)).setText(dayData.getString("ws") + " mph");
                ((TextView)row.findViewById(R.id.precip)).setText(dayData.getString("p") + "%");
                String symbol = dayData.getString("sy").replace(".png", "");
                ((ImageView)row.findViewById(R.id.symbol)).setImageResource(
                        getResources().getIdentifier(symbol, "drawable", "net.newriverclimbing.vous")
                );
                
                if (i % 2 == 1) {
                    row.setBackgroundResource(R.color.silver);
                }
                
                table.addView(row);
                
                String conditions = dayData.getString("c");
                
                if (conditions.length() > 0) {
                
                    TableRow conditionsRow = (TableRow)inflater.inflate(R.layout.conditions_row, table, false);
                    ((TextView)conditionsRow.findViewById(R.id.weather)).setText(conditions);
                
                    if (i % 2 == 1) {
                        conditionsRow.setBackgroundResource(R.color.silver);
                    }
                    
                    table.addView(conditionsRow);
                    
                }
                
            }
            
        } catch (JSONException e) {
            
        	Toast.makeText(mContext, "An error occurred while retrieving forecast data", Toast.LENGTH_SHORT).show();
            
        }
        
        dialog.hide();
        
    }
    
}
