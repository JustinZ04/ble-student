package com.example.blescanning;

import android.os.ParcelUuid;

public class Constants
{
    public static final ParcelUuid UUID = ParcelUuid
        .fromString("cac426a3-344f-45c8-8819-ebcfe81e4b23");
    public static int ENABLE_BT_SUCCESS = 1;
    public static final String URL = "https://ble-attendance.herokuapp.com";
    public static final String REGISTER_STUDENT = "/api/students/createStudent/";
    public static final String GET_LECTURE_ID = "/api/students/viewClasses/";
    public static final String GET_LECTURE_INFO = "/api/classes/viewClass/";
    public static final String ADD_TO_CLASS = "/api/classes/addToClass";
    public static boolean LOGGED_IN = false;
}
