package com.example.blescanning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AttendErrorDialogFragment extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        return new AlertDialog.Builder(getActivity())
                .setPositiveButton("Okay", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        Intent dashIntent = new Intent(getContext(), DashboardActivity.class);
                        startActivity(dashIntent);
                    }
                })
                .setTitle("Error Confirming Attendance")
                .setMessage("There was an error recording your attendance. Please talk to" +
                        " your professor.")
                .create();
    }
}
