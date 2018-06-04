package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;
import rs.ac.uns.ftn.pma.event_organizer.services.GlideApp;

public class UpdateUserProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private TextView username;
    private TextView email;
    private TextView firstName;
    private TextView lastName;
    private ImageView userPicture;
    private Button saveBtn;
    private Button changeImageBtn;
    private TextView password;
    private TextView repeatPassword;
    private StorageReference storageReference;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String profilePicturePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        //setSupportActionBar((Toolbar) findViewById(R.id.user_profile_toolbar));
        Toolbar t = findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        t.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        username = findViewById(R.id.username2);
        email = findViewById(R.id.email2);
        firstName = findViewById(R.id.first_name2);
        lastName = findViewById(R.id.last_name2);
        userPicture = findViewById(R.id.user_picture_iv);
        saveBtn = findViewById(R.id.save_btn);
        changeImageBtn = findViewById(R.id.change_image_btn);
        password = findViewById(R.id.password2);
        repeatPassword = findViewById(R.id.repeat_password2);

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
                    storageReference = FirebaseStorage.getInstance().getReference();

                    storageReference.child(loggedUser.getProfilePicture()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                password.setText(loggedUser.getPassword());
                repeatPassword.setText(loggedUser.getPassword());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(username.getText().toString()).child("lastName").setValue(lastName.getText().toString());
                databaseReference.child(username.getText().toString()).child("name").setValue(firstName.getText().toString());
                if (password.getText().toString().equals(repeatPassword.getText().toString())){
                    databaseReference.child(password.getText().toString()).child("password").setValue(password.getText().toString());
                }
                else{
                    password.setError("Repeated password is not the same");
                    repeatPassword.setError("Repeated password is not the same");
                }
            }
        });

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userPicture.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            profilePicturePath = "images/users/" + username.getText().toString() + ".jpg";
            StorageReference ref = storageReference.child(profilePicturePath);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateUserProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(UpdateUserProfileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
