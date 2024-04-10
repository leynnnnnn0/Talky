package com.leynnnnnn.talky;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchArea extends AppCompatActivity implements FavoriteInterface {
    EditText searchBar;
    DatabaseReference dbRef;
    RecyclerView resultsRecyclerView;
    SearchResultAdapter adapter;
    ArrayList<UserAccount> usersAccounts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_area);

        resultsRecyclerView = findViewById(R.id.resultsRecyclerView);
        adapter = new SearchResultAdapter(this, usersAccounts, this);
        searchBar = findViewById(R.id.searchBar);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts");

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = searchBar.getText().toString();
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usersAccounts.clear();
                        for(DataSnapshot info : snapshot.getChildren()) {
                            String accountUsername = info.child("username").getValue(String.class);
                            String accountEmail = info.child("email").getValue(String.class);
                            String accountPassword = info.child("password").getValue(String.class);
                            if(accountUsername != null && accountUsername.equalsIgnoreCase(query)) {
                                usersAccounts.add(new UserAccount(accountEmail, accountUsername, accountPassword));
                            }
                        }
                        resultsRecyclerView.setAdapter(adapter);
                        resultsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        resultsRecyclerView.setHasFixedSize(true);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onProfileClick(String email, String username, String password, int profilePicture) {
        Intent intent = new Intent(this, Chat.class);
        intent.putExtra("accountInfo", new UserAccount(email, username, password, profilePicture));
        startActivity(intent);
    }
}