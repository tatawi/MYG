package com.app.myg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class FG_main extends AppCompatActivity
{
    //objets page
    private Button btn_all;
    private Button btn_rech;
    private Button btn_add;

    //objet
    private DatabaseReference mDatabase;
    private Z_user user;
    final Context context = this;
    int groupId;


    private LinearLayout globalLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fg_main);



        // initialisation objets
        btn_all = (Button) findViewById(R.id.FG_main_btn_all);
        btn_all.setPaintFlags(btn_all.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btn_rech = (Button) findViewById(R.id.FG_main_btn_rech);
        btn_add = (Button) findViewById(R.id.FG_main_btn_add);
        globalLayout =  (LinearLayout) findViewById(R.id.FG_main_globalLayout);


        //Listeners
        btn_rech.setOnClickListener(onRech);
        btn_add.setOnClickListener(onAdd);


        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(readGroupsListener);
    }




//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //ON ADD
    View.OnClickListener onAdd = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(FG_main.this, FG_add.class);
            startActivity(intent);
        }
    };

    View.OnClickListener onRech = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(FG_main.this, FG_rech.class);
            startActivity(intent);
        }
    };


    //ON LONG CLICK = REMOVE
    View.OnLongClickListener onLongClickLayout = new View.OnLongClickListener() {
        public boolean onLongClick(View v)
        {
            final LinearLayout selectedLL = (LinearLayout) v;
            groupId = selectedLL.getId();

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //REMOVE OK

                            //supression groupe user
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Users").child(user.userId).child("list_groupes").child(""+groupId).removeValue();

                            //suppression groupe global
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("Groupes").child(""+groupId).removeValue();

                            Toast.makeText(FG_main.this, "Groupe supprimé",Toast.LENGTH_SHORT).show();
                            selectedLL.removeAllViews();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Supprimer le groupe ?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();


            return true;
        }
    };

    //ON CLICK = GO
    View.OnClickListener onClickLayout = new View.OnClickListener() {
        public void onClick(View v)
        {
            final LinearLayout selectedLL = (LinearLayout) v;
            int groupId = selectedLL.getId();

            Intent intent = new Intent(FG_main.this, FG_group.class);
            intent.putExtra("groupId",""+groupId);
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

            user = dataSnapshot.getValue(Z_user.class);

            for(String groupId : user.list_groupes)
            {
                mDatabase = FirebaseDatabase.getInstance().getReference().child("Groupes").child(groupId);
                mDatabase.addListenerForSingleValueEvent(readGroupListener);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };


    ValueEventListener readGroupListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Z_Group group = dataSnapshot.getValue(Z_Group.class);

            //panel global
            LinearLayout LLgame = new LinearLayout(context);
            LLgame.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            LLParams.setMargins(20, 5, 20, 0);
            LLgame.setLayoutParams(LLParams);
            LLgame.setBackgroundColor(Color.parseColor("#FDFDFD"));
            LLgame.setId(Integer.parseInt(group.groupId));

            TextView nom = new TextView(context);
            nom.setText(group.nom);
            nom.setTextSize(14);
            nom.setTextColor(Color.parseColor("#000000"));
            LLgame.addView(nom);

            TextView lb_menbres = new TextView(context);
            lb_menbres.setText((group.list_membresId.size()+1) +" menbres");
            if(group.list_demandesMembresId.size()==1)
                lb_menbres.setText("1 membre");
            lb_menbres.setTextSize(12);
            LLgame.addView(lb_menbres);

            TextView lb_demandes = new TextView(context);
            lb_demandes.setText((group.list_demandesMembresId.size()+1) +" demandes");
            if(group.list_demandesMembresId.size()==0)
                lb_demandes.setText("Pas de nouvelles demandes");
            if(group.list_demandesMembresId.size()==1)
                lb_demandes.setText("1 demande");
            lb_demandes.setTextSize(12);
            LLgame.addView(lb_demandes);


            //listeners
            LLgame.setOnClickListener(onClickLayout);
            LLgame.setOnLongClickListener(onLongClickLayout);

            globalLayout.addView(LLgame);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };

}


