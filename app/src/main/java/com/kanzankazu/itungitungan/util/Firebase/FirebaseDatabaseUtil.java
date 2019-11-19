package com.kanzankazu.itungitungan.util.Firebase;

import android.text.TextUtils;
import com.google.firebase.database.*;

/**
 *
 */
public class FirebaseDatabaseUtil {

    public DatabaseReference getRootRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getDataAllFromPrimaryKey(String parent) {
        DatabaseReference rootRef = getRootRef();
        return rootRef.child(parent);
    }

    public DatabaseReference getDataByPrimaryKey(String parent, String key) {
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

    public void addValue(DatabaseReference databaseReference, ValueEventListener listener) {
        databaseReference.addValueEventListener(new ValueEventListener() {
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

    public void addValueSingle(DatabaseReference databaseReference, ValueEventListener listener) {
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

    public void addChild(DatabaseReference databaseReference, ChildEventListener listener) {
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
