package com.app.myg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbaudrais on 10/04/2017.
 */

public class Z_Group
{
    public String groupId;
    public String nom;
    public List<String> list_membresId;
    public List<String> list_demandesMembresId;

    public Z_Group()
    {
        this.list_membresId= new ArrayList<String>();
        this.list_demandesMembresId= new ArrayList<String>();
    }

    public Z_Group(String id, String nom)
    {
        this.groupId=id;
        this.nom=nom;
        this.list_membresId= new ArrayList<String>();
        this.list_demandesMembresId= new ArrayList<String>();
    }
}
