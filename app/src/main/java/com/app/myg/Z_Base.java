package com.app.myg;

import android.os.Bundle;
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
    List<Z_Game> list_games;
    List<Z_Group> list_groups;

    public Z_Base()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    public List<Z_user> getAllUsers()
    {


        return new ArrayList<Z_user>();
    }


    public Z_user getCurrentUser()
    {

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        ValueEventListener postListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                user = dataSnapshot.getValue(Z_user.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {}
        };

        mDatabase.addListenerForSingleValueEvent(postListener);

        //user.list_jeux=getUserGames(user);
       // user.list_groupes=getUserGroups(user);

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


    public List<Z_Game> getUserGames(Z_user user)
    {
        list_games = new ArrayList<Z_Game>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.userId).child("list_jeux");

        ValueEventListener postListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    list_games.add(singleSnapshot.getValue(Z_Game.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {}
        };

        mDatabase.addListenerForSingleValueEvent(postListener);
        return list_games;
    }


    public List<Z_Group> getUserGroups(Z_user user)
    {
        list_groups = new ArrayList<Z_Group>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.userId).child("list_groupes");

        ValueEventListener postListener = new ValueEventListener()
        {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                {
                    list_groups.add(singleSnapshot.getValue(Z_Group.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {}
        };

        mDatabase.addListenerForSingleValueEvent(postListener);
        return list_groups;
    }





}
