package com.kanzankazu.itungitungan.util.Firebase;

import android.app.Activity;
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

    private Activity mActivity;

    public FirebaseDatabaseUtil(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public DatabaseReference getRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDataPrimaryKeyParent(String parent) {
        DatabaseReference rootRef = getRootRef();
        return rootRef.child(parent);
    }

    public DatabaseReference getDataPrimaryKeyChild(String parent, String key) {
        DatabaseReference rootRef = getRootRef();
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
        DatabaseReference rootRef = getRootRef();
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
        DatabaseReference rootRef = getRootRef();
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


}
