package rs.ac.uns.ftn.pma.event_organizer.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.ac.uns.ftn.pma.event_organizer.R;
import rs.ac.uns.ftn.pma.event_organizer.model.Event;
import rs.ac.uns.ftn.pma.event_organizer.model.EventCategory;
import rs.ac.uns.ftn.pma.event_organizer.model.User;

public class AddNewEventActivity extends AppCompatActivity {

    public static final String ADDED_EVENT = "rs.ac.uns.ftn.pma.event_organizer.ADDED_EVENT";

    private TextView txtName;
    private TextView txtDescription;
    private TextView txtStartDate;
    private TextView txtEndDate;
    private TextView txtBudget;
    private Spinner eventCategory;

    private Button upload;
    private ImageView uploadedPicture;

    private Button add;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private String eventPicturePath;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference;
    private DatabaseReference databaseReferenceUsers;
    private FirebaseAuth mAuth;

    Event event = new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_event);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("events");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        txtName = findViewById(R.id.new_event_name);
        txtDescription = findViewById(R.id.new_event_description);
        txtStartDate = findViewById(R.id.new_event_start_date);
        txtEndDate = findViewById(R.id.new_event_end_date);
        txtBudget = findViewById(R.id.new_event_budget);
        eventCategory = findViewById(R.id.new_event_category);


        upload = findViewById(R.id.new_event_upload_image);
        uploadedPicture = findViewById(R.id.new_event_image);

        Spinner categories = findViewById(R.id.new_event_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.event_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);

        Query query = databaseReferenceUsers.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User loggedUser = null;
                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    loggedUser = user.getValue(User.class);
                    event.setCreator(loggedUser);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add = findViewById(R.id.add_new_event);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateForm()) {
                    return;
                }
                event = formEvent();
                save(event);
                formResult(event);

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = txtName.getText().toString();
        if(TextUtils.isEmpty(name)) {
            txtName.setError("Required");
            valid = false;
        }
        String description = txtDescription.getText().toString();
        if(TextUtils.isEmpty(description)) {
            txtDescription.setError("Required");
            valid = false;
        }
        String start_date_string = txtStartDate.getText().toString();
        if(TextUtils.isEmpty(start_date_string)) {
            txtStartDate.setError("Required");
            valid = false;
        }
        String end_date_string = txtEndDate.getText().toString();
        if(TextUtils.isEmpty(end_date_string)) {
            txtEndDate.setError("Required");
            valid = false;
        }
        String budget = txtBudget.getText().toString();
        if(TextUtils.isEmpty(budget)) {
            txtBudget.setError("Required");
            valid = false;
        } else {
            if(!TextUtils.isDigitsOnly(budget)) {
                txtBudget.setError("Only digits allowed");
                valid = false;
            }
        }

        return valid;
    }

    private Event formEvent() {
        event.setName(txtName.getText().toString());
        event.setDescription(txtDescription.getText().toString());
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        String start_date_string = txtStartDate.getText().toString();
        Date start_date_date = null;
        String end_date_string = txtEndDate.getText().toString();
        Date end_date_date = null;
        try {
            start_date_date = formatter.parse(start_date_string);
            end_date_date = formatter.parse(end_date_string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        event.setStartDateTime(start_date_date);
        event.setEndDateTime(end_date_date);
        event.setBudget(Long.valueOf(txtBudget.getText().toString()));
        String eventCategoryName = eventCategory.getSelectedItem().toString();
        event.setEventCategory(new EventCategory(eventCategoryName));

        if(eventPicturePath != null) {
            event.setImage(eventPicturePath);
        }
        return event;
    }

    private void formResult(Event event) {
        Intent i = new Intent();
        i.putExtra(ADDED_EVENT, event);
        setResult(RESULT_OK, i);
        finish();
    }

    private void save(Event event) {
        String key = databaseReference.push().getKey();
        event.setId(key);
        databaseReference.child(key).setValue(event);
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

            eventPicturePath = "images/events/"+ txtName.getText().toString()+".jpg";
            StorageReference ref = storageReference.child(eventPicturePath);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNewEventActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNewEventActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
