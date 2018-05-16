package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private TextView textView_username;
    private TextView textView_password;
    private TextView textView_email;
    private TextView textView_firstname;
    private TextView textView_lastname;

    private FirebaseAuth mAuth;

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

        firebaseDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        databaseReference = firebaseDatabase.getReference("users");

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser loggedUser = mAuth.getCurrentUser();
        //TODO Logout
    }

    public void onRegisterClick(View view){
        Toast.makeText(this, "Clicked on Register Button", Toast.LENGTH_LONG).show();
        createAccount(
                textView_username.getText().toString(),
                textView_password.getText().toString(),
                textView_email.getText().toString(),
                textView_firstname.getText().toString(),
                textView_lastname.getText().toString()
        );
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = textView_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textView_email.setError("Required.");
            valid = false;
        } else {
            textView_email.setError(null);
        }

        String password = textView_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textView_password.setError("Required.");
            valid = false;
        } else {
            textView_password.setError(null);
        }

        return valid;
    }

    private void createAccount(final String username, final String password, final String email, final String firstname, final String lastname) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(username,password,email,firstname,lastname);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void createUser(String username, String password, String email, String firstname, String lastname){

        User newUser = new User(0l, username, password, email, firstname, lastname);

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
