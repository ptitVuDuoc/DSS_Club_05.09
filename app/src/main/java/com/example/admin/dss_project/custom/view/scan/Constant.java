package com.example.admin.dss_project.custom.view.scan;

import android.os.Environment;

/**
 * Created by 1703s on 3/29/2018.
 */

public class Constant {

    // what in for handler mainActivity
    public static final int INT_RESULT_WHAT = 10;
    // tab select
    public static final int INT_TAB_HISTORY = 0;
    public static final int INT_TAB_SCAN = 1;
    public static final int INT_TAB_SETTING = 2;
    // request code for gallery
    public static final int INT_CAMERA_REQUEST_CODE = 100;
    // fps for camera min or max
    public static final int INT_CAMERA_MIN_FPS = 30000;
    public static final int INT_CAMERA_MAX_FPS = 30000;
    //rotate display preview
    public static final int INT_CAMERA_ROTATE = 90;
    // what for handler thread setup camera preview
    public static final int INT_CAMERA_WHAT_OPENED = 17;
    // times delay auto focus
    public static final long LONG_TIME_DELAY_AUTO_FOCUS = 1800;
    // times delay laser updates
    public static final long LONG_TIME_DELAY_LASER = 800;
    // per size of image
    public static final int INT_IMAGE_SIZE = 30;
    // per size of image with camera small
    public static final int INT_IMAGE_MAX_SIZE = 80;
    // max thread in pool not use
    public static final int INT_MAX_THREAD = 4;
    // times live for single thread auto focus
    public static final int INT_MAX_TIME_LIVE = 5;
    // max core for device
    public static final int INT_NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // limited load datas from sqli
    public static final int INT_LIMIT_PAGE = 10;
    // fist item load for sqli
    public static final int INT_DEFAULT_START = 0;
    // request code for gallery
    public static final int INT_REQUEST_LIB = 1000;
    // Database Version
    public static final int INT_DATABASE_VERSION = 1;
    // Error Flag when insert
    public static final int INT_ERROR_INSERT = -1;
    // what for handler History for result from preview
    public static final int INT_RESULT_UPDATE_HISTORY = 52;
    // what for handler Main Fragment change from preview to scan fragment
    public static final int INT_MSG_WHAT_MAIN = 18;
    // times delay vibration
    public static final int INT_VIBRATIONS_TIME = 50;
    // fixed size image when scan from gallery
    public static final int INT_FIXED_SIZE_SCAN = 100;
    // flag for checkbox change value to delete or share
    public static final int INT_FLAG_SHARE = 1;
    public static final int INT_FLAG_DELETE = 0;
    public static final int INT_FLAG_RESET = -1;
    // what for handler service for change sqli send to service
    public static final int INT_INSERT_SQL = 1;
    public static final int INT_STOP_SERVICE_SQL = -1;
    public static final int INT_DELETE_SQL = 0;
    // fixed max size finder on top preview
    public static final int INT_FIXED_MAX_SIZE = 800;
    public static final int INT_FIXED_MIN_SIZE = 250;
    // default values brightness camera (not use)
    public static final int INT_DEFAULT_BRIGHTNESS_CAMERA = 8;

    public static final int INT_DEFAULT_CAMERA_PREVIEW_HEIGHT = 1280;

    public static final int INT_DEFAULT_CAMERA_PREVIEW_WIDTH = 720;

    public static final int INT_RESTART_CAMERA = 120000;

    public static final String STRING_CAMERA_KEY_DATA = "camera.data";

    public static final String STRING_CAMERA_LOOPER = "camera.loop";

    public static final String STRING_CAMERA_KEY_COMPLETE = "camera.key.complete";

    public static final String STRING_CAMERA_KEY_RESULT = "camera.key.result";

    public static final String STRING_FRAGMENT_CAMERA_SCAN = "camera.scan";

    public static final String STRING_FRAGMENT_CAMERA_PREVIEW = "camera.preview";

    public static final String STRING_ACTION_TAB_SELECTED = "onTabSelected";
    // format date time
    public static final String STRING_DATE_TIME_FORMART = "hh:mm dd/MM/yyyy";
    // table name
    public static final String STRING_TABLE_NAME = "imageBarCode";
    // column for database
    public static final String STRING_COLUMN_ID = "id";
    public static final String STRING_COLUMN_DATA = "data";
    public static final String STRING_COLUMN_IMAGE_DATA = "imageData";
    public static final String STRING_COLUMN_TIMESTAMP = "timestamp";
    // thread handler service name
    public static final String STRING_HANDLER_THREAD_SQLI_SERVICE = "service.sqli";

    // Create table SQL query
    public static final String STRING_CREATE_TABLE =
            "CREATE TABLE " + STRING_TABLE_NAME + "("
                    + STRING_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + STRING_COLUMN_DATA + " TEXT,"
                    + STRING_COLUMN_IMAGE_DATA + " TEXT,"
                    + STRING_COLUMN_TIMESTAMP + " TEXT"
                    + ")";

    // Database Name
    public static final String STRING_DATABASE_NAME =  Environment.getExternalStorageDirectory().getAbsolutePath() +"/.QrCode/image_barcode.db";
    // names handle_thread
    public static final String STRING_HANDLE_THREAD_HISTORY = "handle.hisoty";
    public static final String STRING_HANDLE_THREAD_MAIN = "handle.main";
    // key ImageBarcode from scan to history
    public static final String STRING_FRAGMENT_HISTORY  = "main.history.fragment.tag";

}
