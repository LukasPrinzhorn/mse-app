package com.example.weihnachtmaerkte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weihnachtmaerkte.backend.BackendMock;
import com.example.weihnachtmaerkte.backend.DTOs.DetailedUserDTO;
import com.example.weihnachtmaerkte.backend.DTOs.SimpleUserDTO;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES;
import static com.example.weihnachtmaerkte.LoginActivity.USER_ID;

public class FriendsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;

    private List<SimpleUserDTO> friends;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_friends);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loadFriends();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
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
            String message = deletedUser.getUsername() + "entfernt";

            Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        friends.add(position, deletedUser);
                        adapter.notifyItemInserted(position);
                    }).show();
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
                drawer.closeDrawer(GravityCompat.START);
                drawer.postDelayed(() -> startActivity(intent), 200);
                break;
        }
        return true;
    }

    private void loadFriends() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        long id = sharedPreferences.getLong(USER_ID, -1);
        if (id != -1) {
            BackendMock backendMock = new BackendMock();
            DetailedUserDTO user = backendMock.getUserById(id);
            friends = user.getFriends();

            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));

            adapter = new FriendAdapter(friends);
            recyclerView.setAdapter(adapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

    }

    private void removeFriend(Long id) {
        String message = "Removed friend with id " + id;
        Toast.makeText(FriendsActivity.this, message, Toast.LENGTH_LONG).show();
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
}
