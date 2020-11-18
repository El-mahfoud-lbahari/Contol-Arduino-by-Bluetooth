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
import android.widget.Button;
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

public class ActivityLed extends AppCompatActivity {
    BluetoothAdapter myBluetoothAdapter=ActivityMenu.myBluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket=ActivityMenu.btSocket;
    String readMessage;
    ConnectedThread ct;
    InputStream mmInStream=null;
    BluetoothDevice hc06;
    Button ledOn,ledOff;
    int send=0;
    AdView adView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
         /* List<String> testDeviceIds = Arrays.asList("731F5D44C95AE561D3A87855E0453B8A");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
       // Toolbar tl=findViewById(R.id.toolbar);
        //setActionBar(tl);
        ledOn=findViewById(R.id.onLed);
        ledOff=findViewById(R.id.offLed);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView=findViewById(R.id.bannerled);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
        ledOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                send=1;
                ct=new ConnectedThread(btSocket);
                ct.write();
                    Toast.makeText(getApplicationContext(),"LED is ON",Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"device non connected",Toast.LENGTH_LONG).show();
                }
            }
        });
        ledOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ct=new ConnectedThread(btSocket);
                    send=0;
                    ct.write();
                    Toast.makeText(getApplicationContext(),"LED is OFF",Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"device non connected",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
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
                Log.i("TAG", "run, readMessage => " + readMessage);
                handler.postDelayed(this,50);
            } catch (IOException e) {

            }
        }
        public void write() {
            //byte[] msgBuffer =(int) input;                                                          //converts entered String into bytes
            try {
                mmOutStream.write(send);                                                             //write bytes over BT connection via outstream
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
