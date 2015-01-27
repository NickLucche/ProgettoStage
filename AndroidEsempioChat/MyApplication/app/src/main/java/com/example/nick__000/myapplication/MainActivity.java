package com.example.nick__000.myapplication;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class MainActivity extends ActionBarActivity {





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView)findViewById(R.id.HistoryT);
        tv.setMovementMethod(new ScrollingMovementMethod());

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


       // Button send1 = (Button)findViewById(R.id.button);


        Thread t = new Thread (new Server ());
        t.start();
    }

    public void onPippo(View v){

        System.out.println ("Sent message");
    }

    public void onClick(View v)
    {
        final EditText et = (EditText)findViewById(R.id.editText);
        Editable e = et.getText();
        String s = e.toString();

        //DatagramSocket socket;
        try
        {
            DatagramSocket socket = new DatagramSocket ();

            byte[] buf = new byte[256];

            //String outputLine = s; //txArea.getText ();

            buf = s.getBytes ();

            InetAddress address = InetAddress.getByName ("146.48.106.198");
            DatagramPacket packet = new DatagramPacket (buf, buf.length, address,6789);
            System.out.println ("About to send message");
            socket.send (packet);
            System.out.println ("Sent message");
        }
        catch (SocketException e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (UnknownHostException e2)
        {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        catch (IOException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }

    }




    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onClickMethod(View v){

        Button btn = (Button) v;
        EditText input = (EditText)findViewById(R.id.editText);

        TextView history = (TextView) findViewById(R.id.HistoryT);
        history.append("Me: "+input.getText()+"\n");

        input.setText("");

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
    class Server implements Runnable{
    DatagramPacket packet;
    DatagramSocket socket;
    String str;
        public void run(){
            DatagramPacket packet;
            byte[] buf = new byte[256];
            //System.out.println ("Thread running");

            try
            {
                socket = new DatagramSocket (6789);

                while (true)
                {
                    final TextView t = (TextView)findViewById(R.id.HistoryT);

                    packet = new DatagramPacket (buf, buf.length);
                    socket.receive (packet);
                    //System.out.println ("Received packet");
                    String s = new String (packet.getData());

                    CharSequence cs = t.getText ();
                    str = cs + "\r\n" +  s;

                    t.post(new Runnable()
                           {
                               public void run()
                               {
                                   t.setText(str);
                               }
                           }
                    );
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
