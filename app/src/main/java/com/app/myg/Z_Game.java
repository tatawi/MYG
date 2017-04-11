package com.app.myg;

/**
 * Created by mbaudrais on 10/04/2017.
 */

public class Z_Game
{
    public String jeuId;
    public String nom;

    public int nbJoueurs;
    public int dureeM;
    public int note;
    public Z_E_GameType type;
    public int complexite;

    public Z_Game(String id, String nom, int nb, int duree, int note, Z_E_GameType type, int compl)
    {
        this.jeuId=id;
        this.nom=nom;
        this.nbJoueurs=nb;
        this.dureeM=duree;
        this.note=note;
        this.type=type;
        this.complexite=compl;
    }

}
