package com.progettostage.nick__000.timetablesapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Spinner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class ShowTimetable extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_timetable);
        GridView gridview = (GridView) findViewById(R.id.gridView);


        String readString ="";


        try {

            //reading file .txt

            InputStream inputStream = openFileInput("orario");
            //InputStream inputStream = this.getResources().openRawResource(R.raw.orario5bif);

            byte[] input = new byte[inputStream.available()];
            while (inputStream.read(input) != -1) {
                readString += new String(input);
            }

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readString = readString.replaceAll("-","\n");
        readString = readString.replaceAll("na","");
        String [] gridelement =  readString.split(";");


        // sostituiamo ListView con GridView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_layout_grid,R.id.customTextView,gridelement);
       //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gridelement);
        gridview.setAdapter(adapter);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

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
}