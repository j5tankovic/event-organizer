package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.UUID;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextView textView_username;
    private TextView textView_password;
    private TextView textView_password_repeat;
    private TextView textView_email;
    private TextView textView_firstname;
    private TextView textView_lastname;
    private Button button_upload;
    private ImageView uploadedPicture;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String profilePicturePath;

    private boolean validation;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;

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
        textView_password_repeat = findViewById(R.id.repeat_password);
        textView_email = findViewById(R.id.email);
        textView_firstname = findViewById(R.id.first_name);
        textView_lastname = findViewById(R.id.last_name);
        button_upload = findViewById(R.id.upload_picture);
        uploadedPicture = findViewById(R.id.picture);

        firebaseDatabase = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        databaseReference = firebaseDatabase.getReference("users");

        storageReference = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        validation = true;

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        Query query = databaseReference.orderByChild("username").equalTo(textView_username.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("DATA SNAPSHOT: " + dataSnapshot.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onRegisterClick(View view){
        createAccount(
                textView_username.getText().toString(),
                textView_password.getText().toString(),
                textView_email.getText().toString(),
                textView_firstname.getText().toString(),
                textView_lastname.getText().toString()
        );
    }

    private boolean validateForm() {
        //boolean valid = true;
        validateUsernameEmail();

        // email shouldnt be empty
        String email = textView_email.getText().toString();
        if (TextUtils.isEmpty(email)) {
            textView_email.setError("Required.");
            validation = false;
        } else {
            textView_email.setError(null);
        }

        // password shouldnt be empty
        String password = textView_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            textView_password.setError("Required.");
            validation = false;
        } else {
            textView_password.setError(null);
        }

        // password and repeated password shouldnt be equals
        String reapetPassword = textView_password_repeat.getText().toString();
        if (!TextUtils.equals(reapetPassword,password)) {
            textView_password.setError("Repeated password is not the same");
            textView_password_repeat.setError("Repeated password is not the same");
            validation = false;
        } else {
            textView_password.setError(null);
            textView_password_repeat.setError(null);
        }

        // username shouldnt be empty
        String username = textView_username.getText().toString();
        if (TextUtils.isEmpty(username)) {
            textView_username.setError("Required.");
            validation = false;
        } else {
            textView_username.setError(null);
        }

        System.out.println("VALIDATION RESULT: " + validation);
        return validation;
    }

    private void validateUsernameEmail(){

        Query query = databaseReference.orderByChild("username").equalTo(textView_username.getText().toString());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    System.out.println("USERNAME already exist!");
                    Toast.makeText(RegisterActivity.this, "REGISTRATION: username already exist", Toast.LENGTH_LONG).show();
                    validation = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createAccount(final String username, final String password, final String email, final String firstname, final String lastname) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createUser(username,password,email,firstname,lastname);
                            finish();
                            openUserProfileActivity();
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
        if (profilePicturePath != null){
            newUser.setProfilePicture(profilePicturePath);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadedPicture.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void openUserProfileActivity(){
        Intent intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            profilePicturePath = "images/users/"+ textView_username.getText().toString()+".jpg";
            StorageReference ref = storageReference.child(profilePicturePath);
            ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
        }
    }
}
