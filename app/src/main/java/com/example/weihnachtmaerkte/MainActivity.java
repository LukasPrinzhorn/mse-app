package com.example.weihnachtmaerkte;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import com.example.weihnachtmaerkte.entities.Market;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import static com.example.weihnachtmaerkte.FriendsActivity.QR_CODE_PREFIX;
import static com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES;
import static com.example.weihnachtmaerkte.LoginActivity.USER_ID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu menu;
    private ArrayList<Market> markets = new ArrayList<>();

    private boolean sortingExpanded = false;

    private boolean firstStartup = false;

    GoogleMap map;
    BitmapDescriptor candyCaneIcon;
    BitmapDescriptor navIcon;
    Marker currentPositionMarker;

    LocationManager locationManager;
    Context mContext;

    FloatingActionButton centerFab;
    boolean movedByProgram = false;


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

        if (getIntent() != null && getIntent().getExtras() != null) {
            firstStartup = getIntent().getExtras().containsKey("firstStartup");
        }

        setUsername();

        //loadUser();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        retrieveDataFromFireBase();

        centerFab = findViewById(R.id.center_fab);
        centerFab.hide();
        centerFab.setImageResource(R.drawable.my_location);
        centerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLocationEnabled();
            }
        });


        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            moveMapToPosition(new LatLng(latitude, longitude));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void isLocationEnabled() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (firstStartup) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            } else {
                moveMapToPosition(new LatLng(48.210033, 16.363449));
            }
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            moveMapToPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10, locationListenerGPS);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        firstStartup = false;
        isLocationEnabled();
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

        /*searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("Info", "Searching...");
                searchCoordinates("Kärtner Straße");
                return true;
            }
        });*/
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCoordinates(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Bitmap bigCandyCane = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap smallCandyCane = Bitmap.createScaledBitmap(bigCandyCane, 200, 200, false);
        candyCaneIcon = BitmapDescriptorFactory.fromBitmap(smallCandyCane);

        Bitmap bigNav = BitmapFactory.decodeResource(getResources(), R.drawable.navigation);
        Bitmap smallNav = Bitmap.createScaledBitmap(bigNav, 140, 140, false);
        navIcon = BitmapDescriptorFactory.fromBitmap(smallNav);
        LatLng zwidemu = new LatLng(48.204509, 16.360773);
        LatLng mq = new LatLng(48.203322, 16.358644);
        LatLng spittelberg = new LatLng(48.204116, 16.355023);
        map.addMarker(new MarkerOptions().position(zwidemu).icon(candyCaneIcon));
        map.addMarker(new MarkerOptions().position(mq).icon(candyCaneIcon));
        map.addMarker(new MarkerOptions().position(spittelberg).icon(candyCaneIcon));

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (!movedByProgram) {
                    displayFab();
                }
            }
        });

        isLocationEnabled();
    }

    private void displayFab() {
        centerFab.show();
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

    private void retrieveDataFromFireBase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Activity activity = this;
        DatabaseReference databaseReference = firebaseDatabase.getReference("markets/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String marketId = ds.getKey().toString();


                    DatabaseReference marketIdRef = databaseReference.child(marketId);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /*TODO check Rating List?*/
                            String marketIdString = dataSnapshot.child("id").getValue().toString();
                            Long marketId = Long.parseLong(marketIdString);
                            String marketName = dataSnapshot.child("name").getValue().toString();
                            String marketAddress = dataSnapshot.child("address").getValue().toString();
                            ArrayList<Long> ratings = new ArrayList<>(Arrays.asList(1L,3L));
                            Market market = new Market(marketId, marketName, marketAddress, null, "null", "null", "null", ratings, "@drawable/zwidemu");

                            markets.add(market);
                            Log.d("Marketgröße",""+markets.size());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    };
                    marketIdRef.addListenerForSingleValueEvent(eventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //TODO find better solutions
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                initSlidingPanel();
            }
        }.start();

    }

    private void initSlidingPanel() {
        Log.d("Marketgröße-sliding",""+markets.size());

        PreviewFragment previewFragment = PreviewFragment.newInstance(markets);
        ListFragment listFragment = ListFragment.newInstance(markets);
        FragmentManager manager = getSupportFragmentManager();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("markets", markets);
        previewFragment.setArguments(bundle);

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
                if (previousState == SlidingUpPanelLayout.PanelState.EXPANDED && sortingExpanded) {
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

    private void setUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID, null);
        assert userId != null;

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(QR_CODE_PREFIX + userId, BarcodeFormat.QR_CODE, 400, 400);
            ImageView imageViewQrCode = navigationView.findViewById(R.id.qr_code);
            imageViewQrCode.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.i("Info", e.getMessage());
        }

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

    private void searchCoordinates(String input) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://eu1.locationiq.com/v1/search.php?key=19d0252ffc7645&q=" + input + " Wien&format=json";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    String newJSON = "{\"value\":" + response + "}";
                    //Log.i("Response", "Response is: " + response.substring(0, 500));
                    try {
                        JSONObject respObject = new JSONObject(newJSON);
                        JSONArray array = respObject.getJSONArray("value");
                        double lon = array.getJSONObject(0).getDouble("lon");
                        double lat = array.getJSONObject(0).getDouble("lat");
                        //Log.i("Lon", String.valueOf(lon));
                        //Log.i("Lat", String.valueOf(lat));

                        moveMapToPosition(new LatLng(lat, lon));

                    } catch (JSONException e) {
                        Log.e("JSON Error", e.getMessage());
                    }
                }, error -> Log.e("Response error", "That didn't work!"));

        queue.add(stringRequest);
    }

    private void moveMapToPosition(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(17).tilt(45f).bearing(-60).build();
        if (currentPositionMarker != null) {
            currentPositionMarker.remove();
        }
        currentPositionMarker = map.addMarker(new MarkerOptions().position(position).icon(navIcon));
        movedByProgram = true;
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                centerFab.hide();
                movedByProgram = false;
            }

            @Override
            public void onCancel() {

            }
        });

    }

}
