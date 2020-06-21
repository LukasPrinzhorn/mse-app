package com.example.weihnachtmaerkte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weihnachtmaerkte.backend.DTOs.SimpleUserDTO;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES;
import static com.example.weihnachtmaerkte.LoginActivity.USER_ID;

public class FriendsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private final List<SimpleUserDTO> friends = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private NavigationView navigationView;

    public static final String QR_CODE_PREFIX = "weihnachtsmarkt:";

    private boolean deleteCancelled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Freunde");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_friends);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        setUsername();
        loadFriends();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        menu.getItem(0).setOnMenuItemClickListener(item -> {
            Intent intent = new Intent(FriendsActivity.this, QRScannerActivity.class);
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setPrompt("QR CODE Scannen");
            integrator.setCameraId(0); // Use a specific camera of the device
            integrator.setOrientationLocked(true);
            integrator.setBeepEnabled(false);
            integrator.setCaptureActivity(QRScannerActivity.class);
            integrator.initiateScan();
            return true;
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                if (result.getContents().startsWith(QR_CODE_PREFIX)) {

                    String friendId = result.getContents().substring(QR_CODE_PREFIX.length());
                    if (!friendId.equals("")) {
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
                        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NotNull DataSnapshot snapshot) {
                                if (!snapshot.hasChild(friendId)) {
                                    Toast.makeText(FriendsActivity.this, "Uns ist dieser User nicht bekannt", Toast.LENGTH_LONG).show();
                                } else {
                                    makeFriendWith(friendId);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {
                        Toast.makeText(this, "Uns ist dieser User nicht bekannt", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "QR Code nicht erkannt", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void makeFriendWith(String friendId) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String userId = sharedPreferences.getString(USER_ID, null);
        if (userId != null && friendId != null && !userId.equals(friendId)) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.getReference("users/" + userId).child("friends").child(friendId).setValue(true);
            firebaseDatabase.getReference("users/" + friendId).child("friends").child(userId).setValue(true);

            if(!sharedPreferences.contains("dialogAccepted")) {
                Bitmap dialogueImage = BitmapFactory.decodeResource(getResources(), R.drawable.dialogue_image);
                dialogueImage = Bitmap.createScaledBitmap(dialogueImage,1440,274, false);

                LayoutInflater inflater = this.getLayoutInflater();
                ImageView imageView = (ImageView) inflater.inflate(R.layout.dialogue_image, null);
                imageView.setImageBitmap(dialogueImage);
                AlertDialog.Builder builder = new AlertDialog.Builder(FriendsActivity.this);
                builder.setTitle("Freunde können durch wischen gelöscht werden")
                        .setPositiveButton("Verstanden", (dialog, id) -> {
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putBoolean("dialogAccepted", true);
                            editor.apply();
                        })
                .setView(imageView);
                // Create the AlertDialog object and return it
                AlertDialog dialog = builder.create();
                dialog.show();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("dialogAccepted", true);
                editor.apply();
            }

        }
    }


    private final ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            SimpleUserDTO deletedUser = friends.get(position);

            friends.remove(position);
            adapter.notifyItemRemoved(position);
            String message = deletedUser.getUsername() + " entfernt";

            Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        deleteCancelled = true;
                        friends.add(position, deletedUser);
                        adapter.notifyItemInserted(position);
                    }).show();

            Handler handler = new Handler();
            handler.postDelayed(() -> removeFriend(deletedUser.getId()), 3500);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(FriendsActivity.this, R.color.colorPrimary))
                    .addActionIcon(R.drawable.ic_person_remove)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

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
            case R.id.nav_friends:
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_explore:
                Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                drawer.closeDrawer(GravityCompat.START);
                drawer.postDelayed(() -> startActivityIfNeeded(intent, 0), 200);
                finish();
                break;
        }
        return true;
    }

    private void loadFriends() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        String id = sharedPreferences.getString(USER_ID, null);
        if (id != null) {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference("users/" + id + "/friends");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    friends.clear();
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String id = snap.getKey();
                        loadFriendUsername(id);
                    }

                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void loadFriendUsername(String id) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users/" + id + "/username");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.getValue(String.class);
                boolean alreadyInList = false;
                assert username != null;
                for (SimpleUserDTO u : friends) {
                    if (u.getId().equals(id)) {
                        u.setUsername(username);
                        alreadyInList = true;
                    }
                }
                if (!alreadyInList) {
                    friends.add(new SimpleUserDTO(id, username));
                }

                adapter = new FriendAdapter(friends);
                recyclerView.setAdapter(adapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void removeFriend(String friendId) {
        /*String message = "Removed friend with id " + id;
        Toast.makeText(FriendsActivity.this, message, Toast.LENGTH_LONG).show();*/
        if (!deleteCancelled) {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            String userId = sharedPreferences.getString(USER_ID, null);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

            DatabaseReference ref = firebaseDatabase.getReference("users/" + friendId + "/friends/" + userId);
            ref.removeValue();

            ref = firebaseDatabase.getReference("users/" + userId + "/friends/" + friendId);
            ref.removeValue();
        }
        deleteCancelled = false;


    }


    static class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

        private final List<SimpleUserDTO> friends;

        FriendAdapter(List<SimpleUserDTO> friends) {
            this.friends = friends;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            String username = friends.get(position).getUsername();

            holder.friendUsername.setText(username);
        }

        @Override
        public int getItemCount() {
            return friends.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            final TextView friendUsername;

            ViewHolder(@NonNull View itemView) {
                super(itemView);

                friendUsername = itemView.findViewById(R.id.friend_username);
            }
        }
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
}
