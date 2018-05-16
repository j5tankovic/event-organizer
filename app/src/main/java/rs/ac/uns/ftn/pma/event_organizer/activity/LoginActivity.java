package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    TextView textViewUsername;
    TextView textViewPassword;
    User authenticatedUser = null;
    boolean authenticated = false;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);

        Button registerBtn = findViewById(R.id.register);
        Button loginBtn = findViewById(R.id.login);
        textViewUsername = findViewById(R.id.text_input_username);
        textViewPassword = findViewById(R.id.text_input_password);

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser loggedUser = mAuth.getCurrentUser();
        //updateUI(currentUser);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                authanticate();

            }
        });
    }

    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void authanticate(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        authenticatedUser = null;
        authenticated = false;

        Query query = databaseReference.child("users").orderByChild("username").equalTo(textViewUsername.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "user" node with all children with id 0
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        if(user.child("password").getValue().equals(textViewPassword.getText().toString())){
                            User usersBean = user.getValue(User.class);
                            System.out.println("Authenticated User: " + usersBean.toString());
                            authenticatedUser = usersBean;
                        }
                    }
                }
                boolean loginSuccess = authenticatedUser != null;
                Toast.makeText(LoginActivity.this, "LOGIN: " + loginSuccess, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setLoggedUser(User loggedUser){

    }
}
