package com.app.myg;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AG_main extends AppCompatActivity
{
    public ProgressDialog mProgressDialog;
    private LinearLayout globalLayout;

    private Z_user user;
    DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ag_main);

        //Interface Objects
        globalLayout =  (LinearLayout) findViewById(R.id.main_ag_globalLayout);

        //Firabase Objects
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());



        showProgressDialog();
        mDatabase.addListenerForSingleValueEvent(postListener);
        hideProgressDialog();


    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //FOR BDD
    ValueEventListener postListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            user = dataSnapshot.getValue(Z_user.class);
            setAffichage(user);

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };




//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    private void setAffichage(Z_user user)
    {
        for (Z_Game game : user.list_jeux)
        {
            //panel global
            LinearLayout LLgame = new LinearLayout(this);
            LLgame.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LLParams.setMargins(20, 10, 20, 0);
            LLgame.setLayoutParams(LLParams);
            //LLgame.setPadding(20, 10, 20, 0);
            LLgame.setBackgroundColor(Color.parseColor("#EEEEEE"));
            //LLglobal.setId(p.id);

            TextView nom = new TextView(this);
            nom.setText(game.nom);
            nom.setTextSize(25);
            nom.setTextColor(Color.parseColor("#3F51B5"));
            LLgame.addView(nom);


            TextView nb = new TextView(this);
            nb.setText(game.nbJoueurs +" joueurs");
            nb.setTextSize(12);
            LLgame.addView(nb);


            TextView duree = new TextView(this);
            duree.setText(game.dureeM +" minutes");
            duree.setTextSize(12);
            LLgame.addView(duree);

            TextView note = new TextView(this);
            note.setText("note : "+game.note);
            note.setTextSize(12);
            LLgame.addView(note);

            TextView type = new TextView(this);
            type.setText(game.type.name());
            type.setTextSize(12);
            LLgame.addView(type);

            TextView compl = new TextView(this);
            compl.setText(""+game.complexite);
            compl.setTextSize(12);
            LLgame.addView(compl);





            globalLayout.addView(LLgame);

        }

    }


//---------------------------------------------------------------------------------------
//	PROGRESS DIALOG
//---------------------------------------------------------------------------------------
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Chargement...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
