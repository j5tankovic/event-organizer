package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import rs.ac.uns.ftn.pma.event_organizer.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    TextView textViewUsername;
    TextView textViewPassword;
    Button registerBtn;
    Button loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);

        registerBtn = findViewById(R.id.register);
        loginBtn = findViewById(R.id.login);
        textViewUsername = findViewById(R.id.text_input_username);
        textViewPassword = findViewById(R.id.text_input_password);

        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser loggedUser = mAuth.getCurrentUser();
        System.out.println("LOGGED USER: ");
        System.out.println(loggedUser.toString());
        openUserProfileActivity();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                signIn(textViewUsername.getText().toString(), textViewPassword.getText().toString());

            }
        });
    }

    private void openRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openUserProfileActivity(){
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        openUserProfileActivity();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = textViewUsername.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textViewUsername.setError("Required.");
            valid = false;
        } else {
            textViewUsername.setError(null);
        }

        String password = textViewPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textViewPassword.setError("Required.");
            valid = false;
        } else {
            textViewPassword.setError(null);
        }

        return valid;
    }


/*    private void authanticate(){
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
    }*/

}
