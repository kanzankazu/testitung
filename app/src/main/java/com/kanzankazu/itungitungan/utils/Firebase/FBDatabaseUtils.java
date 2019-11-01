package com.kanzankazu.itungitungan.utils.Firebase;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FBDatabaseUtils {

    private Activity activity;

    public FBDatabaseUtils(Activity activity) {
        this.activity = activity;
    }

    public DatabaseReference getReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public void getData(DatabaseReference database, FirebaseInterface.ValueListener listener) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancel(databaseError);
            }
        });
    }

    public

    interface FirebaseInterface {
        interface ValueListener{
            void onChange(DataSnapshot dataSnapshot);
            void onCancel(DatabaseError databaseError);
        }
    }
}
