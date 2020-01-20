package com.kanzankazu.itungitungan.util.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 */
public class FirebaseDatabaseUtil {

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
