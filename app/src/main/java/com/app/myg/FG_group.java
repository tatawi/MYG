package com.app.myg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FG_group extends AppCompatActivity
{
    private TextView lb;

    private Z_Group group;
    private List<Z_user> listMembres;
    private List<Z_user> listDemandes;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_group);

        Intent myIntent = getIntent(); // gets the previously created intent
        String groupId = myIntent.getStringExtra("groupId");
        group=new Z_Group();
        listMembres = new ArrayList<Z_user>();
        listDemandes = new ArrayList<Z_user>();

        lb = (TextView) findViewById(R.id.fg_group_lb);
        lb.setText(groupId);


        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("list_groupes").child(""+groupId);
        mDatabase.addListenerForSingleValueEvent(readGroupListener);



    }





//---------------------------------------------------------------------------------------
//	BDD LISTENERS
//---------------------------------------------------------------------------------------

    //FOR BDD
    ValueEventListener readGroupListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            group = dataSnapshot.getValue(Z_Group.class);

            for(String userId : group.list_membresId)
            {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                mDatabase.addListenerForSingleValueEvent(readUserMembreListener);
            }

            for(String userId : group.list_demandesMembresId)
            {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                mDatabase.addListenerForSingleValueEvent(readUserDemandeurListener);
            }



        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };


    ValueEventListener readUserMembreListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Z_user user = dataSnapshot.getValue(Z_user.class);
            listMembres.add(user);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };

    ValueEventListener readUserDemandeurListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Z_user user = dataSnapshot.getValue(Z_user.class);
            listDemandes.add(user);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };

}
