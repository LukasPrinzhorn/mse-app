package com.example.weihnachtmaerkte;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.weihnachtmaerkte.markets.ListFragment;
import com.example.weihnachtmaerkte.markets.PreviewFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.HashMap;

import static com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES;
import static com.example.weihnachtmaerkte.LoginActivity.USER_ID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu menu;

    private boolean sortingExpanded = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_explore);


        setUsername();

        //loadUser();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setIconResource(R.drawable.ic_search_black_24dp);
        fab.extend();*/

        initSLidingPanel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.setCheckedItem(R.id.nav_explore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setFocusable(true);
        searchView.setQueryHint("Adresse suchen");
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);

        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Bitmap bigCandyCane = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap smallCandyCane = Bitmap.createScaledBitmap(bigCandyCane, 200, 200, false);
        BitmapDescriptor candyCaneIcon = BitmapDescriptorFactory.fromBitmap(smallCandyCane);

        Bitmap bigNav = BitmapFactory.decodeResource(getResources(), R.drawable.navigation);
        Bitmap smallNav = Bitmap.createScaledBitmap(bigNav, 140, 140, false);
        BitmapDescriptor navIcon = BitmapDescriptorFactory.fromBitmap(smallNav);
        LatLng zwidemu = new LatLng(48.204509, 16.360773);
        LatLng mq = new LatLng(48.203322, 16.358644);
        LatLng spittelberg = new LatLng(48.204116, 16.355023);
        LatLng position = new LatLng(48.203475, 16.360252);
        LatLng centerMapPosition = new LatLng(48.20422041318572, 16.358340494334698);
        googleMap.addMarker(new MarkerOptions().position(zwidemu).icon(candyCaneIcon));
        googleMap.addMarker(new MarkerOptions().position(mq).icon(candyCaneIcon));
        googleMap.addMarker(new MarkerOptions().position(spittelberg).icon(candyCaneIcon));
        googleMap.addMarker(new MarkerOptions().position(position).icon(navIcon));
        // Add a marker in Sydney and move the camera
        CameraPosition cameraPosition = new CameraPosition.Builder().target(centerMapPosition).zoom(17).tilt(45f).bearing(-60).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_explore:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_friends:
                Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                drawer.closeDrawer(GravityCompat.START);
                drawer.postDelayed(() -> startActivity(intent), 200);
                break;
        }
        return true;
    }

    private void initSLidingPanel() {
        PreviewFragment previewFragment = new PreviewFragment();
        ListFragment listFragment = new ListFragment();
        FragmentManager manager = getSupportFragmentManager();

        manager.beginTransaction()
                .replace(R.id.fragmentSliding, previewFragment, previewFragment.getTag())
                .commit();

        SlidingUpPanelLayout layout = findViewById(R.id.slider);
        layout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset > 0) {

                    manager.beginTransaction()
                            .replace(R.id.fragmentSliding, listFragment, listFragment.getTag())
                            .commit();
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_sort, menu);
                    menu.getItem(0).setOnMenuItemClickListener(item -> {
                        if (sortingExpanded) {
                            item.setIcon(R.drawable.ic_sort);
                            collapseSorting();
                            sortingExpanded = false;
                        } else {
                            item.setIcon(R.drawable.ic_close);
                            expandSorting();
                            sortingExpanded = true;
                        }
                        return false;
                    });
                } else {
                    manager.beginTransaction()
                            .replace(R.id.fragmentSliding, previewFragment, previewFragment.getTag())
                            .commit();
                    menu.clear();
                    getMenuInflater().inflate(R.menu.menu_main, menu);
                }
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(previousState == SlidingUpPanelLayout.PanelState.EXPANDED && sortingExpanded){
                    sortingExpanded = false;
                    collapseSorting();
                }
            }
        });
    }

    /*private void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        Long id = sharedPreferences.getLong(USER_ID, -1);

    }*/

    private void collapseSorting() {
        HorizontalScrollView horizontalScrollView = findViewById(R.id.sorting_criterias);
        TypedValue tv = new TypedValue();

        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(actionBarHeight, 0).setDuration(200);
        valueAnimator.addUpdateListener(animation -> {
            horizontalScrollView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            horizontalScrollView.requestLayout();
        });

        AnimatorSet set = new AnimatorSet();
        set.play(valueAnimator);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    private void expandSorting() {
        HorizontalScrollView horizontalScrollView = findViewById(R.id.sorting_criterias);
        TypedValue tv = new TypedValue();

        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, actionBarHeight).setDuration(200);
        valueAnimator.addUpdateListener(animation -> {
            horizontalScrollView.getLayoutParams().height = (Integer) animation.getAnimatedValue();
            horizontalScrollView.requestLayout();
        });

        AnimatorSet set = new AnimatorSet();
        set.play(valueAnimator);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }

    private void setUsername(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID, null);
        assert userId != null;

        TextView headerMessage = navigationView.getHeaderView(0).findViewById(R.id.header_username);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users/" + userId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message = "Hallo " + dataSnapshot.child("username").getValue(String.class) + "!";
                headerMessage.setText(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String message = "Hallo";
                headerMessage.setText(message);
            }
        });
    }
}
