/*
 * Copyright (C) 2015 Willi Ye
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kerneladiutor.selinux_plugin;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.kerneladiutor.library.root.RootUtils;

/**
 * Created by willi on 11.08.15.
 */
public class MainActivity extends android.app.Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new AsyncTask<Void, Void, Boolean>() {

            private ProgressDialog mProgressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mProgressDialog = new ProgressDialog(MainActivity.this);
                mProgressDialog.setMessage(getString(R.string.waiting_for_root));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                return RootUtils.rooted() && RootUtils.rootAccess();
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);

                mProgressDialog.dismiss();
                if (aBoolean) {
                    setContentView(R.layout.activity_main);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.no_root), Toast.LENGTH_LONG).show();
                    finish();
                }

            }

        }.execute();
    }

}
