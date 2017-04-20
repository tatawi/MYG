package com.app.myg;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FG_add extends AppCompatActivity
{
    private Button btn_all;
    private Button btn_rech;
    private Button btn_add;
    private Button btn_addGroup;
    private EditText tb_nom;

    private List<Z_Group> list_groupes;
    private Z_Group newGroup;
    private Z_user user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_add);

        //initialisation des objets
        btn_addGroup = (Button) findViewById(R.id.FG_add_btn_addGroup);
        tb_nom = (EditText) findViewById(R.id.FG_add_tb_name);
        btn_all = (Button) findViewById(R.id.FG_add_btn_all);
        btn_rech = (Button) findViewById(R.id.FG_add_btn_rech);
        btn_add = (Button) findViewById(R.id.FG_add_btn_add);
        btn_add.setPaintFlags(btn_add.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //listeners
        btn_addGroup.setOnClickListener(onAdd);

        btn_all.setOnClickListener(onAll);
        btn_rech.setOnClickListener(onRech);

    }




//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //ON ADD
    View.OnClickListener onAdd = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            newGroup = new Z_Group();

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Groupes");
            mDatabase.addListenerForSingleValueEvent(onSaveGlobalGroupListener);

            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
            mDatabase.addListenerForSingleValueEvent(onSaveUserGroupListener);

            Toast.makeText(FG_add.this, "Groupe créé",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FG_add.this, FG_main.class);
            startActivity(intent);

        }
    };

    //ON ADD
    View.OnClickListener onAll = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(FG_add.this, FG_main.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onRech = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(FG_add.this, FG_rech.class);
            startActivity(intent);
        }
    };

//---------------------------------------------------------------------------------------
//	BDD LISTENERS
//---------------------------------------------------------------------------------------

    //FOR BDD
    ValueEventListener onSaveGlobalGroupListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            list_groupes = new ArrayList<Z_Group>();
            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            DatabaseReference mDatabase;

            for (DataSnapshot dsp : dataSnapshot.getChildren())
            {
                list_groupes.add(dsp.getValue(Z_Group.class));
            }
            newGroup.groupId=""+list_groupes.size();

            newGroup.nom=tb_nom.getText().toString();
            newGroup.list_demandesMembresId = new ArrayList<String>();
            newGroup.list_membresId = new ArrayList<String>();
            newGroup.list_membresId.add(mAuth.getCurrentUser().getUid());


            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Groupes").child(newGroup.groupId).setValue(newGroup);





           /* Toast.makeText(FG_add.this, "Groupe créé",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FG_add.this, FG_main.class);
            startActivity(intent);*/

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };

    ValueEventListener onSaveUserGroupListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            user = dataSnapshot.getValue(Z_user.class);
            user.list_groupes.add(newGroup.groupId);

            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("Users").child(user.userId).child("list_groupes").setValue(user.list_groupes);




        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };

}
