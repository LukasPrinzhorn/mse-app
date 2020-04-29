package com.example.weihnachtmaerkte;

import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_menu_black_24dp);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_search_black_24dp);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bitmap bigCandyCane = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap smallCandyCane = Bitmap.createScaledBitmap(bigCandyCane, 200, 200, false);
        BitmapDescriptor candyCaneIcon = BitmapDescriptorFactory.fromBitmap(smallCandyCane);

        Bitmap bigNav = BitmapFactory.decodeResource(getResources(), R.drawable.navigation);
        Bitmap smallNav = Bitmap.createScaledBitmap(bigNav, 140, 140, false);
        BitmapDescriptor navIcon = BitmapDescriptorFactory.fromBitmap(smallNav);
        LatLng zwidemu = new LatLng(48.204509,16.360773);
        LatLng mq = new LatLng(48.203322,16.358644);
        LatLng spittelberg = new LatLng(48.204116,16.355023);
        LatLng position = new LatLng(48.203475,16.360252);
        mMap.addMarker(new MarkerOptions().position(zwidemu).icon(candyCaneIcon));
        mMap.addMarker(new MarkerOptions().position(mq).icon(candyCaneIcon));
        mMap.addMarker(new MarkerOptions().position(spittelberg).icon(candyCaneIcon));
        mMap.addMarker(new MarkerOptions().position(position).icon(navIcon));
        // Add a marker in Sydney and move the camera
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(17).tilt(45f).bearing(-60).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
}
