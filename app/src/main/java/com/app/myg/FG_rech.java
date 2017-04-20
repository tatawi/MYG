package com.app.myg;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FG_rech extends AppCompatActivity
{

    private Button btn_all;
    private Button btn_rech;
    private Button btn_add;

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


        //listeners
        btn_add.setOnClickListener(onAdd);
        btn_all.setOnClickListener(onAll);
    }


//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

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

}
