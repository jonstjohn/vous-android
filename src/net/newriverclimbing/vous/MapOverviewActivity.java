package net.newriverclimbing.vous;


import java.util.List;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapOverviewActivity extends MapActivity {
    
    private MapController mapController;
    private MapView mapView;
    private GeoPoint areaPoint;
    //private LocationManager locationManager;
    
    /**
     * Name
     */
    private String name;
    
    /**
     * Progress dialog
     */
    private ProgressDialog dialog;

    public void onCreate(Bundle bundle) {
        
        super.onCreate(bundle);
        
        // create a map view
        setContentView(R.layout.map_overview); // bind the layout to the activity
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        //mapView.setStreetView(true);
        mapController = mapView.getController();
        mapController.setZoom(12); // Zoom 1 is world view
        
        areaPoint = new GeoPoint(38073332, -81076097);
        mapController.setCenter(areaPoint);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.climbing);
        AreaMapItemizedOverlay itemizedoverlay = new AreaMapItemizedOverlay(drawable, this);
        
        OverlayItem marker = new OverlayItem(areaPoint, name, "Burnwood");
        marker.getTitle();
        itemizedoverlay.addOverlay(marker);
        mapOverlays.add(itemizedoverlay);

    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}

