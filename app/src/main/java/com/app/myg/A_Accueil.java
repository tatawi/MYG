package com.app.myg;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class A_Accueil extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__accueil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //affichage menu gauche
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //affichage menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //selectionne l'accueil du menu
        navigationView.getMenu().getItem(0).setChecked(true);

    }

//---------------------------------------------------------------------------------------
//	FONCTIONS
//---------------------------------------------------------------------------------------






//---------------------------------------------------------------------------------------
//	LISTENERS
//---------------------------------------------------------------------------------------

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.a__accueil, menu);

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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_accueil)
        {
            /*Intent intent = new Intent(A_Accueil.this, A_Accueil.class);
            startActivity(intent);*/
        }

        else if (id == R.id.nav_AllGames)
        {
            Intent intent = new Intent(A_Accueil.this, AG_main.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_NewGame)
        {
            Intent intent = new Intent(A_Accueil.this, NG_main.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_SearchGame)
        {
            Intent intent = new Intent(A_Accueil.this, SG_main.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_FriendsGroups)
        {
            Intent intent = new Intent(A_Accueil.this, FG_main.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_PlayGame)
        {
            Intent intent = new Intent(A_Accueil.this, PG_main.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_Option)
        {
            Intent intent = new Intent(A_Accueil.this, O_main.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_disconnect)
        {
            FirebaseAuth mAuth= FirebaseAuth.getInstance();
            mAuth.signOut();

            Intent intent = new Intent(A_Accueil.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}





/* CREATE NOTIFICATION

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_menu_play)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(this, A_Accueil.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(A_Accueil.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(0, mBuilder.build());

 */

