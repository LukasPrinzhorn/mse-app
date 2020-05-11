package com.example.weihnachtmaerkte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcherKt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weihnachtmaerkte.backend.BackendMock;
import com.example.weihnachtmaerkte.backend.DTOs.DetailedUserDTO;
import com.example.weihnachtmaerkte.backend.DTOs.SimpleUserDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import static com.example.weihnachtmaerkte.LoginActivity.SHARED_PREFERENCES;
import static com.example.weihnachtmaerkte.LoginActivity.USER_ID;

public class FriendsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    DetailedUserDTO user;
    List<SimpleUserDTO> friends;

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

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_friends);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loadFriends();

        FloatingActionButton friendAddButton = findViewById(R.id.friend_add_button);
        friendAddButton.setImageResource(R.drawable.ic_person_add);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
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

            Snackbar.make(recyclerView, deletedUser.getUsername(), Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            friends.add(position, deletedUser);
                            adapter.notifyItemInserted(position);
                        }
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
                    drawer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(intent);
                        }
                    }, 200);
                    break;
            }
            return true;
        }

        private void loadFriends() {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            Long id = sharedPreferences.getLong(USER_ID, -1);
            if (id != -1) {
                BackendMock backendMock = new BackendMock();
                user = backendMock.getUserById(id);
                friends = user.getFriends();
            /*LinearLayout friendLayout = findViewById(R.id.friends_layout);
            for(SimpleUserDTO u: user.getFriends()){
                TextView textView = new TextView(FriendsActivity.this);
                textView.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));
                textView.setText(u.getUsername());
                friendLayout.addView(textView);
            }*/
                List<String> usernames = new ArrayList<>();
                for (SimpleUserDTO u : friends) {
                    usernames.add(u.getUsername());
                }


            /*ListView friendsList = findViewById(R.id.friends_list);
            //ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(FriendsActivity.this, android.R.layout.simple_list_item_1, usernames);
            FriendAdapter friendAdapter = new FriendAdapter(FriendsActivity.this, usernames);
            friendsList.setAdapter(friendAdapter);

            friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    removeFriend(user.getFriends().get(position).getId());
                }
            });*/

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsActivity.this));

                adapter = new FriendAdapter(friends, FriendsActivity.this);
                recyclerView.setAdapter(adapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }

        }

        private void removeFriend(Long id) {
            String message = "Removed friend with id " + id;
            Toast.makeText(FriendsActivity.this, message, Toast.LENGTH_LONG).show();
        }

    /*class FriendAdapter extends ArrayAdapter<String>{

        Context context;
        List<String> usernames = new ArrayList<>();

        FriendAdapter(Context context, List<String> usernames){
            super(context, R.layout.friend_row, R.id.friend_username, usernames);
            this.context = context;
            this.usernames = usernames;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View friendRow = layoutInflater.inflate(R.layout.friend_row, parent, false);
            TextView username = friendRow.findViewById(R.id.friend_username);

            username.setText(usernames.get(position));


            return friendRow;
        }
    }*/


        class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

            private List<SimpleUserDTO> friends = new ArrayList<>();
            private Context context;

            public FriendAdapter(List<SimpleUserDTO> friends, Context context) {
                this.friends = friends;
                this.context = context;
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

            class ViewHolder extends RecyclerView.ViewHolder {

                public TextView friendUsername;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    friendUsername = itemView.findViewById(R.id.friend_username);
                }
            }
        }
    }
