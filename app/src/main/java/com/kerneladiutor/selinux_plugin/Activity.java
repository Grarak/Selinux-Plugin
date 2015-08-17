package com.kerneladiutor.selinux_plugin;

import android.os.Bundle;

import com.kerneladiutor.library.root.RootUtils;

/**
 * Created by willi on 11.08.15.
 */
public class Activity extends android.app.Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RootUtils.rootAccess();
        RootUtils.closeSU();
    }

}
