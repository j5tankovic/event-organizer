package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.SQLOutput;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.services.AuthentificationService;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView username;
    private TextView email;
    private TextView firstName;
    private TextView lastName;
    private ImageView userPicture;
    private StorageReference storageReference;

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
        userPicture = findViewById(R.id.user_picture_iv);

        Query query = databaseReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User loggedUser = null;
                System.out.println("\n\nDATA SNAPSHOT: " + dataSnapshot.toString());
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    loggedUser = user.getValue(User.class);
                    if(!user.hasChild("profilePicture")){
                        loggedUser.setProfilePicture(null);
                    }
                }
                System.out.println("******* logged user: " + loggedUser.toString());
                System.out.println("******* profile pisture: " + loggedUser.getProfilePicture());
                if (loggedUser.getProfilePicture() != null){
                    storageReference = FirebaseStorage.getInstance().getReference().child(loggedUser.getProfilePicture());

                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();
                            GlideApp.with(getApplicationContext())
                                    .load(imageURL)
                                    .placeholder(R.drawable.user_picture)
                                    .into(userPicture)
                            ;
                            System.out.println("User picture loaded!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            System.out.println("Failed to load a picture!");
                        }
                    });
                }


                username.setText(loggedUser.getUsername());
                email.setText(loggedUser.getEmail());
                firstName.setText(loggedUser.getName());
                lastName.setText(loggedUser.getLastName());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        //AuthentificationService service = new AuthentificationService();
        //User loggedUser = service.getLoggedUser(mAuth.getCurrentUser().getEmail());
    }

    private void showImage(String profilePicture){
        storageReference = FirebaseStorage.getInstance().getReference().child(profilePicture);
        GlideApp.with(this /* context */)
                .load(storageReference)
                .into(userPicture);
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
