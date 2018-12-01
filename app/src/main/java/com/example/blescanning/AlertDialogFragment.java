package com.example.blescanning;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment
{
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("I'm sure", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        // close the app here
                        getActivity().finishAndRemoveTask();
                    }
                })
                .setNegativeButton("Retry", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS,
                                REQUEST_LOCATION_PERMISSION);
                    }
                })
                .setTitle("Location Permission")
                .setMessage("The location permission is required for bluetooth" +
                        "to be used. You will not be able to use this app unless the" +
                        "location permission is granted.")
                .create();
    }
}
