package com.example.blescanning;

class Constants
{
    static int ENABLE_BT_SUCCESS = 1;
    static final String URL = "https://ble-attendance.herokuapp.com";
    static final String REGISTER_STUDENT = "/api/students/createStudent/";
    static final String GET_LECTURE_ID = "/api/students/viewClasses/";
    static final String GET_LECTURE_INFO = "/api/classes/viewClass/";
    static final String ADD_TO_CLASS = "/api/classes/addToClass";
    static final String GET_CURRENT_UUID = "/api/professors/bleuuid/";
    static final String MARK_ATTENDED = "/api/students/markHere/";
    static boolean LOGGED_IN = false;
}
