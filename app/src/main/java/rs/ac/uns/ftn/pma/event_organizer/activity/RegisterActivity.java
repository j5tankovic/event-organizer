package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private TextView textView_username;
    private TextView textView_password;
    private TextView textView_email;
    private TextView textView_firstname;
    private TextView textView_lastname;

    private Button button_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        textView_username = findViewById(R.id.username);
        textView_password = findViewById(R.id.text_input_password);
        textView_email = findViewById(R.id.email);
        textView_firstname = findViewById(R.id.first_name);
        textView_lastname = findViewById(R.id.last_name);

        button_register = findViewById(R.id.btn_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        databaseReference = firebaseDatabase.getReference("users");
    }

    public void onRegisterClick(View view){
        Toast.makeText(this, "Clicked on Button", Toast.LENGTH_LONG).show();
        createUser();
    }

    private void createUser(){

        //String userId = databaseReference.push().getKey();
        //System.out.println("\n\nUSER ID: " + userId);
        User newUser = new User(
                0l,
                textView_username.getText().toString(),
                textView_password.getText().toString(),
                textView_email.getText().toString(),
                textView_firstname.getText().toString(),
                textView_lastname.getText().toString()
        );

        System.out.println("USER : " + newUser.toString());
        databaseReference.child(textView_username.getText().toString()).setValue(newUser);
        databaseReference.child(textView_username.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                System.out.println("USERNAME: " + user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
