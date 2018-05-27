package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.os.storage.StorageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.services.AuthentificationService;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView username;
    private TextView email;
    private TextView firstName;
    private TextView lastName;
    private StorageReference storageReference;
    //StorageReference storageRef = FirebaseStorage.getInstance().reference().child("folderName/file.jpg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        setSupportActionBar((Toolbar) findViewById(R.id.user_profile_toolbar));

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        username = findViewById(R.id.username2);
        email = findViewById(R.id.email2);
        firstName = findViewById(R.id.first_name2);
        lastName = findViewById(R.id.last_name2);


        Query query = databaseReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User loggedUser = null;
                System.out.println("\n\nDATA SNAPSHOT: " + dataSnapshot.toString());
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    loggedUser = user.getValue(User.class);
                }

                storageReference = FirebaseStorage.getInstance().getReference().child(loggedUser.getProfilePicture());
                System.out.println("******* logged user: " + loggedUser.toString());
                username.setText(loggedUser.getUsername());
                email.setText(loggedUser.getEmail());
                firstName.setText(loggedUser.getName());
                lastName.setText(loggedUser.getLastName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AuthentificationService service = new AuthentificationService();
        User loggedUser = service.getLoggedUser(mAuth.getCurrentUser().getEmail());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.base_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_profile_settings:
                return true;
            case R.id.action_profile_edit:
                return true;
            case R.id.action_profile_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout(){
        mAuth.signOut();
        openLoginActivity();
    }

    private void openLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
