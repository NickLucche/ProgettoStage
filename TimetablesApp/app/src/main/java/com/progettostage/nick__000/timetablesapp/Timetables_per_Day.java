package com.progettostage.nick__000.timetablesapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class Timetables_per_Day extends ActionBarActivity {

    String monday="";
    String tuesday="";
    String wednesday="";
    String thursday="";
    String friday="";
    String saturday="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetables_per__day);
        final GridView gridView = (GridView) findViewById(R.id.gridView2);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter <String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
                new String[]{"Lunedi","Martedi","Mercoledi","Giovedi","Venerdi","Sabato"}
                );
        spinner.setAdapter(spinnerAdapter);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day==1)
            day=2;
        spinner.setSelection(day-1);

        String readString ="";
        //final List[] array_days = new List[7];




        String[] splitted_text;
        try {
            //reading file .txt
          //  InputStream inputStream = this.getResources().openRawResource(R.raw.orario5bif);
            InputStream inputStream = openFileInput("orario");
            int line=0;
        //    byte[] input = new byte[inputStream.available()];
            //inputStream.read(input);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                readString =scanner.nextLine();
                if(!scanner.hasNextLine())
                    break;
                splitted_text = readString.split(";");
                if(line>=1){
                    monday += splitted_text[0] + "#";
                    tuesday += splitted_text[0] + "#";
                    wednesday += splitted_text[0]+ "#";
                    thursday += splitted_text[0]+ "#";
                    friday += splitted_text[0]+ "#";
                    saturday += splitted_text[0]+ "#";
                    monday += splitted_text[1] + "#";
                    tuesday += splitted_text[2] + "#";
                    wednesday += splitted_text[3]+ "#";
                    thursday += splitted_text[4]+ "#";
                    friday += splitted_text[5]+ "#";
                    saturday += splitted_text[6]+ "#";
                }
                line++;

                /*for(int i = 1;i<=6;i++) {
                    array_days[i].add(splitted_text[i]);

                }*/
            }

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*readString = readString.replaceAll("-","\n");
        readString = readString.replaceAll("na","");
        String [] gridelement =  readString.split(";");*/
        int day_adapter;
        String s = switch_day(day);
        s = s.replaceAll("-","\n");
        s = s.replaceAll("na","");
        String [] gridelement =  s.split("#");
        ArrayAdapter<String> adapter_grid = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gridelement);

        //ArrayAdapter<String> adapter_grid = new ArrayAdapter<String>(this,R.layout.custom_layout_grid,R.id.customTextView,array_days[day]);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,gridelement);
        gridView.setAdapter(adapter_grid);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = switch_day(position+1);
                s = s.replaceAll("-","\n");
                s = s.replaceAll("na","");
                String [] gridelement =  s.split("#");
                ArrayAdapter <String> adapter = new ArrayAdapter<String>(parent.getContext(),android.R.layout.simple_list_item_1, gridelement);
                gridView.setAdapter(adapter);
            }


            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Listener(spinner,array_days,gridView);


    }

    public String switch_day(int day){
        switch (day) {
            case 2:
                return monday;
            case 3:
                return tuesday;
            case 4:
                return wednesday;
            case 5:
                return thursday;
            case 6:
                return friday;
            case 7:
                return saturday;
        }
        return monday;
    }
    public void Listener(Spinner spinner, final List<String>[] array_days,final GridView gridView){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter <String> adapter = new ArrayAdapter<String>(parent.getContext(),R.layout.custom_layout_grid,R.id.customTextView,array_days[position]);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_timetables_per__day, menu);
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
