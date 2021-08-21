package com.tranxit.enterprise.common;

import android.Manifest;

public class Constants {
    public static final String SEARCH_URL = "nearbysearch/json?key=AIzaSyCRLa4LQZWNQBcjCYcIVYA45i9i8zfClqc&sensor=true&radius=50";
    //Camera request
    public static final int CAMERA_REQUEST_ID = 333;
    public static final int STORAGE_REQUEST_ID = 444;
    public static final int REQUEST_CAMERA = 0, SELECT_FILE = 1;



    //Camera request
    public static final String[] MULTIPLE_PERMISSION = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int RC_MULTIPLE_PERMISSION_CODE = 12224;
    public static int APP_REQUEST_CODE = 99;

    public static final int RC_CALL_PHONE = 123;

    public static String Currency = "₹";





}
