package com.app.myg;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mbaudrais on 11/04/2017.
 */

public class Z_Base
{
    DatabaseReference mDatabase;
    Z_user user;

    public Z_Base()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public List<Z_user> getAllUsers()
    {


        //Query ref = mDatabase.child("Users").limitToFirst();
/*
        DatabaseReference ref = mDatabase.child("Users");
        Query userQuery = ref orderByChild("userId").equalTo(mAuth.getCurrentUser().getUid());


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Z_user message = messageSnapshot.getValue(Z_user.class);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) { }
        });



        ValueEventListener postListener = new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // Get Post object and use the values to update the UI
                Z_user post = dataSnapshot.getValue(Z_user.class);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.addValueEventListener(postListener);

*/

        return new ArrayList<Z_user>();
    }


    public Z_user getCurrentUser()
    {
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        DatabaseReference ref = mDatabase.child("Users");

        Query userQuery = ref.orderByChild("userId").equalTo(mAuth.getCurrentUser().getUid());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    user=singleSnapshot.getValue(Z_user.class);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                //Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        return user;
    }


    public Z_user getCurrentUser(String mail)
    {
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        Z_user user = new Z_user(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail());
        Z_Base bdd = new Z_Base();



        return new Z_user();
    }

    public List<Z_user> getFriendsOf(String mail)
    {

        return new ArrayList<Z_user>();
    }

    public void addUser(Z_user user)
    {
        mDatabase.child("Users").child(user.userId).setValue(user);
    }





}
