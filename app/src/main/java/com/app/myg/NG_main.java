package com.app.myg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NG_main extends AppCompatActivity
{
    //variables
    private Z_Game game;

    //objets page
    public ProgressDialog mProgressDialog;
    private EditText tb_nom;
    private SeekBar sb_joueurs;
    private RatingBar rb_note;
    private Spinner sp_type;
    private TextView lb_nbJoueurs;

    private ImageButton btn_hh_moins;
    private ImageButton btn_hh_plus;
    private ImageButton btn_mm_moins;
    private ImageButton btn_mm_plus;
    private TextView lb_hh;
    private TextView lb_mm;
    FloatingActionButton btn_add;


    private ImageButton btn_complx_easy;
    private ImageButton btn_complx_normal;
    private ImageButton btn_complx_hard;

    private TextView lb_complx_easy;
    private TextView lb_complx_normal;
    private TextView lb_complx_hard;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ng_main);

        //initialisations objets page
        tb_nom = (EditText) findViewById(R.id.NG_main_tb_name);
        sb_joueurs = (SeekBar) findViewById(R.id.NG_main_sb_nbJoueurs);
        rb_note = (RatingBar) findViewById(R.id.NG_main_rb_note);
        btn_complx_easy = (ImageButton) findViewById(R.id.NG_main_btn_compl_easy);
        btn_complx_normal = (ImageButton) findViewById(R.id.NG_main_btn_compl_normal);
        btn_complx_hard = (ImageButton) findViewById(R.id.NG_main_btn_compl_hard);
        lb_complx_easy = (TextView) findViewById(R.id.NG_main_lb_compl_easy);
        lb_complx_normal = (TextView) findViewById(R.id.NG_main_lb_compl_normal);
        lb_complx_hard = (TextView) findViewById(R.id.NG_main_lb_compl_hard);
        sp_type = (Spinner) findViewById(R.id.NG_main_sp_type);
        btn_add = (FloatingActionButton) findViewById(R.id.NG_main_btn_add);
        btn_hh_moins= (ImageButton) findViewById(R.id.NG_main_btn_hour_moins);
        btn_hh_plus= (ImageButton) findViewById(R.id.NG_main_btn_hour_plus);
        btn_mm_moins= (ImageButton) findViewById(R.id.NG_main_btn_min_moins);
        btn_mm_plus= (ImageButton) findViewById(R.id.NG_main_btn_min_plus);
        lb_hh= (TextView) findViewById(R.id.NG_main_lb_hour);
        lb_mm= (TextView) findViewById(R.id.NG_main_lb_min);
        lb_nbJoueurs= (TextView) findViewById(R.id.NG_main_lb_nbJoueurs);


        //listeners
        btn_complx_easy.setOnClickListener(onSelectComplxEasy);
        btn_complx_normal.setOnClickListener(onSelectComplxNormal);
        btn_complx_hard.setOnClickListener(onSelectComplxHard);
        btn_add.setOnClickListener(onAdd);
        sb_joueurs.setOnSeekBarChangeListener(new seekBarListener());
        btn_hh_plus.setOnClickListener(onAddHour);
        btn_hh_moins.setOnClickListener(onRemoveHour);
        btn_mm_plus.setOnClickListener(onAddMin);
        btn_mm_moins.setOnClickListener(onRemoveMin);

        //spinner
        List<String> spinnerArray =  new ArrayList<String>();

        for (Z_E_GameType type :Z_E_GameType.values() )
        {
            spinnerArray.add(type.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(adapter);

        //rating bar - nb étoiles
        rb_note.setNumStars(5);

        //SeekBar - nb joueurs
        sb_joueurs.setMax(10);
        sb_joueurs.setProgress(1);


        //ini
        game=new Z_Game();
        game.complexite=0;


    }





//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    //ON ADD
    View.OnClickListener onAdd = new View.OnClickListener()
    {
        public void onClick(View v)
        {

            showProgressDialog();

            Date d = new Date();

            game.id=Integer.parseInt(""+d.getYear()+""+d.getMonth()+""+d.getDay()+""+d.getHours()+""+d.getMinutes()+""+d.getSeconds());
            game.nom=tb_nom.getText().toString();
            game.nbJoueurs=sb_joueurs.getProgress();
            game.dureeM=Integer.valueOf(lb_hh.getText().toString())*60+Integer.valueOf(lb_mm.getText().toString());
            game.note=(int)rb_note.getRating();
            game.type=getType(sp_type.getSelectedItem().toString());

            //Z_Base bdd = new Z_Base();

           // bdd.addGame(game);

            /*FirebaseAuth mAuth= FirebaseAuth.getInstance();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            mDatabase.addListenerForSingleValueEvent(postListener);


            Toast.makeText(NG_main.this, "Jeu créé",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NG_main.this, A_Accueil.class);
            startActivity(intent);*/

            Z_Base bdd = new Z_Base();

            bdd.addGame(game);
            hideProgressDialog();
            Toast.makeText(NG_main.this, "Jeu créé",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NG_main.this, A_Accueil.class);
            startActivity(intent);
        }
    };

    //FOR BDD
    ValueEventListener postListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {

            Z_user user = dataSnapshot.getValue(Z_user.class);

            if(!user.list_jeux.contains(game)) {
                user.list_jeux.add(game);

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users").child(user.userId).setValue(user);
            }
            else
            {
                Toast.makeText(NG_main.this, "Ce jeu existe déjà",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {
            // Getting Post failed, log a message
        }
    };

    //SeekBar
    private class seekBarListener implements SeekBar.OnSeekBarChangeListener
    {

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
        {
            lb_nbJoueurs.setText(""+seekBar.getProgress());
        }

        public void onStartTrackingTouch(SeekBar seekBar) {}

        public void onStopTrackingTouch(SeekBar seekBar) {}

    }



    //click projets en préparation
    View.OnClickListener onSelectComplxEasy = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            btn_complx_easy.setImageResource(R.drawable.ic_complx_easy_fill);
            btn_complx_normal.setImageResource(R.drawable.ic_complx_normal);
            btn_complx_hard.setImageResource(R.drawable.ic_complx_hard);

            lb_complx_easy.setTextColor(Color.parseColor("#16a085"));
            lb_complx_normal.setTextColor(Color.parseColor("#424242"));
            lb_complx_hard.setTextColor(Color.parseColor("#424242"));

            game.complexite=1;

        }
    };

    View.OnClickListener onSelectComplxNormal = new View.OnClickListener() {
        public void onClick(View v)
        {
            btn_complx_easy.setImageResource(R.drawable.ic_complx_easy);
            btn_complx_normal.setImageResource(R.drawable.ic_complx_normal_fill);
            btn_complx_hard.setImageResource(R.drawable.ic_complx_hard);

            lb_complx_easy.setTextColor(Color.parseColor("#424242"));
            lb_complx_normal.setTextColor(Color.parseColor("#f1c40f"));
            lb_complx_hard.setTextColor(Color.parseColor("#424242"));

            game.complexite=2;
        }
    };

    View.OnClickListener onSelectComplxHard = new View.OnClickListener() {
        public void onClick(View v)
        {
            btn_complx_easy.setImageResource(R.drawable.ic_complx_easy);
            btn_complx_normal.setImageResource(R.drawable.ic_complx_normal);
            btn_complx_hard.setImageResource(R.drawable.ic_complx_hard_fill);

            lb_complx_easy.setTextColor(Color.parseColor("#424242"));
            lb_complx_normal.setTextColor(Color.parseColor("#424242"));
            lb_complx_hard.setTextColor(Color.parseColor("#c0392b"));

            game.complexite=3;
        }
    };

    View.OnClickListener onAddHour = new View.OnClickListener() {
        public void onClick(View v)
        {
            int hour = Integer.parseInt(lb_hh.getText().toString());
            hour++;
            lb_hh.setText(""+hour);
        }
    };

    View.OnClickListener onRemoveHour = new View.OnClickListener() {
        public void onClick(View v)
        {
            int hour = Integer.parseInt(lb_hh.getText().toString());
            hour--;
            if(hour < 0)
                hour=0;
            lb_hh.setText(""+hour);
        }
    };

    View.OnClickListener onAddMin = new View.OnClickListener() {
        public void onClick(View v)
        {
            int min = Integer.parseInt(lb_mm.getText().toString());
            min=min+15;
            lb_mm.setText(""+min);

            if(min>60)
            {
                min=0;
                lb_mm.setText(""+min);
                int hour = Integer.parseInt(lb_hh.getText().toString());
                hour++;
                lb_hh.setText(""+hour);
            }
        }
    };

    View.OnClickListener onRemoveMin = new View.OnClickListener() {
        public void onClick(View v)
        {
            int min = Integer.parseInt(lb_mm.getText().toString());
            min=min-15;
            if(min < 15)
                min=15;

            lb_mm.setText(""+min);

        }
    };



//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------

    private Z_E_GameType getType(String type)
    {
        Z_E_GameType monType;
        monType=Z_E_GameType.Adresse;

        switch (type)
        {
            case "Adresse":
                monType=Z_E_GameType.Adresse;
                break;
            case "Affrontement":
                monType=Z_E_GameType.Affrontement;
                break;
            case "Ambiance":
                monType=Z_E_GameType.Ambiance;
                break;
            case "Carte":
                monType=Z_E_GameType.Carte;
                break;
            case "Connaissances":
                monType=Z_E_GameType.Connaissances;
                break;
            case "Cooperation":
                monType=Z_E_GameType.Cooperation;
                break;
            case "Enquete":
                monType=Z_E_GameType.Enquete;
                break;
            case "Logique":
                monType=Z_E_GameType.Logique;
                break;
            case "Rapidité":
                monType=Z_E_GameType.Rapidité;
                break;
            case "Réflexion":
                monType=Z_E_GameType.Réflexion;
                break;
            case "Strategie":
                monType=Z_E_GameType.Strategie;
                break;
            case "Autre":
                monType=Z_E_GameType.Autre;
                break;
        }

        return monType;
    }

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
