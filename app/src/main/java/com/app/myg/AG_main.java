package com.app.myg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AG_main extends AppCompatActivity
{
    public ProgressDialog mProgressDialog;
    private LinearLayout globalLayout;
    final Context context = this;

    private Z_user user;
    private List<Z_Game> list_jeux;
    DatabaseReference mDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ag_main);

        list_jeux=new ArrayList<Z_Game>() ;

        //Interface Objects
        globalLayout =  (LinearLayout) findViewById(R.id.main_ag_globalLayout);

        //Firabase Objects
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid()).child("Jeux");
        Toast.makeText(AG_main.this, mAuth.getCurrentUser().getUid(),Toast.LENGTH_LONG).show();


        showProgressDialog();
        mDatabase.addListenerForSingleValueEvent(postListener);
        hideProgressDialog();


    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //FOR BDD
    /*ValueEventListener postListener = new ValueEventListener()
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
    };*/


    ValueEventListener postListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            list_jeux=new ArrayList<Z_Game>() ;
            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
            {

                int gameId=dataSnapshot.getValue(int.class);




                //DataSnapshot singleSnapshot2=singleSnapshot.getChildren();


                list_jeux.add(dataSnapshot.getValue(Z_Game.class));
            }
            //user = dataSnapshot.getValue(Z_user.class);
            setAffichage(list_jeux);

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };


    //ON LONG CLICK = REMOVE
    View.OnLongClickListener onLongClickLayout = new View.OnLongClickListener() {
        public boolean onLongClick(View v)
        {
            LinearLayout selectedLL = (LinearLayout) v;
            final Z_Game delGame=user.list_jeux.get(selectedLL.getId());

            if(delGame != null)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //REMOVE OK


                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users").child(user.userId).child("list_jeux").child("").removeValue();



                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Supprimer "+delGame.nom+" ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }

/*
            for (C_Sujet s:day.liste_sujets)
            {
                if(s.id==selectedLL.getId())
                {
                    s.creerLesListes(daoMessage, daoparticipant);
                    final C_Sujet sujetToDel = s;

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //REMOVE OK

                                    //delete messages
                                    for (C_Message msgToDel:sujetToDel.liste_messages)
                                    {
                                        daoMessage.supprimer(msgToDel.id, options.online);
                                    }

                                    //delete subject
                                    daoSujet.supprimer(sujetToDel.idSujet, options.online);

                                    //maj jour
                                    for (Iterator<C_Sujet> iter = day.liste_sujets.listIterator(); iter.hasNext(); )
                                    {
                                        C_Sujet subj = iter.next();
                                        if (subj.idSujet.equals(sujetToDel.idSujet))
                                        { iter.remove();}
                                    }
                                    //TODO prix
                                    day.listeToString();
                                    daoJour.modifier(day, options.online);

                                    affichage();


                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(pContext);
                    builder.setMessage("Delete "+s.titre+" ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }
            affichage();*/
            return true;
        }
    };




//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    /*private void setAffichage(Z_user user)
    {
        int idGame = 0;
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



            LLgame.setId(idGame);
            LLgame.setOnLongClickListener(onLongClickLayout);
            globalLayout.addView(LLgame);
            idGame++;
        }

    }*/

    private void setAffichage(List<Z_Game> list_jeux)
    {
        int idGame = 0;
        for (Z_Game game : list_jeux)
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



            LLgame.setId(idGame);
            LLgame.setOnLongClickListener(onLongClickLayout);
            globalLayout.addView(LLgame);
            idGame++;
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
