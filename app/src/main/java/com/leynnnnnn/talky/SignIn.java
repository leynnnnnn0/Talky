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
        // Sign in function will be executed when sign in button is clicked
        signInButton.setOnClickListener(v -> signIn());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    // Sign in function
    public void signIn() {
        String username = usernameTextField.getText().toString();
        String password = passwordTextField.getText().toString();
        // Checking if the username or the password field is empty and if it is empty i won't continue to run the rest of the code
        if(username.isEmpty() || password.isEmpty()) {
            return;
        }
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Looping through all the children
                for(DataSnapshot info : snapshot.getChildren()) {
                    String accountUsername = info.child("username").getValue(String.class);
                    String accountPassword = info.child("password").getValue(String.class);
                    String accountEmail = info.child("email").getValue(String.class);
                    // Checking if the strings is not null
                    if(accountUsername != null && accountPassword != null && accountEmail != null) {
                        // Checking if the username and password matched
                        if(accountUsername.equals(username) && accountPassword.equals(password)) {
                            Toast.makeText(SignIn.this, "Sign in successfully.", Toast.LENGTH_SHORT).show();
                            // Creating an object of user
                            userAccount = new UserAccount(accountEmail, accountUsername, accountPassword);
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.putExtra("userInfo", userAccount);
                            startActivity(intent);
                            return;
                        }
                        // If password doesn't match on the username account
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