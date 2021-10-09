package com.semihbkgr.filebench.android;

public class AppContext {

    public static AppContext instance;

    private AppContext(){
    }

    public static void initialize(){
        if(instance==null)
            instance=new AppContext();
    }

}
