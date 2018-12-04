package com.example.blescanning;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SaveSharedPreference
{
    static final String PREF_STU_NID = "";
    static final String PREF_STU_UUID = "xxx";

    static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setStuUUID(Context ctx, String stuUUID)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STU_UUID, stuUUID);
        editor.apply();
    }

    public static void setStuNID(Context ctx, String stuNID)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STU_NID, stuNID);
        //Log.v("nid", stuNID);
        editor.apply();
    }

    public static String getStuNID(Context ctx)
    {
        Log.v("nid", getSharedPreferences(ctx).getString(PREF_STU_NID, ""));
        return getSharedPreferences(ctx).getString(PREF_STU_NID, "");
    }

    public static String getUUID(Context ctx)
    {
        Log.v("nid", "uuid" + getSharedPreferences(ctx).getString(PREF_STU_UUID, ""));
        return getSharedPreferences(ctx).getString(PREF_STU_UUID, "");
    }
}
