package com.leynnnnnn.talky;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
    TextView goToSignUp;
    EditText usernameTextField, passwordTextField;
    Button signInButton;
    DatabaseReference dbRef;
    UserAccount userAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        goToSignUp = findViewById(R.id.goToSignUp);
        signInButton = findViewById(R.id.signInButton);
        goToSignUp.setOnClickListener(v -> startActivity(new Intent(this, SignUp.class)));
        usernameTextField = findViewById(R.id.usernameTextField);
        passwordTextField = findViewById(R.id.passwordTextField);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts");

        signInButton.setOnClickListener(v -> signIn());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void signIn() {
        String username = usernameTextField.getText().toString();
        String password = passwordTextField.getText().toString();
        if(username.isEmpty() || password.isEmpty()) {
            return;
        }
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot info : snapshot.getChildren()) {
                    String accountUsername = info.child("username").getValue(String.class);
                    String accountPassword = info.child("password").getValue(String.class);
                    String accountEmail = info.child("email").getValue(String.class);
                    if(accountUsername != null && accountPassword != null && accountEmail != null) {
                        if(accountUsername.equals(username) && accountPassword.equals(password)) {
                            Toast.makeText(SignIn.this, "Sign in successfully.", Toast.LENGTH_SHORT).show();
                            userAccount = new UserAccount(accountEmail, accountUsername, accountPassword);
                            return;
                        }
                        if(!password.equals(accountPassword) && username.equals(accountUsername)) {
                            Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(SignIn.this, "Invalid Credentials.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}