package com.app.myg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mbaudrais on 10/04/2017.
 */

public class Z_Group
{
    public String groupId;
    public String name;
    public List<String> list_membresId;

    public Z_Group()
    {

    }

    public Z_Group(String id, String nom)
    {
        this.groupId=id;
        this.name=nom;
        this.list_membresId= new ArrayList<String>();
    }
}
