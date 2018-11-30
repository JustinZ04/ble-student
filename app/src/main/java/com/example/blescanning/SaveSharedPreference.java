package com.example.blescanning;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;

public class SaveSharedPreference
{
    static final String PREF_STU_NID = "";
    static final String PREF_STU_UUID = "";

    static SharedPreferences getSharedPreferences(Context ctx)
    {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setStuUUID(Context ctx, String stuUUID)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STU_UUID, stuUUID);
        editor.commit();
    }

    public static void setStuNID(Context ctx, String stuNID)
    {
        Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_STU_NID, stuNID);
        editor.commit();
    }

    public static String getNID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_STU_NID, "");
    }

    public static String getUUID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_STU_UUID, "");
    }
}
