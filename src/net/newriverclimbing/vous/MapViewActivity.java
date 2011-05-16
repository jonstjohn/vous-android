package net.newriverclimbing.vous;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

public class MapViewActivity extends TabActivity {
    
    /**
     * On create
     */
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab
        
        //Bundle extras = getIntent().getExtras();
        //String areaId = extras.getString("areaId");

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, MapDetailActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        LayoutInflater inflater = getLayoutInflater();
        LinearLayout dailyTab = (LinearLayout)inflater.inflate(R.layout.tab_content, tabHost, false);
        TextView dailyText = (TextView)dailyTab.findViewById(R.id.name);
        dailyText.setText("Detail");
        dailyText.setSelected(true);
        spec = tabHost.newTabSpec("detail").setIndicator(dailyTab).setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, MapOverviewActivity.class);
        LinearLayout hourlyTab = (LinearLayout)inflater.inflate(R.layout.tab_content, tabHost, false);
        TextView hourlyText = (TextView)hourlyTab.findViewById(R.id.name);
        hourlyText.setText("Overview");
        spec = tabHost.newTabSpec("overview").setIndicator(hourlyTab).setContent(intent);
        tabHost.addTab(spec);

        //TextView areaView = (TextView)inflater.inflate(R.layout.tab_content, tabHost, false);
        TextView areaView = (TextView)tabHost.findViewById(R.id.areaName);
        areaView.setText("New River Gorge");
        
        //int tabSelected = extras.getInt("tabSelected");
        //tabHost.setCurrentTab(tabSelected);
    }

}
