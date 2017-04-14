package com.app.myg;

import java.util.Date;

/**
 * Created by mbaudrais on 10/04/2017.
 */

public class Z_Game
{
    public int id;
    public String nom;
    public int nbJoueurs;
    public int dureeM;
    public int note;
    public Z_E_GameType type;
    public int complexite;
    public Date date;

    public Z_Game()
    {

    }

    public Z_Game(String nom, int nb, int duree, int note, Z_E_GameType type, int compl)
    {
        this.nom=nom;
        this.nbJoueurs=nb;
        this.dureeM=duree;
        this.note=note;
        this.type=type;
        this.complexite=compl;
    }

}
