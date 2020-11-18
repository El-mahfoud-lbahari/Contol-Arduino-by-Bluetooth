package com.mahtiz.controlarduino;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ActivityMenu extends AppCompatActivity {
public static BluetoothAdapter myBluetoothAdapter;
public static BluetoothSocket btSocket=null;
public static BluetoothDevice  hc06=null;
private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        myBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
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
            } while (!btSocket.isConnected() && counter < 3);}
    }

    public void BtActive(View view) {
        if(!myBluetoothAdapter.isEnabled()){
            myBluetoothAdapter.enable();
        }else{
            Toast.makeText(getApplicationContext(),"bleutooth is already enable",Toast.LENGTH_LONG).show();
        }

    }

    public void BtDesactive(View view) {
        if(myBluetoothAdapter.isEnabled()){
            myBluetoothAdapter.disable();
        }else{
            Toast.makeText(getApplicationContext(),"bluetooth is already disable",Toast.LENGTH_LONG).show();
        }
    }

    public void connectToDev(View view) {
        Intent myIntent=new Intent(this,deviceBT.class);
        startActivity(myIntent);
    }

    public void connectVolts(View view) {
        Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
    }

    public void connectToServo(View view) {
            startActivity(new Intent(this, ActivityServo.class));
    }

    public void connectToUlra(View view) {
        Intent intent=new Intent(getApplicationContext(),ActivityUltra.class);
            startActivityForResult(intent,4);

    }

    public void connectToLed(View view) {
        Intent intent=new Intent(getApplicationContext(),ActivityLed.class);
            startActivity(intent);

    }

    public void readPDF(View view) {
     startActivity(new Intent(getApplicationContext(),srolActie.class));
    }
}
