package com.app.myg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class O_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_main);


       /* FirebaseAuth mAuth= FirebaseAuth.getInstance();
        Z_Base bdd = new Z_Base();


        Z_user user = new Z_user(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail());
        Z_user user2= new Z_user("id2", "user2@mail.fr");
        Z_user user3= new Z_user("id3", "user3@mail.fr");
        Z_user user4= new Z_user("id4", "user4@mail.fr");

        Z_Group group = new Z_Group("group1","Groupe 1");
        group.list_membresId.add(user.userId);
        group.list_membresId.add(user2.userId);
        group.list_membresId.add(user3.userId);

        Z_Group group2 = new Z_Group("group2","Groupe 2");
        group2.list_membresId.add(user.userId);
        group2.list_membresId.add(user4.userId);

        Z_Game jeu1 = new Z_Game("1","jeu1",5,60,0,Z_E_GameType.Cooperation, 1);
        Z_Game jeu2 = new Z_Game("2","jeu2",5,30,0,Z_E_GameType.autre, 1);
        Z_Game jeu3 = new Z_Game("3","jeu3",5,100,0,Z_E_GameType.strategie, 2);

        user.list_jeux.add(jeu1);
        user.list_jeux.add(jeu2);
        user.list_groupes.add(group);
        user.list_groupes.add(group2);

        user2.list_jeux.add(jeu3);
        user2.list_jeux.add(jeu2);
        user2.list_groupes.add(group);

        user3.list_jeux.add(jeu3);
        user3.list_jeux.add(jeu1);
        user3.list_groupes.add(group2);

        user4.list_jeux.add(jeu1);
        user4.list_jeux.add(jeu2);
        user4.list_groupes.add(group);
        user4.list_groupes.add(group2);



        bdd.addUser(user);
        bdd.addUser(user2);
        bdd.addUser(user3);
        bdd.addUser(user4);
        */
    }
}
