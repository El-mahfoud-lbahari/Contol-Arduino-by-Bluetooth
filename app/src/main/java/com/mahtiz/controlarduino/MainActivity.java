package com.mahtiz.controlarduino;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //public static
    BluetoothAdapter myBluetoothAdapter=ActivityMenu.myBluetoothAdapter;
    ArrayList<Entry> dataVals=new ArrayList<Entry>();
    float incr=0;
    Boolean t=true;
    LineChart ln;
    private LineGraphSeries<DataPoint> series ;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView valeur;
    BluetoothSocket btSocket=ActivityMenu.btSocket;
     Button stop;
    String readMessage;
    ConnectedThread ct;
    InputStream mmInStream=null;
    String incomingMessage;
    StringBuilder messages;
    Button star;
    BluetoothDevice hc06;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       List<String> testDeviceIds = Arrays.asList("731F5D44C95AE561D3A87855E0453B8A");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
       /* AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");*/

        mAdView = findViewById(R.id.bannervolt);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        /*MobileAds.initialize(this, getString(R.string.banner_unit_id));
        AdView mAdView = findViewById(R.id.bannervolt);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        ln = findViewById(R.id.lineChart);
        star=findViewById(R.id.star);
        stop=findViewById(R.id.stop);
        valeur=findViewById(R.id.valeur);
        /* btSocket = null;
        if (getIntent() != null && getIntent().hasExtra("ad")) {
            String mac=getIntent().getStringExtra("ad");
            hc06 = myBluetoothAdapter.getRemoteDevice(mac);
            Toast.makeText(getApplicationContext(),hc06.getName(),Toast.LENGTH_LONG).show();
            int counter = 0;
            do {
                try {
                    btSocket = hc06.createRfcommSocketToServiceRecord(MY_UUID);
                    myBluetoothAdapter.cancelDiscovery();
                    btSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!btSocket.isConnected() && counter < 3);
        }*/
        initGraph();
        /*star.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                final Handler handler=new Handler();
                Runnable runnable=new Runnable() {
                    @Override
                    public void run() {
                        ListenInput.start();
                       msg.setText(messages);
                        handler.postDelayed(this,500);
                    }
                };
                runnable.run();
            }
        });*/
        /*
        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(48);
        } catch (IOException e) {
            e.printStackTrace();
        }
/*
        InputStream inputStream = null;
        OutputStream outputStream = null;
        msg.setText(" ");
        try {
            inputStream = btSocket.getInputStream();
            inputStream.skip(inputStream.available());
            while (btAdapter.isEnabled()) {
                byte b = (byte) inputStream.read();
                System.out.println((long) b);
                Log.i("ttt", String.valueOf((float) b));
                msg.setText((float) b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(btSocket.isConnected()){
            try {
                btSocket.close();
                System.out.println(btSocket.isConnected());
                Log.i("ttt", String.valueOf(btSocket.isConnected()));
            } catch (IOException e) {
                e.printStackTrace();*/

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void StartRec(View view) {
        if(ln.isActivated()){
            Toast.makeText(getApplicationContext(),"connetion already activate",Toast.LENGTH_LONG).show();
        }else{
      if(btSocket.isConnected()) {ct=new ConnectedThread(btSocket);
            ct.write();
            ct.run();
            t=false;
           Toast.makeText(getApplicationContext(),"begin connection",Toast.LENGTH_LONG).show();
       }else{
            Toast.makeText(getApplicationContext(),"device no diconnect",Toast.LENGTH_LONG).show();
        }}
    }
    public void stopBt(View view) throws IOException {
        if(btSocket.isConnected()) {
            btSocket.close();
        }else{
            Toast.makeText(getApplicationContext(),"device is already diconnect",Toast.LENGTH_LONG).show();
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
                    int x=readMessage.indexOf(";");
                   valeur.setText(readMessage.substring(x+1,x+5));
                    float y=Float.parseFloat(readMessage.substring(x+1,x+5));
                    float z=(float)0.05*incr;
                        dataVals.add(new Entry(z, y));
                        incr++;
                        LineDataSet lineDataSet1 = new LineDataSet(dataVals, "dataSet1");
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(lineDataSet1);
                    LineData data=new LineData(dataSets);
                    lineDataSet1.setDrawCircleHole(false);
                    lineDataSet1.setDrawCircles(false);
                    lineDataSet1.setDrawValues(false);
                    lineDataSet1.setLineWidth(4);
                    ln.setData(data);
                    ln.invalidate();
                    if(incr>=300){
                        //dataSets.clear();
                        ln.clear();
                        //data.clearValues();
                        lineDataSet1.clear();
                        incr=0;
                    }
                    // Log readMessage
                   // Log.i("TAG", "run, readMessage => " + readMessage);
                    handler.postDelayed(this,50);
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"problem in connetion",Toast.LENGTH_LONG).show();
                    ct.isInterrupted();
                }

          //  }
        }
        public void write() {
            //byte[] msgBuffer =(int) input;                                                          //converts entered String into bytes
            try {
                mmOutStream.write(1);                                                             //write bytes over BT connection via outstream
            } catch (IOException e) {

            }
        }
    }
    public void initGraph(){
        //ln=findViewById(R.id.lineChart);
        ln.getDescription().setEnabled(true);
        // enable touch gestures
        ln.setTouchEnabled(true);

        // enable scaling and dragging
        ln.setDragEnabled(true);
        ln.setScaleEnabled(true);
        ln.setDrawGridBackground(false);
        ln.setPinchZoom(true);
        // set an alternative background color
        ln.setBackgroundColor(Color.WHITE);

        /*LineDataSet lineDataSet1=new LineDataSet(dataValue1(),"dataSet1");
        ArrayList<ILineDataSet> dataSets=new ArrayList<>();
        dataSets.add(lineDataSet1);*/
        //LineData data=new LineData(dataSets);
        LineData data=new LineData();
        data.setValueTextColor(Color.WHITE);
        ln.setData(data);
        ln.invalidate();

// get the legend (only possible after setting data)
        Legend l = ln.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        XAxis xl = ln.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis =ln.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);


        YAxis rightAxis = ln.getAxisRight();
        rightAxis.setEnabled(false);

        ln.getAxisLeft().setDrawGridLines(false);
        ln.getXAxis().setDrawGridLines(false);
        ln.setDrawBorders(false);

        Description description=new Description();
        description.setText("Voltage");
        description.setTextColor(Color.BLUE);
        description.setTextSize(14);
        ln.setDescription(description);
    }
    /*Thread ListenInput=new Thread(){
        @Override
        public void run() {
            try {
                mmInStream=btSocket.getInputStream();
                byte[] buffer = new byte[4];
                int bytes;
                while (true) {
                    // Read from the InputStream
                    try {
                        if(mmInStream==null)
                        {
                            Log.d("","InputStream is null");
                        }
                        bytes = mmInStream.read(buffer);
                        incomingMessage = new String(buffer, 0, bytes);
                        messages.append(incomingMessage);





                    } catch (IOException e) {


                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4 && resultCode==9){
           String mac=data.getStringExtra("ad");
            hc06 = myBluetoothAdapter.getRemoteDevice(mac);
            Toast.makeText(getApplicationContext(),hc06.getName(),Toast.LENGTH_LONG).show();
            int counter = 0;
            do {
                try {
                    btSocket = hc06.createRfcommSocketToServiceRecord(MY_UUID);
                    myBluetoothAdapter.cancelDiscovery();
                    btSocket.connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                counter++;
            } while (!btSocket.isConnected() && counter < 3);
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_design,menu);
        return super.onCreateOptionsMenu(menu);// la fonction  hhhakpodhjjdhj
    }
}
