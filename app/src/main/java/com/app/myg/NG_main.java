package com.app.myg;

import android.graphics.Color;
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

import java.util.ArrayList;
import java.util.List;

public class NG_main extends AppCompatActivity
{
    //variables
    private Z_Game game;

    //objets page
    private EditText tb_nom;
    private SeekBar sb_joueurs;
    private RatingBar rb_note;
    private Spinner sp_type;

    private ImageButton btn_hh_moins;
    private ImageButton btn_hh_plus;
    private ImageButton btn_mm_moins;
    private ImageButton btn_mm_plus;
    private TextView lb_hh;
    private TextView lb_mm;


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

         btn_hh_moins= (ImageButton) findViewById(R.id.NG_main_btn_hour_moins);
         btn_hh_plus= (ImageButton) findViewById(R.id.NG_main_btn_hour_plus);
         btn_mm_moins= (ImageButton) findViewById(R.id.NG_main_btn_min_moins);
         btn_mm_plus= (ImageButton) findViewById(R.id.NG_main_btn_min_plus);
         lb_hh= (TextView) findViewById(R.id.NG_main_lb_hour);
         lb_mm= (TextView) findViewById(R.id.NG_main_lb_min);


        //listeners
        btn_complx_easy.setOnClickListener(onSelectComplxEasy);
        btn_complx_normal.setOnClickListener(onSelectComplxNormal);
        btn_complx_hard.setOnClickListener(onSelectComplxHard);


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

        //rating bar
        rb_note.setNumStars(5);


        //variables
        game = new Z_Game();


    }





//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------


    //click projets en préparation
    View.OnClickListener onSelectComplxEasy = new View.OnClickListener() {
        public void onClick(View v)
        {
            btn_complx_easy.setImageResource(R.drawable.ic_complx_easy_fill);
            btn_complx_normal.setImageResource(R.drawable.ic_complx_normal);
            btn_complx_hard.setImageResource(R.drawable.ic_complx_hard);

            lb_complx_easy.setTextColor(Color.parseColor("#16a085"));
            lb_complx_normal.setTextColor(Color.parseColor("#424242"));
            lb_complx_hard.setTextColor(Color.parseColor("#424242"));

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



}
