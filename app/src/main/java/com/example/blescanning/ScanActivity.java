package com.example.blescanning;

import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;



public class ScanActivity extends AppCompatActivity {

    private String className;
    private String classID;
//    private String classDbID;
    private TextView classNameView;
    private ParcelUuid lectureUUID;
    private Button startScanning;

    private RequestQueue myQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        classNameView = findViewById(R.id.className);
        startScanning = findViewById(R.id.startScanning);

        myQueue = SingletonAPICalls.getInstance(this).getRequestQueue();

        setIntentInstance();

        startScanning.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View w){

                easyToast("Scanning!! *beeb* *beeb* *beeb*");

            }
        });

    }

    private void setIntentInstance(){
        if(getIntent().hasExtra("classID") && getIntent().hasExtra("className")){
            className = getIntent().getStringExtra("className");
            classID = getIntent().getStringExtra("classID");

            classNameView.setText(className);
        }
    }

    private void easyToast(String string){
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ScanActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }


}
