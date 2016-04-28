package com.tanusha.ideasgenerator;

import android.content.Context;
import android.os.AsyncTask;


public class UpdateTask extends AsyncTask<Void, Void, Void> {

    private Context mCon;

    public UpdateTask(Context con) {
        mCon = con;
    }

    @Override
    protected Void doInBackground(Void... nope) {
        try {
            // Set a time to simulate a long update process.
            Thread.sleep(500);

            return null;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Void nope) {

        // Change the menu back
        ((MainActivity) mCon).resetUpdating();
    }
}
