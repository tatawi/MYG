package com.app.myg;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class AG_main extends AppCompatActivity
{
    public ProgressDialog mProgressDialog;
    private LinearLayout globalLayout;
    final Context context = this;

    private Z_user user;
    private List<Z_Game> list_jeux;
    private int gameId;
    private DatabaseReference mDatabase;

    private Button btn_recent;
    private Button btn_top;
    private Button btn_type;

    private String trie;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ag_main);

        //init
        list_jeux=new ArrayList<Z_Game>() ;
        user=new Z_user();
        trie="recent";

        //init objets page
        btn_recent= (Button) findViewById(R.id.AG_main_btn_recent);
        btn_top= (Button) findViewById(R.id.AG_main_btn_top);
        btn_type= (Button) findViewById(R.id.AG_main_btn_type);

        btn_recent.setOnClickListener(onRecent);
        btn_top.setOnClickListener(onTop);
        btn_type.setOnClickListener(onType);

        btn_recent.setPaintFlags(btn_top.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        //Interface Objects
        globalLayout =  (LinearLayout) findViewById(R.id.main_ag_globalLayout);

        //Firabase Objects
        showProgressDialog();
        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(postListener);



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
            hideProgressDialog();
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
            final LinearLayout selectedLL = (LinearLayout) v;
            gameId = selectedLL.getId();

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //REMOVE OK

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child("Users").child(user.userId).child("list_jeux").child(""+gameId).removeValue();
                                Toast.makeText(AG_main.this, "Jeu supprimé",Toast.LENGTH_SHORT).show();

                                /*showProgressDialog();
                                mDatabase.addListenerForSingleValueEvent(postListener);
                                hideProgressDialog();*/
                                selectedLL.removeAllViews();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Supprimer le jeu ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            return true;
        }
    };


    //ON ADD
    View.OnClickListener onAddaGame = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Intent intent = new Intent(AG_main.this, NG_main.class);
            startActivity(intent);
        }
    };


    //ON recent
    View.OnClickListener onRecent = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            btn_recent.setPaintFlags(btn_recent.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btn_top.setPaintFlags(0);
            btn_type.setPaintFlags(0);
            trie="recent";
            showProgressDialog();
            mDatabase.addListenerForSingleValueEvent(postListener);
        }
    };

    //ON top
    View.OnClickListener onTop = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            btn_recent.setPaintFlags(0);
            btn_top.setPaintFlags(btn_top.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            btn_type.setPaintFlags(0);
            trie="top";
            showProgressDialog();
            mDatabase.addListenerForSingleValueEvent(postListener);
        }
    };

    //ON type
    View.OnClickListener onType = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            btn_recent.setPaintFlags(0);
            btn_top.setPaintFlags(0);
            btn_type.setPaintFlags(btn_type.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            trie="type";
            showProgressDialog();
            mDatabase.addListenerForSingleValueEvent(postListener);
        }
    };




//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    private void setAffichage(Z_user user)
    {
        globalLayout.removeAllViews();
        List<Z_Game>list_sorted = trierList(user.list_jeux);
        for (Z_Game game : list_sorted)
        {
            if (game != null)
            {
                //panel global
                LinearLayout LLgame = new LinearLayout(this);
                LLgame.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
                LLParams.setMargins(20, 5, 20, 0);
                LLgame.setLayoutParams(LLParams);
                LLgame.setBackgroundColor(Color.parseColor("#FDFDFD"));
                LLgame.setId(game.id);

                //tb nom
                TextView nom = new TextView(this);
                nom.setText(game.nom);
                nom.setTextSize(14);
                nom.setTextColor(Color.parseColor("#000000"));
                LLgame.addView(nom);

                //layout
                LinearLayout LLContenu = new LinearLayout(this);
                LLContenu.setOrientation(LinearLayout.HORIZONTAL);
                LLContenu.setLayoutParams(LLParams);

                    ImageButton img = new ImageButton(this);
                    img.setBackgroundColor(Color.TRANSPARENT);

                    LinearLayout LLTextes = new LinearLayout(this);
                    LLTextes.setOrientation(LinearLayout.VERTICAL);
                    LLTextes.setLayoutParams(LLParams);

                        //joueurs
                        TextView nb = new TextView(this);
                        nb.setText("Nombre de joueurs : " + game.nbJoueurs);
                        nb.setTextSize(12);
                        LLTextes.addView(nb);

                        //dureé
                        int hh = game.dureeM/60;
                        int mm = game.dureeM-(hh*60);
                        TextView duree = new TextView(this);
                        duree.setText("Durée d'une partie : "+ hh+"h"+mm);
                        duree.setTextSize(12);
                        LLTextes.addView(duree);

                        //ETOILES
                        LinearLayout LLStars = new LinearLayout(this);
                        LLStars.setOrientation(LinearLayout.HORIZONTAL);
                        LLStars.setLayoutParams(LLParams);

                        for (int i=0; i<game.note; i++)
                        {
                            ImageButton bt = new ImageButton(this);
                            bt.setBackgroundColor(Color.TRANSPARENT);
                            bt.setImageResource(R.drawable.ic_star_full);
                            LLStars.addView(bt);
                        }
                        for (int i=game.note; i<5; i++)
                        {
                            ImageButton bt = new ImageButton(this);
                            bt.setBackgroundColor(Color.TRANSPARENT);
                            bt.setImageResource(R.drawable.ic_star_empty);
                            LLStars.addView(bt);
                        }
                        LLTextes.addView(LLStars);


                    LLContenu.addView(img);
                    LLContenu.addView(LLTextes);

                LLgame.addView(LLContenu);
                LLgame.setOnLongClickListener(onLongClickLayout);
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
            LinearLayout LLgame = new LinearLayout(this);
            LLgame.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
            LLParams.setMargins(50, 500, 50, 0);
            LLgame.setLayoutParams(LLParams);
            LLgame.setBackgroundColor(Color.parseColor("#EEEEEE"));


            Button btn = new Button(this);
            btn.setText("Ajouter un premier jeu");
            btn.setBackgroundColor(Color.parseColor("#3F51B5"));
            btn.setTextColor(Color.parseColor("#FAFAFA"));
            btn.setGravity(Gravity.CENTER);

            btn.setOnClickListener(onAddaGame);

            LLgame.addView(btn);

            globalLayout.addView(LLgame);


        }

    }


    private List<Z_Game> trierList(List<Z_Game>list)
    {
        List<Z_Game> sortedList = list;
        sortedList.removeAll(Collections.singleton(null));

        switch (this.trie)
        {
            case "recent":
                //Sorting
                Collections.sort(sortedList, new Comparator<Z_Game>() {
                    @Override
                    public int compare(Z_Game s1, Z_Game s2) {

                        return s1.date.compareTo(s2.date);
                    }
                });
                break;

            case "top":
                //Sorting
                Collections.sort(sortedList, new Comparator<Z_Game>() {
                    @Override
                    public int compare(Z_Game s1, Z_Game s2) {

                        return String.valueOf(s1.note).compareTo(String.valueOf(s2.note));
                    }
                });

                break;

            case "type":
                //Sorting
                Collections.sort(sortedList, new Comparator<Z_Game>() {
                    @Override
                    public int compare(Z_Game s1, Z_Game s2) {

                        return s1.type.compareTo(s2.type);
                    }
                });

                break;


        }
        Collections.reverse(sortedList);
    return sortedList;

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
