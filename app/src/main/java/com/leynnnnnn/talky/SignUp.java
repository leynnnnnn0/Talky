package com.leynnnnnn.talky;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUp extends AppCompatActivity {
    DatabaseReference dbRef;
    EditText email, username, password;
    Button signUpButton;
    TextView goToSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        //Initializing the database reference
        dbRef = FirebaseDatabase.getInstance().getReference().child("Accounts");
        // Initializing the text fields
        email = findViewById(R.id.emailTextField);
        username = findViewById(R.id.usernameTextField);
        password = findViewById(R.id.passwordTextField);
        signUpButton = findViewById(R.id.signUpButton);
        goToSignIn = findViewById(R.id.goToSignIn);

        goToSignIn.setOnClickListener(v -> startActivity(new Intent(this, SignIn.class)));
        // When the user click the button it will run the create an account function
        signUpButton.setOnClickListener(v -> {
            createAnAccount();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    // Function to check if the email input is valid
    private boolean isValidEmail(CharSequence target) {
        return (target == null || !Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    // Function for creating an account
    private void createAnAccount() {
        String emailText  = email.getText().toString();
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        // Checking if the username, email and password is valid
        if(usernameText.length() < 3 || isValidPass(passwordText) || isValidEmail(emailText) || usernameText.isEmpty() || passwordText.isEmpty() || emailText.isEmpty()) {
            if(usernameText.length() < 3) username.setError("Username should be 3 characters or above");
            if(isValidPass(passwordText)) password.setError("Too short.");
            if(isValidEmail(emailText)) email.setError("Invalid email");
            if(usernameText.isEmpty()) username.setError("Input a username");
            if(passwordText.isEmpty()) password.setError("Input a password");
            if(emailText.isEmpty()) email.setError("Input an email.");
            return;
        }

        // Creating a new account on database
        dbRef.push().setValue(new UserAccount(emailText, usernameText, passwordText))
                .addOnSuccessListener(unused ->
                        Toast.makeText(getApplicationContext(), "Account created.", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(unused ->
                        Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_SHORT).show());

    }

    private boolean isValidPass(String password) {
        return password.length() <= 8;
    }

    private boolean isUniqueUsername(String username) {
        final boolean[] isValid = {true};
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Checking if the username is unique
                for(DataSnapshot info : snapshot.getChildren()) {
                    String name = info.child("username").getValue(String.class);
                    if(name != null) {
                        if(name.equals(username)) {
                            isValid[0] = false;
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignUp.this, "Internal Server Error.", Toast.LENGTH_SHORT).show();
            }
        });
        return isValid[0];
    }
}