package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rs.ac.uns.ftn.pma.event_organizer.R;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    TextView textViewUsername;
    TextView textViewPassword;
    boolean loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        databaseReference = firebaseDatabase.getReference("users");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);

        Button registerBtn = findViewById(R.id.register);
        Button loginBtn = findViewById(R.id.login);
        textViewUsername = findViewById(R.id.text_input_username);
        textViewPassword = findViewById(R.id.text_input_password);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                doLogin();
            }
        });


    }

    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private boolean doLogin(){
        if(databaseReference.child("users") == null){
            System.out.println("************NULLLLLLL**************");
        }
        else{
            System.out.println("************      NIJE      **************");

        }
        /*databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                System.out.println("SNAPSHOT: " + snapshot.getValue());
                String username = textViewUsername.getText().toString();
                String password = textViewPassword.getText().toString();
                if (snapshot.hasChild(username)){
                    loginSuccess = true;
                }
                loginSuccess = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });*/

        Toast.makeText(this, "LOGIN: " + loginSuccess, Toast.LENGTH_LONG).show();
        return loginSuccess;
    }
}
