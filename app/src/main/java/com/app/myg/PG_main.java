package com.app.myg;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PG_main extends AppCompatActivity
{
    //objets page
    public ProgressDialog mProgressDialog;
    private LinearLayout globalLayout;
    private Spinner sp_groupe;
    private SeekBar sb_nbJoueurs;
    private TextView lb_nbJoueurs;
    private SeekBar sb_difficulte;
    private TextView lb_difficulte;
    private SeekBar sb_duree;
    private TextView lb_duree;

    final Context context = this;
    private Z_user user;
    private List<String> list_strgroupes;
    private List<Z_Group> list_groupes;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pg_main);

        //init objets pages
        globalLayout =  (LinearLayout) findViewById(R.id.pg_main_ll_global);
        sp_groupe = (Spinner) findViewById(R.id.pg_main_sp_groupe);
        sb_nbJoueurs = (SeekBar) findViewById(R.id.pg_main_sk_nbJoueurs);
        lb_nbJoueurs = (TextView) findViewById(R.id.pg_main_tb_nbJoueurs);
        sb_difficulte = (SeekBar) findViewById(R.id.pg_main_sk_diff);
        lb_difficulte = (TextView) findViewById(R.id.pg_main_tb_diff);
        sb_duree = (SeekBar) findViewById(R.id.pg_main_sk_duree);
        lb_duree = (TextView) findViewById(R.id.pg_main_tb_duree);

        //listeners
        sb_nbJoueurs.setOnSeekBarChangeListener(new onChangeNbJoueurs());
        sb_difficulte.setOnSeekBarChangeListener(new onChangeDifficulte());
        sb_duree.setOnSeekBarChangeListener(new onChangeDuree());



        showProgressDialog();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(readUser);








    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ng_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        showProgressDialog();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_ng_main_search)
        {
            globalLayout.removeAllViews();

            if(sp_groupe.getSelectedItem().toString().equals("Mes jeux"))
            {
                FirebaseAuth mAuth= FirebaseAuth.getInstance();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                mDatabase.addListenerForSingleValueEvent(readUserDisplay);
            }
            else
            {

                for (Z_Group group : list_groupes) {
                    if (group.nom.equals(sp_groupe.getSelectedItem().toString())) {
                        for (String userId : group.list_membresId) {
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            mDatabase.addListenerForSingleValueEvent(readUserDisplay);
                        }
                    }
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//---------------------------------------------------------------------------------------
//	BDD LISTENERS
//---------------------------------------------------------------------------------------

    //FOR BDD
    ValueEventListener readUser = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            user = dataSnapshot.getValue(Z_user.class);
            list_strgroupes =  new ArrayList<String>();
            list_groupes = new ArrayList<Z_Group>();
            list_strgroupes.add("Mes jeux");

            for (String idGroup : user.list_groupes)
            {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Groupes").child(idGroup);
                mDatabase.addListenerForSingleValueEvent(readGroup);
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list_strgroupes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_groupe.setAdapter(adapter);

            hideProgressDialog();
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };


    ValueEventListener readGroup = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Z_Group group = dataSnapshot.getValue(Z_Group.class);
            if (group != null)
            {

                list_groupes.add(group);
                list_strgroupes.add(group.nom);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };


    ValueEventListener readUserDisplay = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            Z_user myUser = dataSnapshot.getValue(Z_user.class);

            for (Z_Game game : myUser.list_jeux)
            {
                //vérif conditions recherche
                Boolean isGameValid = true;

                if (game != null )
                {
                    //NB joueurs
                    if(!lb_nbJoueurs.getText().toString().equals("All"))
                    {
                        if (Integer.parseInt(lb_nbJoueurs.getText().toString())!=game.nbJoueurs)
                        {
                            isGameValid = false;
                        }
                    }


                    //difficultée
                    if(!lb_difficulte.getText().toString().equals("All"))
                    {
                        if (sb_difficulte.getProgress()!=game.complexite)
                        {
                            isGameValid = false;
                        }
                    }


                    //duree
                    if(!lb_duree.getText().toString().equals("All"))
                    {
                        int duree = sb_duree.getProgress()*15;
                        if (!(duree-35<game.dureeM && game.dureeM < duree+35))
                        {
                            isGameValid = false;
                        }
                    }
                }
                else
                {
                    isGameValid = false;
                }



                if (isGameValid)
                {
                    //panel global
                    LinearLayout LLgame = new LinearLayout(context);
                    LLgame.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                    LLParams.setMargins(20, 5, 20, 0);
                    LLgame.setLayoutParams(LLParams);
                    LLgame.setBackgroundColor(Color.parseColor("#FDFDFD"));
                    //LLgame.setId(game.id);

                    //tb nom
                    TextView nom = new TextView(context);
                    nom.setText(game.nom);
                    nom.setTextSize(14);
                    nom.setTextColor(Color.parseColor("#000000"));
                    LLgame.addView(nom);

                    //layout
                    LinearLayout LLContenu = new LinearLayout(context);
                    LLContenu.setOrientation(LinearLayout.HORIZONTAL);
                    LLContenu.setLayoutParams(LLParams);

                    ImageButton img = new ImageButton(context);
                    img.setBackgroundColor(Color.TRANSPARENT);

                    LinearLayout LLTextes = new LinearLayout(context);
                    LLTextes.setOrientation(LinearLayout.VERTICAL);
                    LLTextes.setLayoutParams(LLParams);

                    //joueurs
                    TextView nb = new TextView(context);
                    nb.setText("Nombre de joueurs : " + game.nbJoueurs);
                    nb.setTextSize(12);
                    LLTextes.addView(nb);

                    //dureé
                    int hh = game.dureeM/60;
                    int mm = game.dureeM-(hh*60);
                    TextView duree = new TextView(context);
                    duree.setText("Durée d'une partie : "+ hh+"h"+mm);
                    duree.setTextSize(12);
                    LLTextes.addView(duree);

                    //ETOILES
                    LinearLayout LLStars = new LinearLayout(context);
                    LLStars.setOrientation(LinearLayout.HORIZONTAL);
                    LLStars.setLayoutParams(LLParams);

                    for (int i=0; i<game.note; i++)
                    {
                        ImageButton bt = new ImageButton(context);
                        bt.setBackgroundColor(Color.TRANSPARENT);
                        bt.setImageResource(R.drawable.ic_star_full);
                        LLStars.addView(bt);
                    }
                    for (int i=game.note; i<5; i++)
                    {
                        ImageButton bt = new ImageButton(context);
                        bt.setBackgroundColor(Color.TRANSPARENT);
                        bt.setImageResource(R.drawable.ic_star_empty);
                        LLStars.addView(bt);
                    }
                    LLTextes.addView(LLStars);


                    LLContenu.addView(img);
                    LLContenu.addView(LLTextes);

                    LLgame.addView(LLContenu);
                    //LLgame.setOnLongClickListener(onLongClickLayout);
                    globalLayout.addView(LLgame);


                    switch (game.type)
                    {
                        case Adresse:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_adresse_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_adresse_normal);
                            else
                                img.setImageResource(R.drawable.ic_adresse_hard);
                            break;
                        case Affrontement:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_affrontement_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_affrontement_normal);
                            else
                                img.setImageResource(R.drawable.ic_affrontement_hard);
                            break;
                        case Ambiance:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_ambiance_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_ambiance_normal);
                            else
                                img.setImageResource(R.drawable.ic_ambiance_hard);
                            break;
                        case Carte:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_cartes_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_cartes_normal);
                            else
                                img.setImageResource(R.drawable.ic_cartes_hard);
                            break;
                        case Connaissances:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_conn_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_conn_normal);
                            else
                                img.setImageResource(R.drawable.ic_conn_hard);
                            break;
                        case Cooperation:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_coop_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_coop_normal);
                            else
                                img.setImageResource(R.drawable.ic_coop_hard);
                            break;
                        case Enquete:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_enquete_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_enquete_normal);
                            else
                                img.setImageResource(R.drawable.ic_enquete_hard);
                            break;
                        case Logique:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_logique_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_logique_normal);
                            else
                                img.setImageResource(R.drawable.ic_logique_hard);
                            break;
                        case Rapidité:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_speed_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_speed_normal);
                            else
                                img.setImageResource(R.drawable.ic_speed_hard);
                            break;
                        case Réflexion:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_reflexion_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_reflexion_normal);
                            else
                                img.setImageResource(R.drawable.ic_reflexion_hard);
                            break;
                        case Strategie:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_strat_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_strat_normal);
                            else
                                img.setImageResource(R.drawable.ic_strat_hard);
                            break;
                        case Autre:
                            if(game.complexite==1)
                                img.setImageResource(R.drawable.ic_autre_easy);
                            else if (game.complexite==2)
                                img.setImageResource(R.drawable.ic_autre_normal);
                            else
                                img.setImageResource(R.drawable.ic_autre_hard);
                            break;


                    }
                }
            }

            if(user.list_jeux.size()<1)
            {
                LinearLayout LLgame = new LinearLayout(context);
                LLgame.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                LLParams.setMargins(50, 500, 50, 0);
                LLgame.setLayoutParams(LLParams);
                LLgame.setBackgroundColor(Color.parseColor("#EEEEEE"));


                Button btn = new Button(context);
                btn.setText("Aucun jeux");
                btn.setBackgroundColor(Color.parseColor("#3F51B5"));
                btn.setTextColor(Color.parseColor("#FAFAFA"));
                btn.setGravity(Gravity.CENTER);

                //btn.setOnClickListener(onAddaGame);

                LLgame.addView(btn);

                globalLayout.addView(LLgame);






            }
            hideProgressDialog();

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };



//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //SeekBar
    private class onChangeNbJoueurs implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if(seekBar.getProgress()==0)
            {
                lb_nbJoueurs.setText("All");
            }
            else
            {
                lb_nbJoueurs.setText(""+seekBar.getProgress());
            }

        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }


    private class onChangeDifficulte implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            switch (seekBar.getProgress())
            {
                case 0:
                    lb_difficulte.setText("All");
                    break;
                case 1:
                    lb_difficulte.setText("Facile");
                    break;
                case 2:
                    lb_difficulte.setText("Moyen");
                    break;
                case 3:
                    lb_difficulte.setText("Difficile");
                    break;
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }


    private class onChangeDuree implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            if(seekBar.getProgress()==0)
            {
                lb_duree.setText("All");
            }
            else
            {
                int duree = seekBar.getProgress()*15;
                int hour = duree/60;
                int min = duree - (hour*60);

                if (hour == 0)
                {
                    lb_duree.setText(min+"m");
                }
                else
                {
                    lb_duree.setText(hour+"h"+min);
                }

            }

        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

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
