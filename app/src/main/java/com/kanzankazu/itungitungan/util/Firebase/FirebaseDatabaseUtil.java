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
        void onSuccessString(String message);

        void onFailureString(String message);
    }

    public interface ValueListenerData {
        void onSuccessData(DataSnapshot dataSnapshot);

        void onFailureData(String message);
    }

    public interface ValueListenerObject {

        void onSuccessData(Object dataSnapshot);

        void onFailureData(String message);

    }

    public interface ValueListenerTrueFalse {

        void onSuccessExist(Boolean isExists);

        void onFailureExist(String message);

    }

    public interface ValueListenerDataTrueFalse {
        void onSuccessDataExist(DataSnapshot dataSnapshot, Boolean isExsist);

        void onFailureDataExist(String message);
    }
}
