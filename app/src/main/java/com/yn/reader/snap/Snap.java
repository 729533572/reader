package com.yn.reader.snap;


import com.yn.reader.model.booklist.Navigation;

import java.util.List;

public class Snap {

    private int mGravity;
    private List<Navigation> mApps;
    private int mTag;

    public Snap(int gravity, List<Navigation> apps) {
        mGravity = gravity;
        mApps = apps;
    }
    public Snap(int gravity, List<Navigation> apps,int tag) {
        mGravity = gravity;
        mApps = apps;
        this.mTag = tag;
    }


    public int getGravity(){
        return mGravity;
    }

    public List<Navigation> getApps(){
        return mApps;
    }

    public void setTag(int tag) {
        this.mTag = tag;
    }

    public int getTag() {
        return mTag;
    }
}
