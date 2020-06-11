package com.example.weihnachtmaerkte;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    public static final String SHARED_PREFERENCES = "shared_preferences";
    public static final String USER_ID = "user_id";

    private EditText editText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        checkRedirect();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editText = findViewById(R.id.username);
        Button button = findViewById(R.id.login);

        button.setOnClickListener(v -> registerUser(editText.getText().toString()));
    }

    private void checkRedirect(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        if(sharedPreferences.contains(USER_ID)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void registerUser(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        String id = databaseReference.push().getKey();
        RegisterUser user = new RegisterUser(username);
        assert id != null;
        databaseReference.child(id).setValue(user);

        editor.putString(USER_ID, id);
        editor.apply();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("firstStartup", true);
        startActivity(intent);
        finish();
    }

    private static class RegisterUser {
        public String username;

        RegisterUser(String username) {
            this.username = username;
        }
    }


}
