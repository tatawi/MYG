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


        /*FirebaseAuth mAuth= FirebaseAuth.getInstance();
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

        Z_Game jeu1 = new Z_Game("Pendemie",4,60,3,Z_E_GameType.Cooperation, 3);
        Z_Game jeu2 = new Z_Game("Seigneur des Tenebres",10,15,1,Z_E_GameType.Adresse, 1);
        Z_Game jeu3 = new Z_Game("Citadelle",8,30,1,Z_E_GameType.Affrontement, 1);
        Z_Game jeu4 = new Z_Game("Le Havre",5,120,2,Z_E_GameType.Carte, 2);
        Z_Game jeu5 = new Z_Game("Game Of Thrones",6,240,3,Z_E_GameType.Enquete, 3);
        Z_Game jeu6 = new Z_Game("Les colon de Catane",4,120,2,Z_E_GameType.Logique, 2);
        Z_Game jeu7 = new Z_Game("Le Saboteur",10,20,1,Z_E_GameType.Strategie, 1);
        Z_Game jeu8 = new Z_Game("Munchkin de l'espace",10,120,2,Z_E_GameType.Enquete, 2);
        Z_Game jeu9 = new Z_Game("Service Compris",5,15,1,Z_E_GameType.Cooperation, 1);
        Z_Game jeu10 = new Z_Game("Dame de pique",4,60,1,Z_E_GameType.Affrontement, 1);

        user.list_jeux.add(jeu1);
        user.list_jeux.add(jeu2);
        user.list_jeux.add(jeu3);
        user.list_jeux.add(jeu4);
        user.list_jeux.add(jeu5);
        user.list_jeux.add(jeu6);
        user.list_jeux.add(jeu7);
        user.list_jeux.add(jeu8);
        user.list_jeux.add(jeu9);
        user.list_jeux.add(jeu10);
        user.list_groupes.add(group);
        user.list_groupes.add(group2);
*/



       // bdd.addUser(user);


    }
}
