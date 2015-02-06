package com.progettostage.nick__000.timetablesapp;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class SettingsPanel extends ActionBarActivity {
static String defaultIP = "146.48.106.198";
static String customIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_panel);

        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        String set = settings.getString("DefaultIp","146.48.106.198");
        EditText editt= (EditText) findViewById(R.id.editText);
        editt.setText(set);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void  ImpostaPred(View v){
        EditText editt = (EditText)   findViewById(R.id.editText);

        editt.setText(defaultIP);
    }




    public void onSave(View v){


        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        SharedPreferences.Editor editor = settings.edit();
        EditText editt= (EditText) findViewById(R.id.editText);
        editor.putString("DefaultIp",editt.getText().toString());
        customIP = editt.getText().toString();
        // Commit the edits!
        editor.commit();
    }
}