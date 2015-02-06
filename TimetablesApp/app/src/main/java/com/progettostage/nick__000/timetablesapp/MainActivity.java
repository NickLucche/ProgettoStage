package com.progettostage.nick__000.timetablesapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

//savedInstanceState

public class MainActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.spinner_layout,R.id.spinnertext,
                new String[]{"5AIF", "5BIF", "1E", "5BSA"}
        );
        spinner.setAdapter(adapter);
        // leggere o creare
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //  boolean silent = settings.getBoolean("silentMode", false);
        int set = settings.getInt("SpinPosition", 0);
        spinner.setSelection(set);

        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selected = spinner.getItemAtPosition(position).toString();

                Button btn1 = (Button) findViewById(R.id.button2);
                btn1.setClickable(false);
                Button btn2 = (Button) findViewById(R.id.button6);
                btn2.setClickable(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/






    }


    protected void onStop() {
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Spinner spinnerId = (Spinner) findViewById(R.id.spinner);
        int position = spinnerId.getSelectedItemPosition();
        editor.putInt("SpinPosition", position);

        // Commit the edits!
        editor.commit();
    }


    public void OnClose(MenuItem m) {
        finish();

    }


    public void loadSettings(MenuItem m) {

        Intent intent = new Intent(this, SettingsPanel.class);

        startActivity(intent);


    }

    public void loadTimetables_per_Day(View v) {

        Intent intent = new Intent(this, Timetables_per_Day.class);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void OnAggiornaOrario(View v) throws IOException {

        Button btn = (Button) findViewById(R.id.button);
        btn.setClickable(false);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        int position = spinner.getSelectedItemPosition();
        String selected = spinner.getItemAtPosition(position).toString();
        RetrieveClassSchedule rcs = new RetrieveClassSchedule();

        try {
            String result = rcs.execute(selected).get();

            Toast.makeText(
                    getApplicationContext(),
                    " " + result,
                    Toast.LENGTH_LONG
            ).show();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //Button btn1 = (Button) findViewById(R.id.button2);
        //btn1.setClickable(false);
        //Button btn2 = (Button) findViewById(R.id.button6);
        //btn2.setClickable(false);
        btn.setClickable(true);
        //We have to show last update
        //Calendar calendar = Calendar.getInstance();
        //int day = calendar.get(Calendar.DAY_OF_WEEK);


    }


    public void OnOrarioCompleto(View v) {

        Intent intent = new Intent(this, ShowTimetable.class);
        // EditText editText = (EditText) findViewById(R.id.edit_message);
        // String message = editText.getText().toString();
        // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);


    }

    public void onTimesChanges(View v) {
        Button btn = (Button) findViewById(R.id.button);
        btn.setClickable(false);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        int position = spinner.getSelectedItemPosition();
        String selected = spinner.getItemAtPosition(position).toString();
        TimeScheduleChanges rcs = new TimeScheduleChanges();
        try {
            String scheduleChanges = rcs.execute(selected).get();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(scheduleChanges)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            builder.create().show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        btn.setClickable(true);

    }

    class RetrieveClassSchedule extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... className) {
            try {
                String s = className[0];
                String requested = "orario";
                String customIP = SettingsPanel.customIP;
                if(customIP == null)
                    customIP = SettingsPanel.defaultIP;
                //instead of localhost we need the IP set on the settings page
                Socket socket = new Socket(customIP, 1801);
                InputStream in = socket.getInputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(s + "\n");

                byte[] buff = new byte[1024];
                int readLength = -1;
                while ((readLength = in.read(buff)) > 0) {

                }

                String file = new String(buff);
                if(file.contains("Il file"))
                    return "Orario della classe non ancora presente nell'archivio, mi dispiace";
                FileOutputStream outputStream;
                outputStream = openFileOutput(requested, Context.MODE_PRIVATE);
                outputStream.write(file.getBytes());
                outputStream.close();
                dataOutputStream.close();
            } catch (IOException e) {

                return "Orario della classe non ancora presente nell'archivio, mi dispiace";
            }
            return "Ok,orario Settimanale Scaricato";
        }

        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

    class TimeScheduleChanges extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... className) {

            try {
                String customIP = SettingsPanel.customIP;
                if(customIP == null)
                    customIP = SettingsPanel.defaultIP;
                String selected = className[0];
                Socket socket = new Socket(customIP, 1800);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                dataOutputStream.writeUTF(selected + "\n");

                String scheduleChanges = inputFromServer.readLine();
                scheduleChanges = scheduleChanges.replace("\n", "");
                return scheduleChanges;


            } catch (IOException e) {
                return "Orario della classe non ancora presente nell'archivio, mi dispiace";
            }

        }


        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute() {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }

}

