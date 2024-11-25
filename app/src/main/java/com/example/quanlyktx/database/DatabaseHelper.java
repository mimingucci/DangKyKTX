package com.example.quanlyktx.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    //Thông số bảng account
    public static final String ACCOUNT_TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role"; // 0 sv 1 admin

    //Thông số bảng rooms
    public static final String ROOM_TABLE_NAME = "rooms";
    public static final String COLUMN_ROOMID = "roomid";
    public static final String COLUMN_ROOMNUMBER = "roomnumber";
    public static final String COLUMN_AREA = "area";
    public static final String COLUMN_FLOOR = "floor";
    public static final String COLUMN_ROOMPRICE = "price";
    public static final String COLUMN_ROOMTYPE = "roomtype";

    //Thông số bảng rentlist
    public static final String RENTLIST_TABLE_NAME = "rentlist";
    public static final String COLUMN_RENTID = "roomid";
    public static final String COLUMN_KYHOC = "kyhoc";
    public static final String COLUMN_NAMHOC = "namhoc";


    //
    public static final String PROFILE_TABLE_NAME = "profile";
    public static final String COLUMN_PROFILEID = "profileid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SDT = "sdt";
    public static final String COLUMN_EMAIL = "email";

    //request
    public static final String REQUEST_TABLE_NAME = "request";
    public static final String COLUMN_REQUESTID = "requestid";
    public static final String COLUMN_REQUESTTYPE = "requesttype"; // 1: gia hạn, 2:đk mới
    public static final String COLUMN_REQUESTSTATUS = "requeststatus";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        //Tạo bảng account
        String createTableQuery = "CREATE TABLE " + ACCOUNT_TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_ROLE + " INTEGER)";
        db.execSQL(createTableQuery);
        //Tạo bảng rooms
        String createTableQuery2 = "CREATE TABLE " + ROOM_TABLE_NAME + "("
                + COLUMN_ROOMID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ROOMNUMBER + " TEXT, "
                + COLUMN_AREA + " TEXT, "
                + COLUMN_FLOOR + " TEXT, "
                + COLUMN_ROOMTYPE + " INTEGER, "
                + COLUMN_ROOMPRICE + " INTEGER)";
        db.execSQL(createTableQuery2);
        //Tạo bảng rentlist
        String createTableQuery3 = "CREATE TABLE " + RENTLIST_TABLE_NAME + "("
                + COLUMN_RENTID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ROOMNUMBER + " TEXT, "
                + COLUMN_AREA + " TEXT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_KYHOC+ " TEXT, "
                + COLUMN_NAMHOC + " TEXT, "
                + "CONSTRAINT fk1 FOREIGN KEY(" + COLUMN_USERNAME + ") "
                + " REFERENCES " + ACCOUNT_TABLE_NAME + " (" + COLUMN_USERNAME + "), "
                + "CONSTRAINT fk2 FOREIGN KEY(" + COLUMN_ROOMNUMBER + ", " + COLUMN_AREA + ") "
                + " REFERENCES " + ROOM_TABLE_NAME + " (" + COLUMN_ROOMNUMBER + ", " + COLUMN_AREA  + ")  )";
        db.execSQL(createTableQuery3);
        //
        String createTableQuery5 = "CREATE TABLE " + PROFILE_TABLE_NAME + "("
                + COLUMN_PROFILEID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_SDT + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + "CONSTRAINT fk4 FOREIGN KEY(" + COLUMN_USERNAME + ") "
                + " REFERENCES " + ACCOUNT_TABLE_NAME + " (" + COLUMN_USERNAME + ")  )";
        db.execSQL(createTableQuery5);
        //
        String createTableQuery6 = "CREATE TABLE " + REQUEST_TABLE_NAME + "("
                + COLUMN_REQUESTID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_REQUESTTYPE + " INTEGER, "
                + COLUMN_ROOMNUMBER + " TEXT, "
                + COLUMN_AREA + " TEXT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_KYHOC+ " TEXT, "
                + COLUMN_NAMHOC + " TEXT, "
                + COLUMN_REQUESTSTATUS + " INTEGER, "
                + "CONSTRAINT fk5 FOREIGN KEY(" + COLUMN_ROOMNUMBER + ", " + COLUMN_AREA + ") "
                + " REFERENCES " + ROOM_TABLE_NAME + " (" + COLUMN_ROOMNUMBER + ", " + COLUMN_AREA + "), "
                + "CONSTRAINT fk6 FOREIGN KEY(" + COLUMN_USERNAME + ") "
                + " REFERENCES " + ACCOUNT_TABLE_NAME + " (" + COLUMN_USERNAME + ")  )";
        db.execSQL(createTableQuery6);
        //
        String createTableQuery7 = "CREATE TABLE session ( sessionid INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT ) ";
        db.execSQL(createTableQuery7);
        //
        String createTableQuery8 = "CREATE TABLE kh ( khid INTEGER PRIMARY KEY AUTOINCREMENT, kyhoc TEXT, namhoc TEXT ) ";
        db.execSQL(createTableQuery8);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ROOM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RENTLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REQUEST_TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + "session");
        onCreate(db);
    }
}
