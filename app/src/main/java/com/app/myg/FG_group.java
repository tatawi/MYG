package com.app.myg;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    public ProgressDialog mProgressDialog;
    private LinearLayout ll_membres;
    private LinearLayout ll_demandes;

    final Context context = this;
    private Z_Group group;
    private String nomGroupe="Groupe";
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

        ll_membres = (LinearLayout) findViewById(R.id.fg_group_membres_layout);
        ll_demandes = (LinearLayout) findViewById(R.id.fg_group_demandes_layout);

        showProgressDialog();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Groupes").child(""+groupId);
        mDatabase.addListenerForSingleValueEvent(readGroupListener);

        this.setTitle(nomGroupe);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fg_group, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_fg_group_game)
        {
            final Dialog dialog = new Dialog(context);

            dialog.setTitle("Tous les jeux");
            //panel global
            LinearLayout LLmain = new LinearLayout(context);
            LLmain.setOrientation(LinearLayout.VERTICAL);
            LLmain.setBackgroundColor(Color.parseColor("#FDFDFD"));

            TextView title = new TextView(context);
            title.setText("Jeux du groupe");
            title.setTextColor(Color.parseColor("#3F51B5"));
            title.setTextSize(18);
            LLmain.addView(title);

            View v = new View(context);
            LinearLayout.LayoutParams LLParamsV = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            LLParamsV.height=2;
            v.setLayoutParams(LLParamsV);
            v.setBackgroundColor(Color.parseColor("#3F51B5"));
            LLmain.addView(v);


            ScrollView SCmain = new ScrollView(context);
            LLmain.addView(SCmain);

            LinearLayout LLglobal = new LinearLayout(context);
            LLglobal.setOrientation(LinearLayout.VERTICAL);
            LLglobal.setBackgroundColor(Color.parseColor("#FDFDFD"));
            SCmain.addView(LLglobal);





            for (Z_user user : listMembres)
            {
                if (user != null)
                {
                    for (Z_Game game : user.list_jeux)
                    {
                        if (game != null)
                        {
                            //panel global
                            LinearLayout LLgame = new LinearLayout(context);
                            LLgame.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                            LLParams.setMargins(20, 5, 20, 0);
                            LLgame.setPadding(5,5,5,5);
                            LLgame.setLayoutParams(LLParams);
                            LLgame.setBackgroundColor(Color.parseColor("#FDFDFD"));

                            TextView nom = new TextView(context);
                            nom.setText(game.nom);
                            nom.setTextSize(13);
                            nom.setTextColor(Color.parseColor("#000000"));
                            LLgame.addView(nom);

                            LLglobal.addView(LLgame);
                        }
                    }
                }

            }



            Button dialogButton = new Button(context);
            dialogButton.setText("Fermer");
            dialogButton.setBackgroundColor(Color.parseColor("#3F51B5"));
            dialogButton.setTextColor(Color.parseColor("#FDFDFD"));
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            LLmain.addView(dialogButton);


            dialog.setContentView(LLmain);


/*
            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Android custom dialog example!");
            ImageView image = (ImageView) dialog.findViewById(R.id.image);
            image.setImageResource(R.drawable.ic_launcher);

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });*/

            dialog.show();

            //
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            nomGroupe=group.nom;

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

            hideProgressDialog();
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

            //panel global
            LinearLayout LLmembre = new LinearLayout(context);
            LLmembre.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            LLParams.setMargins(20, 5, 20, 0);
            LLmembre.setPadding(5,5,5,5);
            LLmembre.setLayoutParams(LLParams);
            LLmembre.setBackgroundColor(Color.parseColor("#FDFDFD"));

            TextView nom = new TextView(context);
            nom.setText(user.mail);
            nom.setTextSize(14);
            nom.setTextColor(Color.parseColor("#000000"));
            LLmembre.addView(nom);


            TextView lbjeux = new TextView(context);
            lbjeux.setText("nombre de jeux : "+user.list_jeux.size());
            LLmembre.addView(lbjeux);

            ll_membres.addView(LLmembre);



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

            //panel global
            LinearLayout LLdemandes = new LinearLayout(context);
            LLdemandes.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            LLParams.setMargins(20, 5, 20, 0);
            LLdemandes.setLayoutParams(LLParams);
            LLdemandes.setPadding(5,5,5,5);
            LLdemandes.setBackgroundColor(Color.parseColor("#FDFDFD"));

            ImageButton btn_accepter = new ImageButton(context);
            btn_accepter.setImageResource(R.drawable.ic_accept);
            btn_accepter.setOnClickListener(onAccept);
            btn_accepter.setBackgroundColor(Color.TRANSPARENT);
            btn_accepter.setPadding(5,5,5,5);
            LLdemandes.addView(btn_accepter);

            ImageButton btn_refuse = new ImageButton(context);
            btn_refuse.setImageResource(R.drawable.ic_refuse);
            btn_refuse.setBackgroundColor(Color.TRANSPARENT);
            btn_refuse.setPadding(5,5,5,5);
            btn_refuse.setOnClickListener(onRefuse);
            LLdemandes.addView(btn_refuse);


            LinearLayout LLdemandesTxt = new LinearLayout(context);
            LLdemandesTxt.setOrientation(LinearLayout.VERTICAL);
            LLdemandesTxt.setBackgroundColor(Color.parseColor("#FDFDFD"));


            TextView nom = new TextView(context);
            nom.setText(user.mail);
            nom.setTextSize(14);
            nom.setTextColor(Color.parseColor("#000000"));
            LLdemandesTxt.addView(nom);


            TextView lbjeux = new TextView(context);
            lbjeux.setText("nombre de jeux : "+user.list_jeux.size());
            LLdemandesTxt.addView(lbjeux);

            LLdemandes.addView(LLdemandesTxt);
            ll_demandes.addView(LLdemandes);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {}
    };



//---------------------------------------------------------------------------------------
// LISTENERS
//---------------------------------------------------------------------------------------

    View.OnClickListener onAccept = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            //DANS LE GROUPE
            //=>retirer de la liste demandes
            //=>ajouter a la liste membre

            //DANS L'USER
            //=> ajouter groupe

        }
    };


    View.OnClickListener onRefuse = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            //DANS LE GROUPE
            //=>retirer de la liste demandes
        }
    };



//---------------------------------------------------------------------------------------
//	FONCTIONS
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
