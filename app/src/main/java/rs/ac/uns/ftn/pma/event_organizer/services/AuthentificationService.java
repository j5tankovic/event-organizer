package rs.ac.uns.ftn.pma.event_organizer.services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import rs.ac.uns.ftn.pma.event_organizer.model.User;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sandra on 5/21/2018.
 */

public class AuthentificationService {

    Activity context;
    User loggedUser;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    SharedPreferences mPrefs;
    User user;

    public AuthentificationService(){
        //this.context = context;
        loggedUser = null;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //mPrefs = context.getPreferences(MODE_PRIVATE);
    }

    public void storeLoggedUserInSharedPref(String email){

       /* SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myObject);
        prefsEditor.putString("LoggedUser", json);
        prefsEditor.commit();*/
    }

    public void logoutSharedPref(){

    }

    public User getLoggedUser(String email){

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //System.out.println("**********************DATA SNAPSHOT: " + dataSnapshot.toString());
                System.out.println("\n\nDATA SNAPSHOT: " + dataSnapshot.toString());
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    user = snapshot.getValue(User.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return user;
    }

}
