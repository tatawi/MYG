package com.app.myg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbaudrais on 10/04/2017.
 */

public class Z_user
{
    public String userId;
    public String mail;
    public List<String> list_groupes;
    public List<Z_Game> list_jeux;

    //Default constructor
    public Z_user()
    {
        this.userId="";
        this.mail="";
        list_groupes=new ArrayList<String>();
        list_jeux=new ArrayList<Z_Game>();
    }

    public Z_user(String userId, String mail)
    {
        this.userId=userId;
        this.mail=mail;
        list_groupes= new ArrayList<String>();
        list_jeux= new ArrayList<Z_Game>();
    }





}