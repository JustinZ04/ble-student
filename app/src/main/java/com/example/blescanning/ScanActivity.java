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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class ScanActivity extends AppCompatActivity
{

    private String className;
    private String classID;
    private String classDbID;
    private TextView classNameView;
    private ParcelUuid lectureUUID;
    private Button startScanning;
    private RequestQueue myQueue;
    private static long SCAN_PERIOD = 15000;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private ScanCallback mScanCallback;
    private Handler mHandler;
    private FragmentManager fm = getSupportFragmentManager();
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        classNameView = findViewById(R.id.className);
        startScanning = findViewById(R.id.startScanning);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        myQueue = SingletonAPICalls.getInstance(this).getRequestQueue();

        setIntentInstance();

        /*startScanning.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View w){

                easyToast("Scanning!! *beeb* *beeb* *beeb*");

            }
        });*/

    }

    public void startScan(View view)
    {
        String bleUrl = Constants.URL + Constants.GET_CURRENT_UUID + getIntent().getStringExtra("ProfNID");
        final StringRequest uuidRequest = new StringRequest(Request.Method.GET, bleUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                response = response.replaceAll("\"","");
                lectureUUID = ParcelUuid.fromString(response);
                //Toast.makeText(getApplicationContext(), lectureUUID.toString(), Toast.LENGTH_LONG).show();
                setupBluetooth();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        myQueue.add(uuidRequest);
    }


    private void setIntentInstance(){
        if(getIntent().hasExtra("classID") && getIntent().hasExtra("className"))
        {
            className = getIntent().getStringExtra("className");
            classID = getIntent().getStringExtra("classID");

            classNameView.setText(className);
        }
    }

    /*private void easyToast(String string)
    {
        final String String;

        String = string;

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(ScanActivity.this, String, Toast.LENGTH_SHORT).show();
            }
        });

        return;
    }*/

    private void setupBluetooth()
    {
        BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        if (mBluetoothAdapter != null)
        {
            mHandler = new Handler();

            // Need to enable the BT adapter
            if (!mBluetoothAdapter.isEnabled())
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

        mScanCallback = new ScanActivity.CustomScanCallback();

        // Trying to create a progress dialog
        //dialog = new ProgressDialog(AttendActivity.this);
        //dialog.show();
        progressBar.setVisibility(View.VISIBLE);
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
        builder.setServiceUuid(lectureUUID);
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
            //Toast.makeText(GlobalApp.getAppContext(), "Got a result", Toast.LENGTH_SHORT).show();
            super.onScanResult(callbackType, result);
            BluetoothDevice b = result.getDevice();
            String record = result.getScanRecord().toString();

            if(result.getScanRecord().getServiceUuids().contains(lectureUUID) && !deviceList.contains(b))
            {
                //Intent confirmAttend = new Intent(AttendActivity.this, ConfirmAttendActivity.class);
                stopScan();
                //AttendActivity.this.startActivity(confirmAttend);

                String attendUrl = Constants.URL + Constants.MARK_ATTENDED + SaveSharedPreference.getUUID(ScanActivity.this);

                JSONObject obj = new JSONObject();
                try
                {
                    obj.put("profUUID", lectureUUID);
                }
                catch (Exception e)
                {
                    return;
                }
                JsonObjectRequest setAttendanceInDb = new JsonObjectRequest(Request.Method.POST, attendUrl, obj, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        //Log.v("volley", response.toString());
                        if (response.has("Message"))
                        {
                            progressBar.setVisibility(View.GONE);
                            Button btn = findViewById(R.id.startScanning);
                            btn.setEnabled(false);
                            ConfirmAttendAlertDialogFragment confirmAttendAlertDialogFragment = new ConfirmAttendAlertDialogFragment();
                            confirmAttendAlertDialogFragment.show(fm, "attendanceConfirmed");
                        }
                        else
                        {
                            AttendErrorDialogFragment errorDialogFragment = new AttendErrorDialogFragment();
                            errorDialogFragment.show(fm, "attendanceError");
                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        //Toast.makeText(ScanActivity.this, "error in attendance request", Toast.LENGTH_LONG).show();
                        Log.v("volley", error.getCause().toString());
                    }
                });

                myQueue.add(setAttendanceInDb);
            }

            Log.d("device", "a result: " + record);
            Log.i("callbackType", String.valueOf(callbackType));
           // Toast.makeText(GlobalApp.getAppContext(), result.getScanRecord().toString(), Toast.LENGTH_SHORT).show();

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
