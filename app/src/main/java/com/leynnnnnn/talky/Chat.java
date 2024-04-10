package com.leynnnnnn.talky;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Chat extends AppCompatActivity {
    ImageView chatProfilePicture;
    TextView chatUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        chatUsername = findViewById(R.id.chatUsername);
        chatProfilePicture = findViewById(R.id.chatProfilePicture);

        UserAccount userAccount = getIntent().getParcelableExtra("accountInfo");
        assert userAccount != null;
        chatUsername.setText(userAccount.getUsername());
        chatProfilePicture.setImageResource(userAccount.getProfilePicture());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}