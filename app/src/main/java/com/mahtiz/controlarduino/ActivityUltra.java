package com.mahtiz.controlarduino;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ActivityUltra extends AppCompatActivity {
    BluetoothAdapter myBluetoothAdapter=ActivityMenu.myBluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket=ActivityMenu.btSocket;
    String readMessage;
    ConnectedThread ct;
    InputStream mmInStream=null;
    String incomingMessage;
    StringBuilder messages;
    BluetoothDevice hc06;
    EditText limit;
    TextView distance;
    AdView adView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultra);
     /*List<String> testDeviceIds = Arrays.asList("731F5D44C95AE561D3A87855E0453B8A");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        distance=findViewById(R.id.distance);
       MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView=findViewById(R.id.bannerultra);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void StartRec(View view) {
        if(btSocket.isConnected())
        try {
            ct=new ConnectedThread(btSocket);
            ct.write();
            ct.run();
            Toast.makeText(getApplicationContext(),"begin connection",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"device no diconnect",Toast.LENGTH_LONG).show();
        }else{

            Toast.makeText(getApplicationContext(),"device no connected",Toast.LENGTH_LONG).show();
        }
    }

    public void stopConn(View view) {
        try{
        ct.interrupt();
        btSocket.close();
            Toast.makeText(getApplicationContext(),"connetion is stoped",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"connetion is stoped",Toast.LENGTH_LONG).show();
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //            creation of the connect thread
        ConnectedThread(BluetoothSocket socket) {
            Log.i("TAG", "ConnectedThread");

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void run() {
            byte[] buffer=new byte[480] ;
            float b;
            final Handler handler=new Handler();
            // Keep looping to listen for received messages

            try {
                b = mmInStream.read(buffer);
                //float b = (float) mmInStream.read();
                readMessage = new String(buffer);
                Log.i("TAG", "Bytes: " + b);
               // int x=readMessage.indexOf(".");
                int z=readMessage.indexOf(";");
               distance.setText(readMessage.substring(z+1,z+4)+"");

                Log.i("TAG", "run, readMessage => " + readMessage);
                handler.postDelayed(this,50);
            } catch (IOException e) {

            }
        }
        public void write() {
            //byte[] msgBuffer =(int) input;                                                          //converts entered String into bytes
            try {
                mmOutStream.write(2);                                                             //write bytes over BT connection via outstream
            } catch (IOException e) {

            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_design,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
