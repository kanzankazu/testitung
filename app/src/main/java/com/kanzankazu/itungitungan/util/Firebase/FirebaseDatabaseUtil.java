package com.kanzankazu.itungitungan.util.Firebase;

import android.text.TextUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 *
 */
public class FirebaseDatabaseUtil {

    public FirebaseDatabaseUtil() {
    }

    public static DatabaseReference getRootRef() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        /*if (isPersistance)*/
        /*    firebaseDatabase.setPersistenceEnabled(isPersistance);*/

        DatabaseReference reference = firebaseDatabase.getReference();
        /*if (isKeepSync)
            reference.keepSynced(isKeepSync);*/

        return reference;
    }

    public interface ValueListenerString {
        void onSuccess(String message);

        void onFailure(String message);
    }

    public interface ValueListenerData {
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailure(String message);
    }

    public interface ValueListenerObject {

        void onSuccess(Object dataSnapshot);

        void onFailure(String message);

    }

    public interface ValueListenerTrueFalse {

        void isExist(Boolean isExists);

        void onFailure(String message);

    }

    public interface ValueListenerDataTrueFalse {
        void onSuccess(DataSnapshot dataSnapshot, Boolean isExsist);

        void onFailure(String message);
    }
}
