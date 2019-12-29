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

    public DatabaseReference getRootRef(Boolean isPersistance, Boolean isKeepSync) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        if (isPersistance)
            firebaseDatabase.setPersistenceEnabled(isPersistance);

        DatabaseReference reference = firebaseDatabase.getReference();
        if (isKeepSync)
            reference.keepSynced(isKeepSync);

        return reference;
    }

    public DatabaseReference getDataPrimaryKeyParent(String parent) {
        DatabaseReference rootRef = getRootRef(false, false);
        return rootRef.child(parent);
    }

    public DatabaseReference getDataPrimaryKeyChild(String parent, String key) {
        DatabaseReference rootRef = getRootRef(false, false);
        return rootRef.child(parent).child(key);
    }

    public Query getDataByEqualTo(DatabaseReference reference, String s) {
        return reference.equalTo(s);
    }

    public Query getDataByLimit(DatabaseReference reference, int limit, boolean isfirst) {
        if (isfirst) {
            return reference.limitToFirst(limit);
        } else {
            return reference.limitToLast(limit);
        }
    }

    public Query getDataByLike(DatabaseReference reference, String startAt, String endAt) {
        if (TextUtils.isEmpty(endAt)) {
            return reference.startAt(startAt);
        } else if (TextUtils.isEmpty(startAt)) {
            return reference.endAt(endAt);
        } else {
            return reference.startAt(startAt).endAt(endAt);
        }
    }

    public Query getDataByLike(DatabaseReference reference, int startAt, int endAt) {
        if (endAt < -1) {
            return reference.startAt(startAt);
        } else if (startAt < -1) {
            return reference.endAt(endAt);
        } else {
            return reference.startAt(startAt).endAt(endAt);
        }
    }

    public void setValue(DatabaseReference databaseReference, Object object, OnSuccessListener listener1, OnFailureListener listener2) {
        databaseReference
                .setValue(object)
                .addOnSuccessListener(listener1)
                .addOnFailureListener(listener2);
    }

    public void setChild(DatabaseReference databaseReference, ChildEventListener listener) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listener.onChildChanged(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.onChildRemoved(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                listener.onChildMoved(dataSnapshot, s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    public void getValueSingle(DatabaseReference databaseReference, ValueEventListener listener) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    public void removeValue(DatabaseReference databaseReference, OnSuccessListener listener1, OnFailureListener listener2) {
        databaseReference
                .removeValue()
                .addOnSuccessListener(listener1)
                .addOnFailureListener(listener2);
    }

    public void removeValue(DatabaseReference databaseReference, ValueEventListener listener) {
        DatabaseReference rootRef = getRootRef(false, false);
        rootRef.removeEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onDataChange(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    public void removeValue(DatabaseReference databaseReference, ChildEventListener listener) {
        DatabaseReference rootRef = getRootRef(false, false);
        rootRef.removeEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                listener.onChildAdded(dataSnapshot, s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                listener.onChildChanged(dataSnapshot, s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                listener.onChildRemoved(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                listener.onChildMoved(dataSnapshot, s);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancelled(databaseError);
            }
        });
    }

    /**
     * getWhereData
     *
     * @param key
     * @param whereData
     * @return
     */
    public DatabaseReference getWhereData(String key, String whereData) {
        DatabaseReference parentRef = getDataPrimaryKeyParent(key);
        return getDataByEqualTo(parentRef, whereData).getRef();
    }

    /**
     * getLimitData
     *
     * @param key
     * @param limitData
     * @param isFirst
     * @return
     */
    public DatabaseReference getLimitData(String key, int limitData, boolean isFirst) {
        DatabaseReference parentRef = getDataPrimaryKeyParent(key);
        return getDataByLimit(parentRef, limitData, isFirst).getRef();
    }

    public interface ValueListenerString {
        void onSuccess(String message);

        void onFailure(String message);
    }

    public interface ValueListenerData {
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailure(String message);
    }

    public interface ValueListenerDatas {
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailure(String message);
    }

    public interface ValueListenerTrueFalse {
        void isExist(Boolean isExists);

        void onFailure(String message);
    }
}
