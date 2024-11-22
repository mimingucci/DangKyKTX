package com.example.uddd_nhom11.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.uddd_nhom11.entity.Account;
import com.example.uddd_nhom11.entity.Profile;
import com.example.uddd_nhom11.entity.Rent;
import com.example.uddd_nhom11.entity.Request;
import com.example.uddd_nhom11.entity.Room;

import java.util.ArrayList;
import java.util.List;

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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ROOM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RENTLIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROFILE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REQUEST_TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + "session");
        onCreate(db);
    }

    public Cursor getAccountByUsernameAndPasswordCursor(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        return db.query(ACCOUNT_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    public Cursor getAccountByUsernameCursor(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        return db.query(ACCOUNT_TABLE_NAME, null, selection, selectionArgs, null, null, null);
    }
    public Cursor getRoomByRoomNumberAndAreaCursor(String roomnumber, String area) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(ROOM_TABLE_NAME, null, COLUMN_ROOMNUMBER + " = ? AND " + COLUMN_AREA + " = ?", new String[]{roomnumber, area}, null, null, null);
    }
    public Cursor getRentByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(RENTLIST_TABLE_NAME, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }
    public Cursor getProfileInfo(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(PROFILE_TABLE_NAME, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }
    public Cursor getRenewRequestByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(REQUEST_TABLE_NAME, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }
    public Cursor getSession(){
        SQLiteDatabase db = getReadableDatabase();
        return db.query("session", null, null, null, null, null, null);
    }
    public Cursor getRequestList(){
        SQLiteDatabase db = getReadableDatabase();
        return db.query(REQUEST_TABLE_NAME, null, null , null, null, null, null);
    }


    public void updateAccount (Account a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, a.getUsername());
        cv.put(COLUMN_PASSWORD, a.getPassword());
        cv.put(COLUMN_ROLE, a.getRole());
        db.update(ACCOUNT_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {a.getUsername()});
    }
    public void updateProfile (Profile p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, p.getUsername());
        cv.put(COLUMN_SDT, p.getSdt());
        cv.put(COLUMN_EMAIL, p.getEmail());
        cv.put(COLUMN_NAME, p.getName());
        db.update(PROFILE_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {p.getUsername()});
    }



    public void addAccountToDatabase (Account a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, a.getUsername());
        cv.put(COLUMN_PASSWORD, a.getPassword());
        cv.put(COLUMN_ROLE, a.getRole());
        db.insert(ACCOUNT_TABLE_NAME, null, cv);
        db.update(ACCOUNT_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {a.getUsername()});
    }
    public void addRoomToDatabase(Room r){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ROOMNUMBER, r.getRoomnumber());
        cv.put(COLUMN_AREA, r.getArea());
        cv.put(COLUMN_FLOOR, r.getFloor());
        cv.put(COLUMN_ROOMTYPE, r.getRoomtype());
        cv.put(COLUMN_ROOMPRICE, r.getRoomprice());
        db.insert(ROOM_TABLE_NAME, null, cv);
        db.update(ROOM_TABLE_NAME, cv, COLUMN_ROOMNUMBER + " = ?", new String[] {r.getRoomnumber()});
    }
    public void addARentToDatabase(Rent r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, r.getUsername());
        cv.put(COLUMN_ROOMNUMBER, r.getRoomnumber());
        cv.put(COLUMN_AREA, r.getRoomarea());
        cv.put(COLUMN_KYHOC, r.getKyhoc());
        cv.put(COLUMN_NAMHOC, r.getNamhoc());
        db.insert(RENTLIST_TABLE_NAME, null, cv);
        db.update(RENTLIST_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {r.getUsername()});
    }

    public void addAProfile(Profile p){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, p.getUsername());
        cv.put(COLUMN_SDT, p.getSdt());
        cv.put(COLUMN_EMAIL, p.getEmail());
        cv.put(COLUMN_NAME, p.getName());
        db.insert(PROFILE_TABLE_NAME, null, cv);
        db.update(PROFILE_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {p.getUsername()});
    }
    public void addARenewRequest(Request r){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_REQUESTTYPE, r.getRequesttype());
        cv.put(COLUMN_USERNAME, r.getUsername());
        cv.put(COLUMN_ROOMNUMBER, r.getRoomnumber());
        cv.put(COLUMN_AREA, r.getArea());
        cv.put(COLUMN_REQUESTSTATUS, r.getRequeststatus());
        cv.put(COLUMN_NAMHOC, r.getNamhoc());
        cv.put(COLUMN_KYHOC, r.getKyhoc());
        db.insert(REQUEST_TABLE_NAME, null, cv);
        db.update(REQUEST_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {r.getUsername()});
    }
    public void addSession(String username) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        db.insert("session", null, cv);
    }
    public void changeSession(String username) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, username);
        db.update("session", cv, null, null);
    }
    public void addTerm (String kyhoc, String namhoc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("kyhoc", kyhoc);
        cv.put("namhoc", namhoc);
        db.insert("kh", null, cv);
    }
    public void updateRenewForRentlist(Rent r) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USERNAME, r.getUsername());
        cv.put(COLUMN_ROOMNUMBER, r.getRoomnumber());
        cv.put(COLUMN_AREA, r.getRoomarea());
        cv.put(COLUMN_KYHOC, r.getKyhoc());
        cv.put(COLUMN_NAMHOC, r.getNamhoc());
        db.update(RENTLIST_TABLE_NAME, cv, COLUMN_USERNAME + " = ?", new String[] {r.getUsername()});
    }
    public void deleteRequest(String username) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(REQUEST_TABLE_NAME, COLUMN_USERNAME + " = ?", new String[]{username});
    }
    public void changeTerm (String kyhoc, String namhoc) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("kyhoc", kyhoc);
        cv.put("namhoc", namhoc);
        db.update("kh", cv, null, null);
    }
    public void deleteRoomFromDatabase (String roomnumber, String area){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ROOMNUMBER + " = ? AND " + COLUMN_AREA + " = ?";
        String[] whereArgs = {roomnumber, area};
        db.delete(ROOM_TABLE_NAME, whereClause, whereArgs);
    }

    public List<Room> getRoom () {
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + ROOM_TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int roomIdIndex = cursor.getColumnIndex(COLUMN_ROOMID);
        int roomNumberIndex = cursor.getColumnIndex(COLUMN_ROOMNUMBER);
        int areaIndex = cursor.getColumnIndex(COLUMN_AREA);
        int floorIndex = cursor.getColumnIndex(COLUMN_FLOOR);
        int roomTypeIndex = cursor.getColumnIndex(COLUMN_ROOMTYPE);
        int roomPriceIndex = cursor.getColumnIndex(COLUMN_ROOMPRICE);

        if (cursor.moveToFirst()) {
            do {
                int roomId = (roomIdIndex >= 0) ? cursor.getInt(roomIdIndex) : -1;
                String roomNumber = (roomNumberIndex >= 0) ? cursor.getString(roomNumberIndex) : "";
                String area = (areaIndex >= 0) ? cursor.getString(areaIndex) : "";
                String floor = (floorIndex >= 0) ? cursor.getString(floorIndex) : "";
                int roomType = (roomTypeIndex >= 0) ? cursor.getInt(roomTypeIndex) : 0;
                int roomPrice = (roomPriceIndex >= 0) ? cursor.getInt(roomPriceIndex) : 0;

                Room room = new Room(roomNumber, area, floor, roomPrice, roomType);
                roomList.add(room);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return roomList;
    }

    public List<Room> getRoomByArea (String area){
        List<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + ROOM_TABLE_NAME + " WHERE " + COLUMN_AREA + " = ?";
        String[] selectionArgs = {area};
        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        int roomIdIndex = cursor.getColumnIndex(COLUMN_ROOMID);
        int roomNumberIndex = cursor.getColumnIndex(COLUMN_ROOMNUMBER);
        int floorIndex = cursor.getColumnIndex(COLUMN_FLOOR);
        int roomTypeIndex = cursor.getColumnIndex(COLUMN_ROOMTYPE);
        int roomPriceIndex = cursor.getColumnIndex(COLUMN_ROOMPRICE);

        if (cursor.moveToFirst()) {
            do {
                int roomId = (roomIdIndex >= 0) ? cursor.getInt(roomIdIndex) : -1;
                String roomNumber = (roomNumberIndex >= 0) ? cursor.getString(roomNumberIndex) : "";
                String floor = (floorIndex >= 0) ? cursor.getString(floorIndex) : "";
                int roomType = (roomTypeIndex >= 0) ? cursor.getInt(roomTypeIndex) : 0;
                int roomPrice = (roomPriceIndex >= 0) ? cursor.getInt(roomPriceIndex) : 0;

                Room room = new Room(roomNumber, area, floor, roomPrice, roomType);
                roomList.add(room);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return roomList;
    }

    public void updateRoom (Room r){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ROOMNUMBER, r.getRoomnumber());
        cv.put(COLUMN_AREA, r.getArea());
        cv.put(COLUMN_FLOOR, r.getFloor());
        cv.put(COLUMN_ROOMTYPE, r.getRoomtype());
        cv.put(COLUMN_ROOMPRICE, r.getRoomprice());
        db.update(ROOM_TABLE_NAME, cv, COLUMN_ROOMNUMBER + " = ? AND " + COLUMN_AREA + " = ?", new String[]{r.getRoomnumber(), r.getArea()});
    }

    public List<Account> getRegistrantsByRoom (String roomNumber, String area){
        List<Account> registrants = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + RENTLIST_TABLE_NAME + " r "
                + "INNER JOIN " + ACCOUNT_TABLE_NAME + " a "
                + "ON r." + COLUMN_USERNAME + " = a." + COLUMN_USERNAME + " "
                + "WHERE r." + COLUMN_ROOMNUMBER + " = ? AND r." + COLUMN_AREA + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{roomNumber, area});

        int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
        int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
        int roleIndex = cursor.getColumnIndex(COLUMN_ROLE);

        if (cursor.moveToFirst()) {
            do {
                if (usernameIndex >= 0 && passwordIndex >= 0 && roleIndex >= 0) {
                    String username = cursor.getString(usernameIndex);
                    String password = cursor.getString(passwordIndex);
                    int role = cursor.getInt(roleIndex);

                    Account account = new Account(username, password, role);
                    registrants.add(account);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return registrants;
    }


    public void removeRegistrant (Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_USERNAME + " = ?";
        String[] whereArgs = {account.getUsername()};
        db.delete(RENTLIST_TABLE_NAME, whereClause, whereArgs);
    }

    public Boolean checkRoomExist (String roomNumber, String roomArea){
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ROOMNUMBER + " = ? AND " + COLUMN_AREA + " = ?";
        String[] selectionArgs = {roomNumber, roomArea};
        Cursor cursor = db.query(ROOM_TABLE_NAME, null, selection, selectionArgs, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void addRoom (Room r){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ROOMNUMBER, r.getRoomnumber());
        cv.put(COLUMN_AREA, r.getArea());
        cv.put(COLUMN_FLOOR, r.getFloor());
        cv.put(COLUMN_ROOMTYPE, r.getRoomtype());
        cv.put(COLUMN_ROOMPRICE, r.getRoomprice());
        db.insert(ROOM_TABLE_NAME, null, cv);
    }

    @SuppressLint("Range")
    public String getNameByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(PROFILE_TABLE_NAME, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
        String res = "";
        if (cursor.moveToFirst()) res = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
        cursor.close();
        return res;
    }
    public boolean updateRequestStatus(int requestId, int newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_REQUESTSTATUS, newStatus);
        int result = db.update(REQUEST_TABLE_NAME, contentValues, COLUMN_REQUESTID + " = ?", new String[]{String.valueOf(requestId)});
        return result > 0;
    }
    // Khanh code databasse o day
    public int updatePasswordAccount(Account c, int id) {
        SQLiteDatabase db = getWritableDatabase();
        String updateQuery = "UPDATE " + ACCOUNT_TABLE_NAME + " SET " + COLUMN_PASSWORD + " = '" + c.getPassword() + "' WHERE " + COLUMN_ID + " = " + id;
        db.execSQL(updateQuery);
        db.close();
        return 1;
    }
    public Cursor getAccountInfoById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(ACCOUNT_TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
    }


    public int countPeopleByRoomNumberAndArea(String roomNumber, String area) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ROOMNUMBER + " = ? AND " + COLUMN_AREA + " = ?";
        String[] selectionArgs = {roomNumber, area};
        Cursor cursor = db.query(RENTLIST_TABLE_NAME, null, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    @SuppressLint("Range")
    public String getCurrentTerm() {
        SQLiteDatabase db = getReadableDatabase();
        String term = "";
        Cursor cursor = db.query("kh", null, null, null, null, null, null);
        if (cursor.moveToFirst()) term = cursor.getString(cursor.getColumnIndex("kyhoc"));
        cursor.close();
        db.close();
        return term;
    }
    @SuppressLint("Range")
    public String getCurrentYear() {
        SQLiteDatabase db = getReadableDatabase();
        String year = "";
        Cursor cursor = db.query("kh", null, null, null, null, null, null);
        if (cursor.moveToFirst()) year = cursor.getString(cursor.getColumnIndex("namhoc"));
        cursor.close();
        db.close();
        return year;

    }

    // Các khai báo và phương thức khác của lớp DatabaseHelper
    public void addARegisterRequest(Request request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, request.getUsername());
        values.put(COLUMN_ROOMNUMBER, request.getRoomnumber());
        values.put(COLUMN_AREA, request.getArea());
        values.put(COLUMN_KYHOC, request.getKyhoc());
        values.put(COLUMN_NAMHOC, request.getNamhoc());
        values.put(COLUMN_REQUESTSTATUS, request.getRequeststatus());
        values.put(COLUMN_REQUESTTYPE, request.getRequesttype()); // Loại phiếu đăng ký

        // Chèn một dòng mới vào bảng Requests
        long result = db.insert(REQUEST_TABLE_NAME, null, values);
        if (result == -1) {
            // Nếu chèn không thành công, in ra log hoặc thực hiện xử lý khác
            Log.e("DatabaseHelper", "Failed to insert request");
        } else {
            // Nếu chèn thành công, in ra log hoặc thực hiện xử lý khác
            Log.d("DatabaseHelper", "Request inserted successfully");
        }

        db.close();
    }

    //.......
}

