package net.newriverclimbing.vous;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Home extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView scheduleText = (TextView) findViewById(R.id.schedule_text);
        scheduleText.setOnClickListener(scheduleListener);
        
        ImageView scheduleImage = (ImageView) findViewById(R.id.schedule_image);
        scheduleImage.setOnClickListener(scheduleListener);
        
        
        TextView sponsorText = (TextView) findViewById(R.id.sponsor_text);
        sponsorText.setOnClickListener(sponsorListener);
        
        ImageView sponsorImage = (ImageView) findViewById(R.id.sponsor_image);
        sponsorImage.setOnClickListener(sponsorListener);
        
        TextView weatherText = (TextView) findViewById(R.id.weather_text);
        weatherText.setOnClickListener(weatherListener);
        
        ImageView weatherImage = (ImageView) findViewById(R.id.weather_image);
        weatherImage.setOnClickListener(weatherListener);
        
        TextView mapText = (TextView) findViewById(R.id.map_text);
        mapText.setOnClickListener(mapListener);
        
        ImageView mapImage = (ImageView) findViewById(R.id.map_image);
        mapImage.setOnClickListener(mapListener);
        
        TextView registrationText = (TextView) findViewById(R.id.registration_text);
        registrationText.setOnClickListener(registrationListener);
        
        ImageView registrationImage = (ImageView) findViewById(R.id.registration_image);
        registrationImage.setOnClickListener(registrationListener);
        
        TextView supportText = (TextView) findViewById(R.id.support_text);
        supportText.setOnClickListener(supportListener);
        
        ImageView supportImage = (ImageView) findViewById(R.id.support_image);
        supportImage.setOnClickListener(supportListener);
    }
    
    /**
     * On click listener for schedule button
     */
    private OnClickListener scheduleListener = new OnClickListener() {
        
        public void onClick(View v) {
            
            Intent i = new Intent(getApplicationContext(), ScheduleListActivity.class);
            startActivity(i);
        }
        
    };
    
    /**
     * On click listener for sponsor button
     */
    private OnClickListener sponsorListener = new OnClickListener() {
        
        public void onClick(View v) {
            
            Intent i = new Intent(getApplicationContext(), SponsorListActivity.class);
            startActivity(i);
        }
        
    };
    
    /**
     * On click listener for weather button
     */
    private OnClickListener weatherListener = new OnClickListener() {
        
        public void onClick(View v) {
            
            Intent i = new Intent(getApplicationContext(), WeatherActivity.class);
            i.putExtra("areaId", "3");
            i.putExtra("name", "New River Gorge");
            startActivity(i);
        }
        
    };
    
    /**
     * On click listener for map button
     */
    private OnClickListener mapListener = new OnClickListener() {
        
        public void onClick(View v) {
            
            Intent i = new Intent(getApplicationContext(), MapViewActivity.class);
            i.putExtra("areaId", "3");
            i.putExtra("name", "New River Gorge");
            startActivity(i);
        }
        
    };
    
    /**
     * On click listener for registration button
     */
    private OnClickListener registrationListener = new OnClickListener() {
        
        public void onClick(View v) {
            
            Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(i);
        }
        
    };
    
    /**
     * On click listener for support button
     */
    private OnClickListener supportListener = new OnClickListener() {
        
        public void onClick(View v) {
            
            Intent i = new Intent(getApplicationContext(), SupportActivity.class);
            startActivity(i);
        }
        
    };
}