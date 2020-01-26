package com.kanzankazu.itungitungan.util.Firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 *
 */
public class FirebaseDatabaseUtil {

    static DatabaseReference getRootRef() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = firebaseDatabase.getReference();

        return reference;
    }

    public interface ValueListenerStringSaveUpdate {
        void onSuccessSaveUpdate(String message);

        void onFailureSaveUpdate(String message);
    }

    public interface ValueListenerStringDelete {
        void onSuccessDelete(String message);

        void onFailureDelete(String message);
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
