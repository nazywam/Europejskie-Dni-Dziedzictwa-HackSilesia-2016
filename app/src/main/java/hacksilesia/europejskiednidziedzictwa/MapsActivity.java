package hacksilesia.europejskiednidziedzictwa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.UserHandle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import android.support.v4.content.ContextCompat;

import hacksilesia.europejskiednidziedzictwa.mapsparser.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapParser parser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parser = new MapParser(this);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

/*
        for(MapPath mp : parser.getUserHandler().mapPaths)
        {
            Log.d("PATHEEE", "path");
            PolylineOptions rectOptions = new PolylineOptions();
            Log.d("PATHEEEs", "size: " + mp.coordinates.size());
            for(MapPoint ml : mp.coordinates)
            {
                rectOptions.add(new LatLng(ml.latitude, ml.longitude));
                Log.d("PATHEEE", "coord " + ml.latitude + " " + ml.longitude);
            }
            mMap.addPolyline(rectOptions);
        }
        */
        for(MapPoint mp : parser.getUserHandler().mapLocations){
            mMap.addMarker(new MarkerOptions().position(new LatLng(mp.longitude, mp.latitude)));
        }
        for(MapPath mp : parser.getUserHandler().mapPaths){
            PolylineOptions o = new PolylineOptions();
            o.width(10).color(0xFF00AAAA);

            for(MapPoint p : mp.coordinates){
                o.add(new LatLng(p.longitude, p.latitude));
            }
            mMap.addPolyline(o);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(50.2644568, 18.9956939)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(17.0f));
    }
}
