package com.app.myg;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class FG_rech extends AppCompatActivity
{

    private Button btn_all;
    private Button btn_rech;
    private Button btn_add;
    private Button btn_rejoindre;
    private EditText tb_nomGroupe;

    private String nomGroupRech="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_rech);


        //initialisation des objets

        btn_all = (Button) findViewById(R.id.FG_rech_btn_all);
        btn_rech = (Button) findViewById(R.id.FG_rech_btn_rech);
        btn_rech.setPaintFlags(btn_rech.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_add = (Button) findViewById(R.id.FG_rech_btn_add);
        btn_rejoindre = (Button) findViewById(R.id.FG_rech_btn_rejoindre);
        tb_nomGroupe = (EditText) findViewById(R.id.FG_rech_tb_nomGroupe);


        //listeners
        btn_add.setOnClickListener(onAdd);
        btn_all.setOnClickListener(onAll);
        btn_rejoindre.setOnClickListener(onRejoindre);
    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //onRejoindre
    View.OnClickListener onRejoindre = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            nomGroupRech = tb_nomGroupe.getText().toString();
            if(nomGroupRech != null && nomGroupRech.length()>1)
            {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Groupes");
                mDatabase.addListenerForSingleValueEvent(readGroupsListener);
            }
            else
            {
                Toast.makeText(FG_rech.this, "Veuillez entrer un nom",Toast.LENGTH_SHORT).show();
            }


            //Intent intent = new Intent(FG_rech.this, FG_main.class);
            //startActivity(intent);
        }
    };

    //ON ADD
    View.OnClickListener onAll = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(FG_rech.this, FG_main.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onAdd = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(FG_rech.this, FG_add.class);
            startActivity(intent);
        }
    };


//---------------------------------------------------------------------------------------
//	BDD LISTENERS
//---------------------------------------------------------------------------------------

    //FOR BDD
    ValueEventListener readGroupsListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            boolean find = false;
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
            {
                Z_Group group = postSnapshot.getValue(Z_Group.class);

                if(group.nom.equals(nomGroupRech))
                {
                    FirebaseAuth mAuth= FirebaseAuth.getInstance();
                    String userId = mAuth.getCurrentUser().getUid();
                    find=true;


                    if(group.list_demandesMembresId.contains(userId))
                    {
                        Toast.makeText(FG_rech.this, "Vous avez déjà envoyé une demande pour ce groupe !",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //add user to group
                        group.list_demandesMembresId.add(userId);
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child("Groupes").child(group.groupId).setValue(group);

                        Toast.makeText(FG_rech.this, "Demande envoyée",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(FG_rech.this, A_Accueil.class);
                        startActivity(intent);
                    }

                }

            }
            if (!find)
            {
                Toast.makeText(FG_rech.this, "Aucun groupe avec ce nom trouvé",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };

}
