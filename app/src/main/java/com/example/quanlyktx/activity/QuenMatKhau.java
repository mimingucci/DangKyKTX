package com.example.uddd_nhom11.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.uddd_nhom11.R;
import com.example.uddd_nhom11.database.DatabaseHelper;
import com.example.uddd_nhom11.entity.Account;

public class QuenMatKhau extends AppCompatActivity {
    Button btnXacNhanQMK;
    EditText edtTenDN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quen_mat_khau);
 // Xóa database
//        // Lấy context của ứng dụng
//        Context context = getApplicationContext();
//
//        // Tên của cơ sở dữ liệu bạn muốn xóa
//        String dbName = "mydatabase.db";
//

//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.onUpgrade(db, 2, 3);
        //initDatabase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnXacNhanQMK = findViewById(R.id.btnXacNhanQMK);
        edtTenDN = findViewById(R.id.edtTenDN);
        btnXacNhanQMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String username = edtTenDN.getText().toString();
                int idAcc = getIdByUserName( username);
                if(idAcc> -1) {
                    Account acc = getAccById(idAcc);
                    String phoneNumber = getPhoneNumberByUserName(username);
                    if (phoneNumber.length() > 0) {
                        Log.d("Thông tin acc",  acc.getUsername());

                        // Activity đầu tiên
                        Intent intent = new Intent(QuenMatKhau.this, otpForm.class);
                        Bundle myBundle = new Bundle();
                        myBundle.putSerializable("account", acc);
                        myBundle.putInt("id", idAcc);
                        myBundle.putString("phoneNumber", phoneNumber);
                        intent.putExtra("myPackage", myBundle);

                        startActivity(intent);
                    }

                }

            }
        });
    }
    //
    public String getPhoneNumberByUserName(String username)
    {
        try
        {

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.getProfileInfo(username);
            if (cursor != null)
            {
                // Kiểm tra xem Cursor có chứa bất kỳ hàng nào không
                if (cursor.moveToFirst())
                {
                    String phoneNumber = cursor.getString(3);
                    return phoneNumber;
                } else
                {
                    Toast.makeText(QuenMatKhau.this, "Không tìm thấy số điện thoại của tài khoản trên hệ thống!", Toast.LENGTH_LONG).show();
                    return "";
                }

            }
            cursor.close();
            dbHelper.close();

        } catch (Exception e)
        {
            Toast.makeText(QuenMatKhau.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Lỗi đa này", e.toString());

        }
        return "";
    }
    public Account getAccById(int id)
    {
        try
        {

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.getAccountInfoById(id);
            if (cursor != null)
            {
                // Kiểm tra xem Cursor có chứa bất kỳ hàng nào không
                if (cursor.moveToFirst())
                {

                    Account acc = new Account(cursor.getString(1), cursor.getString(2),
                             cursor.getInt(3));
                    return acc;
                } else
                {
                    Toast.makeText(QuenMatKhau.this, "Không tìm thấy tài khoản trên hệ thống!", Toast.LENGTH_LONG).show();
                    return null;
                }

            }
            cursor.close();
            dbHelper.close();

        } catch (Exception e)
        {
            Toast.makeText(QuenMatKhau.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Lỗi đa này", e.toString());

        }
        return null;
    }
    public int getIdByUserName(String username)
    {
        try
        {

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            Cursor cursor = dbHelper.getAccountByUsernameCursor(username);
            if (cursor != null)
            {
                // Kiểm tra xem Cursor có chứa bất kỳ hàng nào không
                if (cursor.moveToFirst())
                {
                    int id = Integer.parseInt(cursor.getString(0));
                    return id;
                } else
                {
                    Toast.makeText(QuenMatKhau.this, "Không tìm thấy tài khoản trên hệ thống!", Toast.LENGTH_LONG).show();
                    return -1;
                }

            }
            cursor.close();
            dbHelper.close();

        } catch (Exception e)
        {
            Toast.makeText(QuenMatKhau.this, e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Lỗi đa này", e.toString());

        }
        return -1;
    }

    public void initDatabase() {
     //   DatabaseHelper db = new DatabaseHelper(this);
        //acc
//        db.addAccountToDatabase(new Account( "khanh", "12345",  1));
//        db.addAccountToDatabase(new Account( "khai", "12345",  1));
//        db.addAccountToDatabase(new Account( "bao", "12345", 1));
//        db.addAccountToDatabase(new Account( "dung", "12345",  1));
//        //profile
//        db.addAProfile(new Profile("khanh", "0393511358", "bangbang2k3kul@gmail.com", "khanh"));
//        db.addAProfile(new Profile("khai", "0385946895", "abc@gmail.com", "khai"));
//        db.addAProfile(new Profile("bao", "3", "abc@gmail.com", "bao"));
//        db.addAProfile(new Profile("dung", "2", "abc@gmail.com", "dung"));
//        //room
//
//        db.addRoomToDatabase(new Room("501", "A", 5+"", 700000 , 4));
//        db.addRoomToDatabase(new Room("502", "A", 5+"", 800000, 4));
//        db.addRoomToDatabase(new Room("301", "A", 3+"", 1200000 , 4));
//        db.addRoomToDatabase(new Room("303", "A", 3+"", 800000, 4));

        //request
//        db.addARenewRequest(new Request("khanh", "501", "A", "1", "2024", 1, 0));
//        db.addARenewRequest(new Request("khai", "502", "A", "2", "2024", 2, 1));
//        db.addARenewRequest(new Request("bao", "301", "A", "1", "2024", 2, 0));
//        db.addARenewRequest(new Request("dung", "303", "A", "2", "2024", 1, 0));
//        db.addARenewRequest(new Request("khanh", "501", "A", "1", "2024", 2, -1));
//        db.addARenewRequest(new Request("khai", "301", "A", "2", "2024", 1, 0));

      //  db.close();
    }

}