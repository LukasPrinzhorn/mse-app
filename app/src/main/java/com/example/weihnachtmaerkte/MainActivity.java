package com.example.weihnachtmaerkte;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.weihnachtmaerkte.entities.Market;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weihnachtmaerkte.entities.Rating;
import com.example.weihnachtmaerkte.markets.previews.ListFragment;
import com.example.weihnachtmaerkte.markets.previews.PreviewFragment;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.weihnachtmaerkte.FriendsActivity.QR_CODE_PREFIX;
import static com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES;
import static com.example.weihnachtmaerkte.LoginActivity.USER_ID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu menu;
    private ArrayList<Market> markets = new ArrayList<>();
    private ArrayList<Rating> ratings = new ArrayList<>();

    private boolean sortingExpanded = false;

    private boolean firstStartup = false;

    private GoogleMap map;
    private BitmapDescriptor candyCaneIcon;
    private BitmapDescriptor navIcon;
    private BitmapDescriptor locationIcon;
    private Marker currentPositionMarker;
    private LatLng currentSearchCoordinates;

    private LocationManager locationManager;
    private Context mContext;

    private FloatingActionButton centerFab;
    boolean movedByProgram = false;
    boolean centeredOnUser;

    private PreviewFragment previewFragment;
    private ListFragment listFragment;
    private SortingCriteria currentSortingCriteria;


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

        initMarkers();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        retrieveDataFromFireBase();

        centerFab = findViewById(R.id.center_fab);
        centerFab.hide();
        centerFab.setImageResource(R.drawable.my_location);
        centerFab.setOnClickListener(v -> {
            centeredOnUser = true;
            currentSearchCoordinates = null;
            isLocationEnabled();
        });


        mContext = this;
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        initFilterButtons();

    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            if (centeredOnUser) {
                moveMapToPosition(new LatLng(latitude, longitude), navIcon);
            } else {
                currentPositionMarker.remove();
                currentPositionMarker = map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(navIcon));
            }
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
            centeredOnUser = false;
            if (firstStartup) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            } else {
                moveMapToPosition(new LatLng(48.210033, 16.363449), locationIcon);
            }
        } else {
            centeredOnUser = true;
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            assert location != null;
            moveMapToPosition(new LatLng(location.getLatitude(), location.getLongitude()), navIcon);
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

    private void initMarkers() {
        Bitmap bigCandyCane = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        Bitmap smallCandyCane = Bitmap.createScaledBitmap(bigCandyCane, 200, 200, false);
        candyCaneIcon = BitmapDescriptorFactory.fromBitmap(smallCandyCane);

        Bitmap bigNav = BitmapFactory.decodeResource(getResources(), R.drawable.navigation);
        Bitmap smallNav = Bitmap.createScaledBitmap(bigNav, 140, 140, false);
        navIcon = BitmapDescriptorFactory.fromBitmap(smallNav);

        Bitmap bigLocation = BitmapFactory.decodeResource(getResources(), R.drawable.position_marker);
        Bitmap smallLocation = Bitmap.createScaledBitmap(bigLocation, 140, 140, false);
        locationIcon = BitmapDescriptorFactory.fromBitmap(smallLocation);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnCameraMoveListener(() -> {
            if (!movedByProgram && !(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                displayFab();
                centeredOnUser = false;
            }
            if (previewFragment != null) {
                previewFragment.reorderMarketsByPosition(map.getCameraPosition().target);
            }
            if (listFragment != null && (currentSortingCriteria == null || currentSortingCriteria == SortingCriteria.DISTANCE)) {
                listFragment.reorderMarketsByPosition(map.getCameraPosition().target);
            }
        });

        map.setOnMarkerClickListener(marker -> {
            moveMapToPosition(marker.getPosition(), null);
            return false;
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
        List<Marker> markers = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("markets/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (Marker m : markers) {
                    m.remove();
                }

                markers.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String marketId = ds.getKey().toString();


                    DatabaseReference marketIdRef = databaseReference.child(marketId);
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            /*TODO check Rating List?*/
                            String marketId = dataSnapshot.child("id").getValue().toString();
                            Log.d("debugValue", marketId);
                            String marketName = dataSnapshot.child("name").getValue().toString();
                            String marketAddress = dataSnapshot.child("address").getValue().toString();
                            String marketDates = dataSnapshot.child("date").getValue().toString();
                            String marketTime = dataSnapshot.child("time").getValue().toString();
                            String weblink = dataSnapshot.child("url").getValue().toString();
                            String image = dataSnapshot.child("image").getValue().toString();
                            double xCoord = Double.parseDouble(dataSnapshot.child("coordinates").child("x").getValue().toString());
                            double yCoord = Double.parseDouble(dataSnapshot.child("coordinates").child("y").getValue().toString());

                            markers.add(map.addMarker(new MarkerOptions().position(new LatLng(xCoord, yCoord)).icon(candyCaneIcon)));

                            ArrayList<String> ratings = new ArrayList<>();
                            for (DataSnapshot postSnapshot : dataSnapshot.child("ratings").getChildren()) {
                                if (postSnapshot.getValue() != null) {
                                    String value = postSnapshot.getValue().toString();
                                    ratings.add(value);
                                }
                            }
                            Market market = new Market(marketId, marketName, marketAddress, new double[]{xCoord, yCoord}, marketDates, marketTime, weblink, ratings, image);
                            markets.add(market);
                        }

                        @Override
                        public void onCancelled(@NotNull DatabaseError databaseError) {
                        }
                    };
                    marketIdRef.addListenerForSingleValueEvent(eventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        DatabaseReference ratingsReference = firebaseDatabase.getReference("ratings/");
        ratingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("value", "" + ds.getValue().toString());
                    Rating rating = ds.getValue(Rating.class);
                    ratings.add(rating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initSlidingPanel();
        //TODO find better solutions
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    private void initSlidingPanel() {
        previewFragment = PreviewFragment.newInstance(markets, ratings);
        listFragment = ListFragment.newInstance(markets, ratings);
        FragmentManager manager = getSupportFragmentManager();

   /*     Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("markets", markets);
        bundle.putParcelableArrayList("ratings", ratings);
        previewFragment.setArguments(bundle);
*/
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
            Log.e("Exception", (e.getMessage() == null) ? "No further information" : e.getMessage());
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

                        currentSearchCoordinates = new LatLng(lat, lon);
                        moveMapToPosition(currentSearchCoordinates, locationIcon);
                        centeredOnUser = false;
                        if (!(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                            displayFab();
                        }

                    } catch (JSONException e) {
                        Log.e("JSON Error", (e.getMessage() == null) ? "No further information" : e.getMessage());
                    }
                }, error -> Log.e("Response error", "That didn't work!"));

        queue.add(stringRequest);
    }

    private void moveMapToPosition(LatLng position, BitmapDescriptor markerIcon) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(17).tilt(45f).bearing(-60).build();
        if (currentPositionMarker != null && markerIcon != null) {
            currentPositionMarker.remove();
        }
        if (markerIcon != null) {
            currentPositionMarker = map.addMarker(new MarkerOptions().position(position).icon(markerIcon));
            centerFab.hide();
        }
        movedByProgram = true;
        if (previewFragment != null) {
            previewFragment.reorderMarketsByPosition(position);
        }
        if (listFragment != null && (currentSortingCriteria == null || currentSortingCriteria == SortingCriteria.DISTANCE)) {
            listFragment.reorderMarketsByPosition(position);
        }
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                movedByProgram = false;
            }

            @Override
            public void onCancel() {

            }
        });

    }

    private LatLng getReferencePosition() {
        return map.getCameraPosition().target;
    }

    private void initFilterButtons() {
        Button foodButton = findViewById(R.id.button_food);
        foodButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByCriteria(SortingCriteria.FOOD);
            currentSortingCriteria = SortingCriteria.FOOD;
        });

        Button drinksButton = findViewById(R.id.button_drinks);
        drinksButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByCriteria(SortingCriteria.DRINKS);
            currentSortingCriteria = SortingCriteria.DRINKS;
        });

        Button familyButton = findViewById(R.id.button_family);
        familyButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByCriteria(SortingCriteria.FAMILY);
            currentSortingCriteria = SortingCriteria.FAMILY;
        });

        Button overallButton = findViewById(R.id.button_overall);
        overallButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByCriteria(SortingCriteria.OVERALL);
            currentSortingCriteria = SortingCriteria.OVERALL;
        });

        Button ambianceButton = findViewById(R.id.button_ambiance);
        ambianceButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByCriteria(SortingCriteria.AMBIANCE);
            currentSortingCriteria = SortingCriteria.AMBIANCE;
        });

        Button crowdingButton = findViewById(R.id.button_crowding);
        crowdingButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByCriteria(SortingCriteria.CROWDING);
            currentSortingCriteria = SortingCriteria.CROWDING;
        });

        Button distanceButton = findViewById(R.id.button_distance);
        distanceButton.setOnClickListener(v -> {
            listFragment.reorderMarketsByPosition(getReferencePosition());
            currentSortingCriteria = SortingCriteria.DISTANCE;
        });
    }

    public enum SortingCriteria {
        OVERALL,
        AMBIANCE,
        FOOD,
        DRINKS,
        CROWDING,
        FAMILY,
        DISTANCE
    }

}
