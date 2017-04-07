package com.app.myg;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialiser objets de la page
        btn_start = (Button) findViewById(R.id.main_btn);


        //listeners
        btn_start.setOnClickListener(onStart);


    }






//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------
    //click onStart
    View.OnClickListener onStart = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, A_Accueil.class);
            startActivity(intent);
        }
    };



}
