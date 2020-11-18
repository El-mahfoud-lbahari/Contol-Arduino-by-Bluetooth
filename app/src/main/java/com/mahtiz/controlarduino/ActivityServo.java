package com.mahtiz.controlarduino;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toolbar;

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

public class ActivityServo extends AppCompatActivity {
    BluetoothAdapter myBluetoothAdapter=ActivityMenu.myBluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothSocket btSocket=ActivityMenu.btSocket;
    String readMessage;
    ConnectedThread ct;
    InputStream mmInStream=null;
    BluetoothDevice hc06;
    Button servoA,servoB,servoC,servoD;
    SeekBar servoSek;
    int angle;
    Activity myActivity;
    Toolbar toolbar;
    AdView adView;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servo);
          /*List<String> testDeviceIds = Arrays.asList("731F5D44C95AE561D3A87855E0453B8A");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView=findViewById(R.id.bannerservo);
        AdRequest adRequest=new AdRequest.Builder().build();
        adView.loadAd(adRequest);*/
        //toolbar=findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        myActivity=this;
        servoA=findViewById(R.id.servoA);
        servoB=findViewById(R.id.servoB);
        servoC=findViewById(R.id.servoC);
        servoD=findViewById(R.id.servoD);
        servoSek=findViewById(R.id.servoSeek);


        servoA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct=new ConnectedThread(btSocket);
                angle=0;
                ct.write();
            }
        });
        servoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct=new ConnectedThread(btSocket);
                angle=30;
                ct.write();
            }
        });
        servoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct=new ConnectedThread(btSocket);
                angle=90;
                ct.write();
            }
        });
        servoD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct=new ConnectedThread(btSocket);
                angle=180;
                ct.write();
            }
        });
        servoSek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ct=new ConnectedThread(btSocket);
                angle=progress;
                ct.write();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ct=new ConnectedThread(btSocket);
                      angle=seekBar.getProgress();
                      ct.write();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ct=new ConnectedThread(btSocket);
                angle=seekBar.getProgress();
                ct.write();
            }
        });
    }

    public void servoo(View view) {
        ct.write();
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
            mmOutStream.write(angle);                                                             //write bytes over BT connection via outstream
        } catch (IOException e) {
        }
    }
}
/*class mySeekThread extends Thread{
   SeekBar servoSek=findViewById(R.id.servoSeek);
    @Override
    public void run() {
/*myActivity.runOnUiThread(new Runnable() {
    @Override
    public void run() {
    }
});
        servoSek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                angle=progress;
                ct.write();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                  angle=seekBar.getProgress();
                ct.write();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                angle=seekBar.getProgress();
                ct.write();
            }
        });
    }
}*/

}

