package com.app.myg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class AG_main extends AppCompatActivity
{
    private TextView tx;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ag_main);

        tx = (TextView) findViewById(R.id.ag_main_txt);

        Z_Base bdd = new Z_Base();

        Z_user user = bdd.getCurrentUser();

        tx.setText(user.mail);


    }
}
