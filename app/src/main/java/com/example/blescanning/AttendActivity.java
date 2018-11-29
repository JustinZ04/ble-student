package com.example.blescanning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AttendActivity extends AppCompatActivity
{
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private static long SCAN_PERIOD = 15000;
    private Handler mHandler;
    //public ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar a = getSupportActionBar();
        a.setDisplayHomeAsUpEnabled(true);
    }

    public void setScan(View view)
    {
        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if(mBluetoothAdapter != null)
        {
            mHandler = new Handler();

            // Need to enable the BT adapter
            if(!mBluetoothAdapter.isEnabled())
            {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBTIntent, Constants.ENABLE_BT_SUCCESS);
            }
            else
            {
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

                if(mBluetoothLeScanner != null)
                {
                    startScan();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == Constants.ENABLE_BT_SUCCESS)
        {
            if(resultCode == RESULT_OK) {
                mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

                if (mBluetoothLeScanner != null)
                {
                    startScan();
                }
            }
        }
    }

    private void startScan()
    {
        if(mScanCallback == null)
        {
            mHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    stopScan();
                }
            }, SCAN_PERIOD);
        }

        mScanCallback = new CustomScanCallback();

        // Trying to create a progress dialog
        //dialog = new ProgressDialog(AttendActivity.this);
        //dialog.show();
        mBluetoothLeScanner.startScan(buildScanFilters(), buildScanSettings(), mScanCallback);

    }

    private void stopScan()
    {
        mBluetoothLeScanner.stopScan(mScanCallback);
        mScanCallback = null;
    }

    private List<ScanFilter> buildScanFilters()
    {
        List<ScanFilter> scanFilters = new ArrayList<>();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setServiceUuid(Constants.UUID);
        scanFilters.add(builder.build());

        return scanFilters;
    }

    private ScanSettings buildScanSettings()
    {
        ScanSettings.Builder builder = new ScanSettings.Builder();
        builder.setScanMode(ScanSettings.MATCH_MODE_AGGRESSIVE);

        return builder.build();
    }

    class CustomScanCallback extends ScanCallback
    {
        // I think this has to be here
        private ArrayList<BluetoothDevice> deviceList = new ArrayList<>();

        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            Toast.makeText(GlobalApp.getAppContext(), "Got a result", Toast.LENGTH_SHORT).show();
            super.onScanResult(callbackType, result);
            BluetoothDevice b = result.getDevice();
            String record = result.getScanRecord().toString();

            if(result.getScanRecord().getServiceUuids().contains(Constants.UUID) && !deviceList.contains(b))
            {
                Intent confirmAttend = new Intent(AttendActivity.this, ConfirmAttendActivity.class);
                stopScan();
                AttendActivity.this.startActivity(confirmAttend);
            }

            Log.d("device", "a result: " + record);
            Log.i("callbackType", String.valueOf(callbackType));
            Toast.makeText(GlobalApp.getAppContext(), result.getScanRecord().toString(), Toast.LENGTH_SHORT).show();

            if(!deviceList.contains(b))
            {
                deviceList.add(b);
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results)
        {
            Log.d("device", "In batch results");
            super.onBatchScanResults(results);

            for(ScanResult result : results)
            {
                BluetoothDevice b = result.getDevice();
                deviceList.add(b);
            }
        }

        @Override
        public void onScanFailed(int errorCode)
        {
            super.onScanFailed(errorCode);
            Toast.makeText(GlobalApp.getAppContext(),"Scan failed " + errorCode, Toast.LENGTH_LONG)
                    .show();
        }
    }
}


