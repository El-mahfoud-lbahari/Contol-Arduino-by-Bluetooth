package com.mahtiz.controlarduino;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Set;

public class deviceBT extends AppCompatActivity {
    ListView listView;
    BluetoothDevice[] bluetoothDeviceDev;
    BluetoothDevice bldev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_b_t);
        listView = findViewById(R.id.listDev);
                Set < BluetoothDevice > bt = ActivityMenu.myBluetoothAdapter.getBondedDevices();
        String[] strings = new String[bt.size()];
        bluetoothDeviceDev=new BluetoothDevice[bt.size()];
        int index = 0;
        if (bt.size() > 0) {
            for (BluetoothDevice device : bt) {
                strings[index] = device.getName();
                bluetoothDeviceDev[index]=device;
                index++;
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
            listView.setAdapter(arrayAdapter);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bldev=bluetoothDeviceDev[position];
                String mac=bldev.getAddress();
                Intent data=new Intent(getApplicationContext(),ActivityMenu.class);
                data.putExtra("ad",mac);
                startActivity(data);

            }
        });
    }
}

